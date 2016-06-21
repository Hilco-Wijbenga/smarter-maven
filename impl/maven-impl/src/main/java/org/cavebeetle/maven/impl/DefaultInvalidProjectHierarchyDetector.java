package org.cavebeetle.maven.impl;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Optional.of;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven.Gav;
import org.cavebeetle.maven.GavGenerator;
import org.cavebeetle.maven.GavToProjectMap;
import org.cavebeetle.maven.GavTuple;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.InvalidProjectHierarchyDetector;
import org.cavebeetle.maven.SnapshotDetector;
import com.google.common.base.Optional;

/**
 * The implementation of {@code InvalidProjectHierarchyDetector}.
 */
@Singleton
public final class DefaultInvalidProjectHierarchyDetector
        implements
            InvalidProjectHierarchyDetector
{
    private final SnapshotDetector snapshotDetector;
    private final GavGenerator gavGenerator;

    /**
     * Creates a new {@code DefaultInvalidProjectHierarchyDetector}.
     *
     * @param internalApi
     *            the {@code InternalApi} instance.
     */
    @Inject
    public DefaultInvalidProjectHierarchyDetector(final InternalApi internalApi)
    {
        checkNotNull(internalApi, "Missing 'internalApi'.");
        snapshotDetector = internalApi.getSnapshotDetector();
        gavGenerator = internalApi.getGavGenerator();
    }

    private int checkMaxGroupAndArtifactLength(final int maxGroupAndArtifactLength, final String groupAndArtifact)
    {
        final int gavLength = groupAndArtifact.length();
        if (maxGroupAndArtifactLength < gavLength)
        {
            return gavLength;
        }
        return maxGroupAndArtifactLength;
    }

    @Override
    public Optional<String> getInvalidProjectHierarchyError(final GavToProjectMap gavToProjectMap)
    {
        final List<String> errors = newArrayList();
        for (final Gav gav : gavToProjectMap)
        {
            final MavenProject mavenProject = gavToProjectMap.getProject(gav).getMavenProject();
            if (!snapshotDetector.isSnapshot(mavenProject))
            {
                final List<Gav> snapshotGavs = newArrayList();
                int maxGroupAndArtifactLength = -1;
                final Optional<MavenProject> maybeParentMavenProject = fromNullable(mavenProject.getParent());
                if (maybeParentMavenProject.isPresent())
                {
                    final MavenProject parentMavenProject = maybeParentMavenProject.get();
                    if (snapshotDetector.isSnapshot(parentMavenProject))
                    {
                        snapshotGavs.add(gavGenerator.getGav(parentMavenProject));
                        final String groupAndArtifact = parentMavenProject.getGroupId() + ":" + parentMavenProject.getArtifactId();
                        maxGroupAndArtifactLength = checkMaxGroupAndArtifactLength(maxGroupAndArtifactLength, groupAndArtifact);
                    }
                }
                final List<Dependency> dependencies = mavenProject.getDependencies();
                for (final Dependency dependency : dependencies)
                {
                    if (snapshotDetector.isSnapshot(dependency))
                    {
                        final String groupAndArtifact = dependency.getGroupId() + ":" + dependency.getArtifactId();
                        final int groupAndArtifactLength = groupAndArtifact.length();
                        if (maxGroupAndArtifactLength < groupAndArtifactLength)
                        {
                            maxGroupAndArtifactLength = groupAndArtifactLength;
                        }
                        snapshotGavs.add(gavGenerator.getGav(dependency));
                    }
                }
                if (!snapshotGavs.isEmpty())
                {
                    final String groupAndArtifactMask = "     %-" + maxGroupAndArtifactLength + "s   %s";
                    final Gav mavenProjectGav = gavGenerator.getGav(mavenProject);
                    errors.add("");
                    errors.add("");
                    errors.add("Release project '" + mavenProjectGav + "' depends on");
                    errors.add("");
                    for (final Gav dependencyGav : snapshotGavs)
                    {
                        final String groupAndArtifact = dependencyGav.getGroupId() + ":" + dependencyGav.getArtifactId();
                        errors.add(format(groupAndArtifactMask, groupAndArtifact, dependencyGav.getVersion()));
                    }
                }
            }
        }
        if (!errors.isEmpty())
        {
            final StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("\n");
            errorMessage.append("[ERROR]\n");
            errorMessage.append("[ERROR] !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
            errorMessage.append("[ERROR] !!!                           !!!\n");
            errorMessage.append("[ERROR] !!! INVALID PROJECT HIERARCHY !!!\n");
            errorMessage.append("[ERROR] !!!                           !!!\n");
            errorMessage.append("[ERROR] !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
            for (final String error : errors)
            {
                errorMessage.append("[ERROR] ").append(error).append('\n');
            }
            errorMessage.append("[ERROR]");
            return of(errorMessage.toString());
        }
        return absent();
    }

    @Override
    public List<String> getProjectHierarchyWarnings(final GavToProjectMap gavToProjectMap)
    {
        final Map<Gav, List<GavTuple>> map = newHashMap();
        for (final Gav gav : gavToProjectMap)
        {
            final List<GavTuple> gavTuples = newArrayList();
            map.put(gav, gavTuples);
        }
        for (final Gav gav : gavToProjectMap)
        {
            final MavenProject project = gavToProjectMap.getProject(gav).getMavenProject();
            final Optional<MavenProject> maybeParent = fromNullable(project.getParent());
            if (maybeParent.isPresent())
            {
                final MavenProject parent = maybeParent.get();
                final Gav parentGav = gavGenerator.getGav(parent);
                final Optional<Gav> maybeParentGav = findClosestGav(parentGav, gavToProjectMap);
                if (maybeParentGav.isPresent())
                {
                    final Gav parentGav_ = maybeParentGav.get();
                    if (!parentGav.equals(parentGav_))
                    {
                        map.get(parentGav_).add(new GavTuple(gav, parentGav));
                    }
                }
            }
            final List<Dependency> dependencies = project.getDependencies();
            if (!dependencies.isEmpty())
            {
                for (final Dependency dependency : dependencies)
                {
                    final Gav dependencyGav = gavGenerator.getGav(dependency);
                    final Optional<Gav> maybeDependencyGav = findClosestGav(dependencyGav, gavToProjectMap);
                    if (maybeDependencyGav.isPresent())
                    {
                        final Gav dependencyGav_ = maybeDependencyGav.get();
                        if (!dependencyGav.equals(dependencyGav_))
                        {
                            map.get(dependencyGav_).add(new GavTuple(gav, dependencyGav));
                        }
                    }
                }
            }
        }
        final List<String> warnings = newArrayList();
        for (final Gav gav : map.keySet())
        {
            final List<GavTuple> gavTuples = map.get(gav);
            if (!gavTuples.isEmpty())
            {
                warnings.add("");
                warnings.add("");
                warnings.add("Project " + gav.getGroupId() + ":" + gav.getArtifactId() + " has version " + gav.getVersion());
                warnings.add("");
                warnings.add("    but");
                warnings.add("");
                int maxGroupAndArtifactLength = -1;
                for (final GavTuple gavTuple : gavTuples)
                {
                    final Gav projectGav = gavTuple.getProjectGav();
                    final String projectGroupAndArtifact = projectGav.getGroupId() + ":" + projectGav.getArtifactId();
                    final int groupAndArtifactLength = projectGroupAndArtifact.length();
                    if (maxGroupAndArtifactLength < groupAndArtifactLength)
                    {
                        maxGroupAndArtifactLength = groupAndArtifactLength;
                    }
                }
                final String groupAndArtifactMask = "        %-" + maxGroupAndArtifactLength + "s depends on %s";
                for (final GavTuple gavTuple : gavTuples)
                {
                    final Gav projectGav = gavTuple.getProjectGav();
                    final String projectGroupAndArtifact = projectGav.getGroupId() + ":" + projectGav.getArtifactId();
                    final Gav dependencyGav = gavTuple.getDependencyGav();
                    warnings.add(format(groupAndArtifactMask, projectGroupAndArtifact, dependencyGav.getVersion()));
                }
            }
        }
        return warnings;
    }

    private Optional<Gav> findClosestGav(final Gav gav_, final GavToProjectMap gavToProjectMap)
    {
        for (final Gav gav : gavToProjectMap)
        {
            if (gav.getGroupId().equals(gav_.getGroupId()) && gav.getArtifactId().equals(gav_.getArtifactId()))
            {
                return of(gav);
            }
        }
        return absent();
    }
}

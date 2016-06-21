package org.cavebeetle.maven.impl;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static org.cavebeetle.maven.DirtyReason.NOT_DIRTY;
import static org.cavebeetle.maven.DirtyReason.PUBLISHED;
import static org.cavebeetle.maven.impl.Equalizer.isEqual;
import java.io.File;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven.ArtifactDetector;
import org.cavebeetle.maven.DirtDetector;
import org.cavebeetle.maven.DirtyReason;
import org.cavebeetle.maven.GavGenerator;
import org.cavebeetle.maven.GavToProjectMap;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.Project;
import org.cavebeetle.maven.SnapshotDetector;
import org.cavebeetle.maven.SourceFilesDigest;
import org.cavebeetle.maven.SourceFilesHashGenerator;
import org.codehaus.plexus.logging.Logger;
import com.google.common.base.Optional;

/**
 * The implementation of {@code DirtDetector}.
 */
@Singleton
public final class DefaultDirtDetector
        implements
            DirtDetector
{
    /**
     * Returns the first of the {@code Project}'s dirty dependencies or {@code Optional.absent()} if there are none.
     *
     * @param project
     *            the {@code Project} to investigate.
     * @param includeModules
     *            whether to include the project's modules when determining "dirtiness".
     * @return the first of the {@code Project}'s dirty dependencies or {@code Optional.absent()} if there are none.
     */
    public static Optional<Project> hasDirtyDependency(
            final Project project,
            final boolean includeModules)
    {
        for (final Project dependency : project.getDependencies())
        {
            final DirtyReason dependencyDirtyReason = dependency.findDirtyReason(false);
            if (dependencyDirtyReason.isDirty())
            {
                return of(dependency);
            }
        }
        if (includeModules)
        {
            for (final Project module : project.getModules())
            {
                final DirtyReason moduleDirtyReason = module.findDirtyReason(false);
                if (moduleDirtyReason.isDirty())
                {
                    return of(module);
                }
            }
        }
        return absent();
    }

    private final InternalApi internalApi;
    private final SnapshotDetector snapshotDetector;
    private final ArtifactDetector artifactDetector;
    private final GavGenerator gavGenerator;
    private final SourceFilesHashGenerator sourceFilesHashGenerator;

    /**
     * Creates a new {@code DefaultDirtDetector}.
     *
     * @param internalApi
     *            the {@code InternalApi} instance.
     */
    @Inject
    public DefaultDirtDetector(
            final InternalApi internalApi)
    {
        this.internalApi = internalApi;
        artifactDetector = internalApi.getArtifactDetector();
        gavGenerator = internalApi.getGavGenerator();
        snapshotDetector = internalApi.getSnapshotDetector();
        sourceFilesHashGenerator = internalApi.getSourceFilesHashGenerator();
    }

    @Override
    public DirtyReason findDirtyReason(
            final Logger logger,
            final MavenSession session,
            final MavenProject mavenProject,
            final GavToProjectMap gavToProjectMap,
            final boolean includeModules)
    {
        if (!snapshotDetector.isSnapshot(mavenProject) && artifactDetector.hasArtifactInRemoteRepository(mavenProject))
        {
            return PUBLISHED;
        }
        final File targetDir = new File(mavenProject.getBuild().getDirectory());
        final boolean targetDirExists = targetDir.exists();
        if (!targetDirExists)
        {
            return newNoBuildDirectoryDirtyReason(session, targetDir);
        }
        final boolean artifactInLocalRepository = artifactDetector.hasArtifactInLocalRepository(session, mavenProject);
        if (!artifactInLocalRepository)
        {
            return newNotInLocalRepoDirtyReason(session);
        }
        final Project project = gavToProjectMap.getProject(gavGenerator.getGav(mavenProject));
        final File projectHashFile = project.getSourceFilesFile();
        final boolean projectHashFileExists = projectHashFile.exists();
        final String projectRootDir = session.getExecutionRootDirectory();
        final String projectHash = projectHashFile.getPath().substring(projectRootDir.length() + 1);
        if (!projectHashFileExists)
        {
            return internalApi.newDirtyReason(true, "No source file digest (" + projectHash + ")");
        }
        final SourceFilesDigest expectedProjectSourceFilesDigest = internalApi.newSourceFilesDigest(projectHashFile);
        final int digestLength = expectedProjectSourceFilesDigest.getDigest().toString().length();
        final Set<String> files = newHashSet();
        final Map<String, String> expectedFileToDigestMap = newHashMap();
        try
        {
            loadExpectedFileToDigestMap(expectedProjectSourceFilesDigest, digestLength, files, expectedFileToDigestMap);
        }
        catch (final Exception exception)
        {
            return internalApi.newDirtyReason(true, "Invalid/corrupt source file digest (" + projectHash + ")");
        }
        final SourceFilesDigest actualProjectSourceFilesDigest = sourceFilesHashGenerator.generateUsingCache(project);
        if (!isEqual(expectedProjectSourceFilesDigest, actualProjectSourceFilesDigest))
        {
            return newFileChangedDirtyReason(digestLength, files, expectedFileToDigestMap, actualProjectSourceFilesDigest);
        }
        final Optional<Project> maybeFirstDirtyDependency = hasDirtyDependency(project, includeModules);
        if (maybeFirstDirtyDependency.isPresent())
        {
            return internalApi.newDirtyReason(true, maybeFirstDirtyDependency.get().getGav().toString());
        }
        return NOT_DIRTY;
    }

    private DirtyReason newNoBuildDirectoryDirtyReason(final MavenSession session, final File targetDir)
    {
        final String projectRootDir = session.getExecutionRootDirectory();
        final String target = targetDir.getPath().substring(projectRootDir.length() + 1);
        return internalApi.newDirtyReason(true, "No build directory (" + target + ")");
    }

    private DirtyReason newNotInLocalRepoDirtyReason(final MavenSession session)
    {
        final File localMavenRepoDir = artifactDetector.getLocalRepositoryDirectory(session);
        final String userHomeDir = System.getProperty("user.home");
        final String localMavenRepo = localMavenRepoDir.getPath().startsWith(userHomeDir)
            ? "~" + localMavenRepoDir.getPath().substring(userHomeDir.length())
            : localMavenRepoDir.getPath();
        return internalApi.newDirtyReason(true, "Not in local repo (" + localMavenRepo + ")");
    }

    private DirtyReason newFileChangedDirtyReason(
            final int digestLength,
            final Set<String> files,
            final Map<String, String> expectedFileToDigestMap,
            final SourceFilesDigest actualProjectSourceFilesDigest)
    {
        final Map<String, String> actualFileToDigestMap = newHashMap();
        loadExpectedFileToDigestMap(actualProjectSourceFilesDigest, digestLength, files, actualFileToDigestMap);
        final String changedFile = findChangedFile(files, expectedFileToDigestMap, actualFileToDigestMap);
        return internalApi.newDirtyReason(true, changedFile);
    }

    private void loadExpectedFileToDigestMap(
            final SourceFilesDigest expectedProjectSourceFilesDigest,
            final int digestLength,
            final Set<String> files,
            final Map<String, String> expectedFileToDigestMap)
    {
        for (final String line : expectedProjectSourceFilesDigest)
        {
            final String digest = line.substring(0, digestLength);
            final String file = line.substring(digestLength + 1);
            files.add(file);
            expectedFileToDigestMap.put(file, digest);
        }
    }

    private String findChangedFile(
            final Set<String> files,
            final Map<String, String> expectedFileToDigestMap,
            final Map<String, String> actualFileToDigestMap)
    {
        for (final String file : files)
        {
            if (!expectedFileToDigestMap.containsKey(file))
            {
                return "+ " + file;
            }
            else if (!actualFileToDigestMap.containsKey(file))
            {
                return "- " + file;
            }
            else
            {
                final String expectedDigest = expectedFileToDigestMap.get(file);
                final String actualDigest = actualFileToDigestMap.get(file);
                if (!expectedDigest.equals(actualDigest))
                {
                    return file;
                }
            }
        }
        throw new IllegalStateException("This should never happen.");
    }
}

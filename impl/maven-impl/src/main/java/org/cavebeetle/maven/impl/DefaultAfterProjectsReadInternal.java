package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.cavebeetle.maven.AfterProjectsReadInternal;
import org.cavebeetle.maven.DirtyReason;
import org.cavebeetle.maven.Gav;
import org.cavebeetle.maven.GavGenerator;
import org.cavebeetle.maven.GavToProjectMap;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.MavenExecutionListener;
import org.cavebeetle.maven.Project;
import org.codehaus.plexus.logging.Logger;

/**
 * The implementation of {@code AfterProjectsReadInternal}.
 */
@Singleton
public final class DefaultAfterProjectsReadInternal
        implements
            AfterProjectsReadInternal
{
    private final InternalApi internalApi;
    private final GavGenerator gavGenerator;

    /**
     * Creates a new {@code DefaultAfterProjectsReadInternal}.
     *
     * @param internalApi
     *            the {@code InternalApi} instance.
     */
    @Inject
    public DefaultAfterProjectsReadInternal(final InternalApi internalApi)
    {
        checkNotNull(internalApi, "Missing 'internalApi'.");
        this.internalApi = internalApi;
        gavGenerator = internalApi.getGavGenerator();
    }

    @Override
    public GavToProjectMap initializeGavToProjectMap(
            final Logger logger,
            final MavenSession mavenSession,
            final ProjectBuilder projectBuilder)
    {
        checkNotNull(logger, "Missing 'logger'.");
        checkNotNull(mavenSession, "Missing 'mavenSession'.");
        checkNotNull(projectBuilder, "Missing 'projectBuilder'.");
        final GavToProjectMap gavToProjectMap = internalApi.newGavToProjectMap();
        for (final MavenProject mavenProject : mavenSession.getProjects())
        {
            final Gav projectGav = gavGenerator.getGav(mavenProject);
            final Project project =
                    internalApi.newProject(
                            logger,
                            mavenSession,
                            projectBuilder,
                            mavenProject,
                            gavToProjectMap);
            gavToProjectMap.putProject(projectGav, project);
        }
        return gavToProjectMap;
    }

    @Override
    public MavenExecutionRequest getMavenExecutionRequest(
            final Logger logger,
            final MavenSession mavenSession,
            final GavToProjectMap gavToProjectMap)
    {
        checkNotNull(logger, "Missing 'logger'.");
        checkNotNull(mavenSession, "Missing 'mavenSession'.");
        checkNotNull(gavToProjectMap, "Missing 'gavToProjectMap'.");
        final MavenExecutionListener mavenExecutionListener = internalApi.getMavenExecutionListener();
        mavenExecutionListener.init(logger, mavenSession, gavToProjectMap);
        final MavenExecutionRequest mavenExecutionRequest = mavenSession.getRequest();
        mavenExecutionRequest.setExecutionListener(mavenExecutionListener);
        return mavenExecutionRequest;
    }

    @Override
    public List<MavenProject> collectDirtyProjects(
            final Logger logger,
            final MavenSession mavenSession,
            final GavToProjectMap gavToProjectMap)
    {
        checkNotNull(mavenSession, "Missing 'mavenSession'.");
        checkNotNull(gavToProjectMap, "Missing 'gavToProjectMap'.");
        final String dirtyReasonMask = getDirtyReasonMask(gavToProjectMap);
        final List<MavenProject> dirtyProjects = newArrayList();
        for (final MavenProject mavenProject : mavenSession.getProjects())
        {
            final Gav projectGav = gavGenerator.getGav(mavenProject);
            final Project project = gavToProjectMap.getProject(projectGav);
            final DirtyReason dirtyReason = project.findDirtyReason(true);
            final String dirtyAsText = dirtyReason.isDirty() ? "*" : " ";
            final String dirtyReasonMessage = format(dirtyReasonMask, projectGav, dirtyAsText, dirtyReason.getReason());
            logger.info(dirtyReasonMessage);
            if (dirtyReason.isDirty())
            {
                dirtyProjects.add(mavenProject);
            }
        }
        return dirtyProjects;
    }

    private String getDirtyReasonMask(final GavToProjectMap gavToProjectMap)
    {
        int maxGavLength = 0;
        for (final Gav gav : gavToProjectMap)
        {
            final int gavLength = gav.toString().length();
            if (gavLength > maxGavLength)
            {
                maxGavLength = gavLength;
            }
        }
        return "%-" + maxGavLength + "s [%1s] %s";
    }

    @Override
    public MavenProject createDummyProjectToIndicateNothingToDo()
    {
        final MavenProject dummyProject = new MavenProject();
        dummyProject.setArtifactId("nothing");
        dummyProject.setVersion("(everything is up-to-date).");
        return dummyProject;
    }

    @Override
    public MavenProject createDummyProjectToIndicateProjectHierarchyCheck()
    {
        final MavenProject dummyProject = new MavenProject();
        dummyProject.setArtifactId("nothing");
        dummyProject.setVersion("(only checking the project hierarchy).");
        return dummyProject;
    }
}

package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.maven.BuildAbort;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.cavebeetle.maven.ActiveDetector;
import org.cavebeetle.maven.AfterProjectsRead;
import org.cavebeetle.maven.AfterProjectsReadInternal;
import org.cavebeetle.maven.GavToProjectMap;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.InvalidProjectHierarchyDetector;
import org.codehaus.plexus.logging.Logger;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

/**
 * The implementation of {@code AfterProjectsRead}.
 */
@Singleton
public final class DefaultAfterProjectsRead
        implements
            AfterProjectsRead
{
    private final ActiveDetector activeDetector;
    private final AfterProjectsReadInternal afterProjectsReadInternal;
    private final InvalidProjectHierarchyDetector invalidProjectHierarchyDetector;

    /**
     * Creates a new {@code DefaultAfterProjectsRead}.
     *
     * @param internalApi
     *            the {@code InternalApi} instance.
     */
    @Inject
    public DefaultAfterProjectsRead(final InternalApi internalApi)
    {
        checkNotNull(internalApi, "Missing 'internalApi'.");
        activeDetector = internalApi.getActiveDetector();
        afterProjectsReadInternal = internalApi.getAfterProjectsReadInternal();
        invalidProjectHierarchyDetector = internalApi.getInvalidProjectHierarchyDetector();
    }

    @Override
    public void afterProjectsRead(
            final Logger logger,
            final RuntimeInformation runtimeInformation,
            final MavenSession mavenSession,
            final ProjectBuilder projectBuilder)
    {
        checkNotNull(logger, "Missing 'logger'.");
        checkNotNull(runtimeInformation, "Missing 'runtimeInformation'.");
        checkNotNull(mavenSession, "Missing 'mavenSession'.");
        checkNotNull(projectBuilder, "Missing 'projectBuilder'.");
        if (activeDetector.showBanner(mavenSession))
        {
            final GavToProjectMap gavToProjectMap =
                    afterProjectsReadInternal.initializeGavToProjectMap(logger, mavenSession, projectBuilder);
            final Optional<String> maybeErrorMessage;
            maybeErrorMessage = invalidProjectHierarchyDetector.getInvalidProjectHierarchyError(gavToProjectMap);
            if (maybeErrorMessage.isPresent())
            {
                final String errorMessage = maybeErrorMessage.get();
                throw new BuildAbort(errorMessage);
            }
            final MavenExecutionRequest mavenExecutionRequest =
                    afterProjectsReadInternal.getMavenExecutionRequest(logger, mavenSession, gavToProjectMap);
            if (activeDetector.isSmarterMavenActive(mavenSession))
            {
                if (mavenExecutionRequest.getSelectedProjects().isEmpty())
                {
                    logger.info("");
                    final List<MavenProject> dirtyProjects =
                            afterProjectsReadInternal.collectDirtyProjects(logger, mavenSession, gavToProjectMap);
                    if (!dirtyProjects.isEmpty())
                    {
                        logger.info("");
                    }
                    else
                    {
                        dirtyProjects.add(afterProjectsReadInternal.createDummyProjectToIndicateNothingToDo());
                    }
                    mavenSession.setProjects(dirtyProjects);
                }
            }
            else if (activeDetector.showProjectHierarchyWarnings(mavenSession))
            {
                mavenSession
                        .getUserProperties()
                        .setProperty(ActiveDetector.SHOW_PROJECT_HIERARCHY_WARNINGS_PROPERTY, "TRUE");
                mavenSession.getGoals().clear();
                mavenSession.getGoals().add("validate");
                final MavenProject dummyProject =
                        afterProjectsReadInternal.createDummyProjectToIndicateProjectHierarchyCheck();
                mavenSession.setProjects(Lists.newArrayList(dummyProject));
            }
        }
    }
}

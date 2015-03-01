package org.cavebeetle.maven.impl;

import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Sets.newHashSet;
import java.io.File;
import java.util.Deque;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.maven.execution.ExecutionEvent;
import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven.Gav;
import org.cavebeetle.maven.GavGenerator;
import org.cavebeetle.maven.GavToProjectMap;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.InvalidProjectHierarchyDetector;
import org.cavebeetle.maven.MavenExecutionListener;
import org.cavebeetle.maven.Project;
import org.cavebeetle.maven.SourceFilesHashGenerator;
import org.codehaus.plexus.logging.Logger;

/**
 * The implementation of {@code MavenExecutionListener}.
 */
@Singleton
public final class DefaultMavenExecutionListener
        implements
            MavenExecutionListener
{
    private final GavGenerator gavGenerator;
    private final SourceFilesHashGenerator sourceFilesHashGenerator;
    private final InvalidProjectHierarchyDetector invalidProjectHierarchyDetector;
    private Logger logger;
    private ExecutionListener delegate;
    private GavToProjectMap gavToProjectMap;

    /**
     * Creates a new {@code DefaultMavenExecutionListener}.
     *
     * @param internalApi
     *            the {@code InternalApi} instance.
     */
    @Inject
    public DefaultMavenExecutionListener(
            final InternalApi internalApi)
    {
        gavGenerator = internalApi.getGavGenerator();
        sourceFilesHashGenerator = internalApi.getSourceFilesHashGenerator();
        invalidProjectHierarchyDetector = internalApi.getInvalidProjectHierarchyDetector();
    }

    @Override
    public void forkedProjectFailed(
            final ExecutionEvent event)
    {
        delegate.forkedProjectFailed(event);
    }

    @Override
    public void forkedProjectStarted(
            final ExecutionEvent event)
    {
        delegate.forkedProjectStarted(event);
    }

    @Override
    public void forkedProjectSucceeded(
            final ExecutionEvent event)
    {
        delegate.forkedProjectSucceeded(event);
    }

    @Override
    public void forkFailed(
            final ExecutionEvent event)
    {
        delegate.forkFailed(event);
    }

    @Override
    public void forkStarted(
            final ExecutionEvent event)
    {
        delegate.forkStarted(event);
    }

    @Override
    public void forkSucceeded(
            final ExecutionEvent event)
    {
        delegate.forkSucceeded(event);
    }

    @Override
    public void init(
            final Logger logger_,
            final MavenSession session,
            final GavToProjectMap gavToProjectMap_)
    {
        logger = logger_;
        delegate = session.getRequest().getExecutionListener();
        gavToProjectMap = gavToProjectMap_;
        final Set<Project> finishedProjects = newHashSet();
        final Deque<Project> projects = newLinkedList();
        for (final Gav gav : gavToProjectMap)
        {
            final Project project = gavToProjectMap.getProject(gav);
            projects.add(project);
        }
        while (!projects.isEmpty())
        {
            final Project project = projects.pop();
            boolean dependenciesFinished = true;
            for (final Project dependency : project.getDependencies())
            {
                if (!finishedProjects.contains(dependency))
                {
                    dependenciesFinished = false;
                    break;
                }
            }
            if (!dependenciesFinished)
            {
                projects.addLast(project);
            }
            else
            {
                sourceFilesHashGenerator.generate(project);
                finishedProjects.add(project);
            }
        }
    }

    @Override
    public void mojoFailed(
            final ExecutionEvent event)
    {
        delegate.mojoFailed(event);
    }

    @Override
    public void mojoSkipped(
            final ExecutionEvent event)
    {
        delegate.mojoSkipped(event);
    }

    @Override
    public void mojoStarted(
            final ExecutionEvent event)
    {
        delegate.mojoStarted(event);
    }

    @Override
    public void mojoSucceeded(
            final ExecutionEvent event)
    {
        delegate.mojoSucceeded(event);
    }

    @Override
    public void projectDiscoveryStarted(
            final ExecutionEvent event)
    {
        delegate.projectDiscoveryStarted(event);
    }

    @Override
    public void projectSkipped(
            final ExecutionEvent event)
    {
        delegate.projectSkipped(event);
    }

    @Override
    public void projectStarted(
            final ExecutionEvent event)
    {
        delegate.projectStarted(event);
    }

    @Override
    public void projectFailed(
            final ExecutionEvent event)
    {
        delegate.projectFailed(event);
    }

    @Override
    public void projectSucceeded(
            final ExecutionEvent event)
    {
        delegate.projectSucceeded(event);
        final MavenProject mavenProject = event.getSession().getCurrentProject();
        if (mavenProject.getArtifact() != null)
        {
            final File targetDir = new File(mavenProject.getBuild().getDirectory());
            final Gav gav = gavGenerator.getGav(mavenProject);
            final Project project = gavToProjectMap.getProject(gav);
            sourceFilesHashGenerator.generate(project, targetDir);
        }
    }

    @Override
    public void sessionStarted(
            final ExecutionEvent event)
    {
        delegate.sessionStarted(event);
    }

    @Override
    public void sessionEnded(
            final ExecutionEvent event)
    {
        delegate.sessionEnded(event);
        final List<String> warnings = invalidProjectHierarchyDetector.getProjectHierarchyWarnings(gavToProjectMap);
        if (!warnings.isEmpty())
        {
            logger.warn("");
            logger.warn("");
            logger.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            logger.warn("!!!                            !!!");
            logger.warn("!!! PROJECT HIERARCHY WARNINGS !!!");
            logger.warn("!!!                            !!!");
            logger.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            for (final String warning : warnings)
            {
                logger.warn(warning);
            }
            logger.warn("");
            logger.warn("");
        }
    }
}

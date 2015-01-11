package org.cavebeetle.maven;

import java.util.List;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.codehaus.plexus.logging.Logger;

/**
 * A {@code AfterProjectsReadInternal} represents the steps that a {@code AfterProjectsRead} takes.
 */
public interface AfterProjectsReadInternal
{
    /**
     * Initializes the {@code GavToProjectMap}.
     *
     * @param logger
     *            the {@code Logger} instance.
     * @param mavenSession
     *            the {@code MavenSession} instance.
     * @param projectBuilder
     *            the {@code ProjectBuilder} instance.
     * @return an initialized {@code GavToProjectMap}.
     */
    GavToProjectMap initializeGavToProjectMap(
            Logger logger,
            MavenSession mavenSession,
            ProjectBuilder projectBuilder);

    /**
     * Gets the {@code MavenExecutionRequest} for the current build.
     *
     * @param logger
     *            the {@code Logger} instance.
     * @param mavenSession
     *            the {@code MavenSession} instance.
     * @param gavToProjectMap
     *            the {@code GavToProjectMap} instance.
     * @return the {@code MavenExecutionRequest} for the current build
     */
    MavenExecutionRequest getMavenExecutionRequest(
            Logger logger,
            MavenSession mavenSession,
            GavToProjectMap gavToProjectMap);

    /**
     * Collects the dirty projects from those available in the given {@code GavToProjectMap}.
     *
     * @param logger
     *            the {@code Logger} instance.
     * @param mavenSession
     *            the {@code MavenSession} instance.
     * @param gavToProjectMap
     *            the {@code GavToProjectMap} instance.
     * @return the dirty projects from those available in the given {@code GavToProjectMap}.
     */
    List<MavenProject> collectDirtyProjects(
            Logger logger,
            MavenSession mavenSession,
            GavToProjectMap gavToProjectMap);

    /**
     * Creates a dummy Maven project to create the correct output when nothing needs to be rebuilt.
     *
     * @return a dummy Maven project to create the correct output when nothing needs to be rebuilt.
     */
    MavenProject createDummyProjectToIndicateNothingToDo();
}

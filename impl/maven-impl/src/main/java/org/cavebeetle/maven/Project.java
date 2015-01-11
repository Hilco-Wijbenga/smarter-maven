package org.cavebeetle.maven;

import java.io.File;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.codehaus.plexus.logging.Logger;

/**
 * A {@code Project} represents a Maven project/artifact/dependency.
 */
public interface Project
{
    /**
     * A factory for {@code Project} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code Project}.
         *
         * @param logger
         *            the {@code Logger} instance.
         * @param mavenSession
         *            the {@code MavenSession} instance.
         * @param mavenProjectBuilder
         *            the {@code ProjectBuilder} instance.
         * @param mavenProject
         *            the {@code MavenProject} associated with the requested {@code Project}.
         * @param gavToProjectMap
         *            the {@code GavToProjectMap} instance.
         * @return a new {@code Project}.
         */
        Project newProject(
                Logger logger,
                MavenSession mavenSession,
                ProjectBuilder mavenProjectBuilder,
                MavenProject mavenProject,
                GavToProjectMap gavToProjectMap);
    }

    /**
     * Gets the {@code MavenProject} associated with this {@code Project}.
     *
     * @return the {@code MavenProject} associated with this {@code Project}.
     */
    MavenProject getMavenProject();

    /**
     * Gets the base directory for the source files of this {@code Project}. Normally, this is the directory that stores
     * the project's POM.
     *
     * @return the base directory for the source files of this {@code Project}.
     */
    File getBaseDir();

    /**
     * Gets the "target" directory.
     *
     * @return the "target" directory.
     */
    File getTargetDir();

    /**
     * Gets the {@code Gav} that uniquely identifies this {@code Project}.
     *
     * @return the {@code Gav} that uniquely identifies this {@code Project}.
     */
    Gav getGav();

    /**
     * Returns the {@code DirtyReason} explaining why this {@code Project} needs to be rebuilt.
     *
     * @param includeModules
     *            whether to include the project's modules when determining dirtiness.
     * @return the {@code DirtyReason} explaining why this {@code Project} needs to be rebuilt.
     */
    DirtyReason findDirtyReason(
            boolean includeModules);

    /**
     * Returns an {@code Iterable<Project>} that includes all dependencies.
     *
     * @return an {@code Iterable<Project>} that includes all dependencies.
     */
    Iterable<Project> getDependencies();

    /**
     * Returns an {@code Iterable<Project>} that includes all modules.
     *
     * @return an {@code Iterable<Project>} that includes all modules.
     */
    Iterable<Project> getModules();
}

package org.cavebeetle.maven;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.codehaus.plexus.logging.Logger;

/**
 * A {@code AfterProjectsRead} handles the {@code afterProjectsRead} event.
 */
public interface AfterProjectsRead
{
    /**
     * Handles the {@code afterProjectsRead} event.
     *
     * @param logger
     *            the {@code Logger} instance.
     * @param runtimeInformation
     *            the {@code RuntimeInformation} instance.
     * @param mavenSession
     *            the {@code MavenSession} instance.
     * @param mavenProjectBuilder
     *            the {@code ProjectBuilder} instance.
     */
    void afterProjectsRead(
            Logger logger,
            RuntimeInformation runtimeInformation,
            MavenSession mavenSession,
            ProjectBuilder mavenProjectBuilder);
}

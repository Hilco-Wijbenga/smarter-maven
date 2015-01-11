package org.cavebeetle.maven;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.codehaus.plexus.logging.Logger;

/**
 * A {@code AfterSessionStart} handles the {@code afterSessionStart} event.
 */
public interface AfterSessionStart
{
    /**
     * Handles the {@code afterProjectsRead} event.
     *
     * @param smartMavenVersion
     *            the {@code MavenVersion} of the running Smart Maven extension.
     * @param logger
     *            the {@code Logger} instance.
     * @param runtimeInformation
     *            the {@code RuntimeInformation} instance.
     * @param mavenSession
     *            the {@code MavenSession} instance.
     */
    void afterSessionStart(
            MavenVersion smartMavenVersion,
            Logger logger,
            RuntimeInformation runtimeInformation,
            MavenSession mavenSession);
}

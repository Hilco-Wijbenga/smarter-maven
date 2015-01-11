package org.cavebeetle.maven;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.logging.Logger;

/**
 * A {@code DirtDetector} detects whether a project is "dirty", i.e. whether it needs to be rebuilt.
 */
public interface DirtDetector
{
    /**
     * Determines whether the given project should be rebuilt.
     *
     * @param logger
     *            the {@code Logger} instance.
     * @param session
     *            the {@code MavenSession} instance.
     * @param project
     *            the {@code MavenProject} to investigate.
     * @param gavToProjectMap
     *            a map that maps {@code Gav}s to their corresponding {@code MavenProject}s.
     * @param includeModules
     *            whether to include a project's modules (as opposed to its dependencies and parent project only) when
     *            determining "dirtiness".
     * @return the {@code DirtyReason} found.
     */
    DirtyReason findDirtyReason(
            Logger logger,
            MavenSession session,
            MavenProject project,
            GavToProjectMap gavToProjectMap,
            boolean includeModules);
}

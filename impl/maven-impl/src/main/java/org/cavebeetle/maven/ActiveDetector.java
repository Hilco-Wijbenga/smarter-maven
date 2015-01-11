package org.cavebeetle.maven;

import org.apache.maven.execution.MavenSession;

/**
 * An {@code ActiveDetector} detects whether the Smart Maven extension is active.
 */
public interface ActiveDetector
{
    /**
     * Returns whether the Smart Maven extension is active. This is the case if the install phase is invoked (directly
     * or indirectly).
     *
     * @param session
     *            the {@code MavenSession} instance.
     * @return {@code true} if and only if the Smart Maven extension is active.
     */
    boolean isActive(
            MavenSession session);
}

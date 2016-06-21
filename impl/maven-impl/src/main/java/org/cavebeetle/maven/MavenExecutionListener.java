package org.cavebeetle.maven;

import org.apache.maven.execution.ExecutionListener;
import org.apache.maven.execution.MavenSession;
import org.codehaus.plexus.logging.Logger;

/**
 * A {@code MavenExecutionListener} initializes a {@code GavToProjectMap}.
 */
public interface MavenExecutionListener
        extends
            ExecutionListener
{
    /**
     * Initializes the given {@code GavToProjectMap}.
     *
     * @param logger
     *            the {@code Logger} instance.
     * @param session
     *            the {@code MavenSession} instance.
     * @param gavToProjectMap
     *            the {@code GavToProjectMap} instance.
     */
    void init(Logger logger, MavenSession session, GavToProjectMap gavToProjectMap);
}

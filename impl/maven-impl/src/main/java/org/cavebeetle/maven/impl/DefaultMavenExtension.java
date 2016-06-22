package org.cavebeetle.maven.impl;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.cavebeetle.maven.AfterProjectsRead;
import org.cavebeetle.maven.AfterSessionStart;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.MavenExtension;
import org.cavebeetle.maven.MavenVersion;
import org.codehaus.plexus.logging.Logger;

/**
 * The implementation of {@code MavenExtension}.
 */
@Singleton
public final class DefaultMavenExtension
        implements
            MavenExtension
{
    private final AfterSessionStart afterSessionStart;
    private final AfterProjectsRead afterProjectsRead;

    /**
     * Creates a new {@code DefaultMavenExtension}.
     *
     * @param internalApi
     *            the {@code InternalApi} instance.
     */
    @Inject
    public DefaultMavenExtension(final InternalApi internalApi)
    {
        afterSessionStart = internalApi.getAfterSessionStart();
        afterProjectsRead = internalApi.getAfterProjectsRead();
    }

    @Override
    public void afterProjectsRead(
            final Logger logger,
            final RuntimeInformation runtime,
            final MavenSession session,
            final ProjectBuilder projectBuilder)
    {
        afterProjectsRead.afterProjectsRead(logger, runtime, session, projectBuilder);
    }

    @Override
    public void afterSessionStart(
            final MavenVersion version,
            final Logger logger,
            final RuntimeInformation runtime,
            final MavenSession session)
    {
        afterSessionStart.afterSessionStart(version, logger, runtime, session);
    }
}

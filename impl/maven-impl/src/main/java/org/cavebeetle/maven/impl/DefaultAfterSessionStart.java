package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.inject.Singleton;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.cavebeetle.maven.AfterSessionStart;
import org.cavebeetle.maven.Banner;
import org.cavebeetle.maven.MavenVersion;
import org.codehaus.plexus.logging.Logger;

/**
 * The implementation of {@code AfterSessionStart}.
 */
@Singleton
public final class DefaultAfterSessionStart
        implements
            AfterSessionStart
{
    @Override
    public void afterSessionStart(
            final MavenVersion smartMavenVersion,
            final Logger logger,
            final RuntimeInformation runtimeInformation,
            final MavenSession mavenSession)
    {
        checkNotNull(smartMavenVersion, "Missing 'smarterMavenVersion'.");
        checkNotNull(logger, "Missing 'logger'.");
        checkNotNull(runtimeInformation, "Missing 'runtimeInformation'.");
        checkNotNull(mavenSession, "Missing 'mavenSession'.");
        final String mavenVersionAsText = runtimeInformation.getMavenVersion();
        final String[] extra = new String[] {
            "",
            " Maven " + mavenVersionAsText,
            " Smarter Maven " + smartMavenVersion,
            " JDK " + System.getProperty("java.version"),
        };
        for (int i = 0; i < Banner.BANNER.length; i++)
        {
            final String bannerLine = Banner.BANNER[i];
            logger.info(bannerLine + extra[i]);
        }
        logger.info("");
    }
}

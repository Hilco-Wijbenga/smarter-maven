package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Properties;
import javax.inject.Singleton;
import org.apache.maven.execution.MavenSession;
import org.cavebeetle.maven.ActiveDetector;

/**
 * The implementation of {@code ActiveDetector}.
 */
@Singleton
public final class DefaultActiveDetector
        implements
            ActiveDetector
{
    @Override
    public boolean isSmarterMavenActive(final MavenSession session)
    {
        checkNotNull(session, "Missing 'session'.");
        return session.getUserProperties().containsKey(ActiveDetector.SMARTER_MAVEN_ACTIVE_PROPERTY);
    }

    @Override
    public boolean showProjectHierarchyWarnings(final MavenSession session)
    {
        checkNotNull(session, "Missing 'session'.");
        final Properties userProperties = session.getUserProperties();
        return userProperties.containsKey(ActiveDetector.SHOW_PROJECT_HIERARCHY_WARNINGS_PROPERTY);
    }

    @Override
    public boolean showBanner(final MavenSession session)
    {
        return isSmarterMavenActive(session) || showProjectHierarchyWarnings(session);
    }
}

package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkNotNull;
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
    public boolean isActive(
            final MavenSession session)
    {
        checkNotNull(session, "Missing 'session'.");
        for (final String goal : session.getGoals())
        {
            if (goal.equals("install"))
            {
                return true;
            }
        }
        return false;
    }
}

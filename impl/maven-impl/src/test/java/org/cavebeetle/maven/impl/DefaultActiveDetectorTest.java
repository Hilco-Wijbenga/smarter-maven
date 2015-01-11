/**
 *
 */
package org.cavebeetle.maven.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;
import org.apache.maven.execution.MavenSession;
import org.junit.Before;
import org.junit.Test;
import com.google.common.collect.Lists;

/**
 * The unit tests for {@code DefaultActiveDetector}.
 */
public class DefaultActiveDetectorTest
{
    private DefaultActiveDetector activeDetector;
    private MavenSession mockMavenSession;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        activeDetector = new DefaultActiveDetector();
        mockMavenSession = mock(MavenSession.class);
    }

    /**
     * Tests that a {@code null} {@code MavenSession} is handled correctly.
     */
    @Test
    public final void a_null_MavenSession_is_handled_correctly()
    {
        try
        {
            activeDetector.isActive(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'session'.", e.getMessage());
        }
    }

    /**
     * Tests that with goals {"clean"} Smarter Maven is not active.
     */
    @Test
    public final void with_goals_clean_Smarter_Maven_is_not_active()
    {
        final List<String> goals = Lists.newArrayList("clean");
        when(mockMavenSession.getGoals()).thenReturn(goals);
        assertFalse(activeDetector.isActive(mockMavenSession));
    }

    /**
     * Tests that with goals {"clean", "install"} Smarter Maven is active.
     */
    @Test
    public final void with_goals_clean_install_Smarter_Maven_is_active()
    {
        final List<String> goals = Lists.newArrayList("clean", "install");
        when(mockMavenSession.getGoals()).thenReturn(goals);
        assertTrue(activeDetector.isActive(mockMavenSession));
    }

    /**
     * Tests that with goals {"install"} Smarter Maven is active.
     */
    @Test
    public final void with_goals_install_Smarter_Maven_is_active()
    {
        final List<String> goals = Lists.newArrayList("install");
        when(mockMavenSession.getGoals()).thenReturn(goals);
        assertTrue(activeDetector.isActive(mockMavenSession));
    }
}

/**
 *
 */
package org.cavebeetle.maven.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import java.util.Properties;
import org.apache.maven.execution.MavenSession;
import org.cavebeetle.maven.ActiveDetector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
            activeDetector.isSmarterMavenActive(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'session'.", e.getMessage());
        }
    }

    @SuppressWarnings("boxing")
    @Test
    public final void isSmarterMaven_is_true_depending_on_the_SMARTER_MAVEN_ACTIVE_PROPERTY()
    {
        final Properties mockProperties = Mockito.mock(Properties.class);
        Mockito.when(mockMavenSession.getUserProperties()).thenReturn(mockProperties);
        Mockito.when(mockProperties.containsKey(ActiveDetector.SMARTER_MAVEN_ACTIVE_PROPERTY)).thenReturn(true);
        assertTrue(activeDetector.isSmarterMavenActive(mockMavenSession));
        Mockito.when(mockProperties.containsKey(ActiveDetector.SMARTER_MAVEN_ACTIVE_PROPERTY)).thenReturn(false);
        assertFalse(activeDetector.isSmarterMavenActive(mockMavenSession));
    }

    @SuppressWarnings("boxing")
    @Test
    public final void showProjectHierarchyWarnings_is_true_depending_on_the_SHOW_PROJECT_HIERARCHY_WARNINGS_PROPERTY()
    {
        final Properties mockProperties = Mockito.mock(Properties.class);
        Mockito.when(mockMavenSession.getUserProperties()).thenReturn(mockProperties);
        Mockito.when(mockProperties.containsKey(ActiveDetector.SHOW_PROJECT_HIERARCHY_WARNINGS_PROPERTY)).thenReturn(true);
        assertTrue(activeDetector.showProjectHierarchyWarnings(mockMavenSession));
        Mockito.when(mockProperties.containsKey(ActiveDetector.SHOW_PROJECT_HIERARCHY_WARNINGS_PROPERTY)).thenReturn(false);
        assertFalse(activeDetector.showProjectHierarchyWarnings(mockMavenSession));
    }
}

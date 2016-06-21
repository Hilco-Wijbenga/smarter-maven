package org.cavebeetle.maven.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.endsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Random;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.cavebeetle.maven.MavenVersion;
import org.codehaus.plexus.logging.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code DefaultAfterSessionStart}.
 */
public final class DefaultAfterSessionStartTest
{
    private MavenVersion mockSmarterMavenVersion;
    private Logger mockLogger;
    private RuntimeInformation mockRuntimeInformation;
    private MavenSession mockMavenSession;
    private DefaultAfterSessionStart afterSessionStart;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        mockSmarterMavenVersion = mock(MavenVersion.class);
        mockLogger = mock(Logger.class);
        mockRuntimeInformation = mock(RuntimeInformation.class);
        mockMavenSession = mock(MavenSession.class);
        afterSessionStart = new DefaultAfterSessionStart();
    }

    /**
     * Tests that a missing {@code MavenVersion} in {@code AfterSessionStart#afterSessionStart} is handled correctly.
     */
    @Test
    public final void a_missing_MavenVersion_in_AfterSessionStart_afterSessionStart_is_handled_correctly()
    {
        try
        {
            afterSessionStart.afterSessionStart(null, mockLogger, mockRuntimeInformation, mockMavenSession);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'smarterMavenVersion'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code Logger} in {@code AfterSessionStart#afterSessionStart} is handled correctly.
     */
    @Test
    public final void a_missing_Logger_in_AfterSessionStart_afterSessionStart_is_handled_correctly()
    {
        try
        {
            afterSessionStart.afterSessionStart(mockSmarterMavenVersion, null, mockRuntimeInformation, mockMavenSession);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'logger'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code RuntimeInformation}. in {@code AfterSessionStart#afterSessionStart} is handled
     * correctly.
     */
    @Test
    public final void a_missing_runtimeInformation_in_AfterSessionStart_afterSessionStart_is_handled_correctly()
    {
        try
        {
            afterSessionStart.afterSessionStart(mockSmarterMavenVersion, mockLogger, null, mockMavenSession);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'runtimeInformation'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code MavenSession} in {@code AfterSessionStart#afterSessionStart} is handled correctly.
     */
    @Test
    public final void a_missing_MavenSession_in_AfterSessionStart_afterSessionStart_is_handled_correctly()
    {
        try
        {
            afterSessionStart.afterSessionStart(mockSmarterMavenVersion, mockLogger, mockRuntimeInformation, null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'mavenSession'.", e.getMessage());
        }
    }

    /**
     * Tests that the right text is logged.
     */
    @Test
    public final void the_right_text_is_logged()
    {
        final Random rnd = new Random();
        final String smarterMavenVersionAsText = createRandomVersion(rnd);
        when(mockSmarterMavenVersion.toString()).thenReturn(smarterMavenVersionAsText);
        final String mavenVersionAsText = createRandomVersion(rnd);
        when(mockRuntimeInformation.getMavenVersion()).thenReturn(mavenVersionAsText);
        afterSessionStart.afterSessionStart(mockSmarterMavenVersion, mockLogger, mockRuntimeInformation, mockMavenSession);
        verify(mockLogger).info(endsWith(" Maven " + mavenVersionAsText));
        verify(mockLogger).info(endsWith(" Smarter Maven " + smarterMavenVersionAsText));
        verify(mockLogger).info(endsWith(" JDK " + System.getProperty("java.version")));
    }

    private String createRandomVersion(final Random rnd)
    {
        final int major = 1 + rnd.nextInt(9);
        final int minor = rnd.nextInt(10);
        final int micro = rnd.nextInt(10);
        return major + "." + minor + "." + micro;
    }
}

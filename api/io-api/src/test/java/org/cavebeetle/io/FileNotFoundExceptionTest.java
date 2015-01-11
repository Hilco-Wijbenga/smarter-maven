package org.cavebeetle.io;

import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code FileNotFoundException}.
 */
public final class FileNotFoundExceptionTest
{
    private String message = "message";
    private Throwable cause = new Throwable();

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        message = "message";
        cause = new Throwable();
    }

    /**
     * Tests that just providing a message works.
     */
    @Test
    public final void just_providing_a_message_works()
    {
        final FileNotFoundException fileNotFoundException = new FileNotFoundException(message);
        assertSame(message, fileNotFoundException.getMessage());
    }

    /**
     * Tests that just providing a cause works.
     */
    @Test
    public final void just_providing_a_cause_works()
    {
        final FileNotFoundException fileNotFoundException = new FileNotFoundException(cause);
        assertSame(cause, fileNotFoundException.getCause());
    }

    /**
     * Tests that providing both a message and a cause works.
     */
    @Test
    public final void providing_both_a_message_and_a_cause_works()
    {
        final FileNotFoundException fileNotFoundException = new FileNotFoundException(message, cause);
        assertSame(message, fileNotFoundException.getMessage());
        assertSame(cause, fileNotFoundException.getCause());
    }
}

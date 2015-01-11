package org.cavebeetle.io;

import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code IoException}.
 */
public final class IoExceptionTest
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
     * Test that just providing a message works.
     */
    @Test
    public final void just_providing_a_message_works()
    {
        final IoException ioException = new IoException(message);
        assertSame(message, ioException.getMessage());
    }

    /**
     * Tests that just providing a cause works.
     */
    @Test
    public final void just_providing_a_cause_works()
    {
        final IoException ioException = new IoException(cause);
        assertSame(cause, ioException.getCause());
    }

    /**
     * Tests that providing both a message and a cause works.
     */
    @Test
    public final void providing_both_a_message_and_a_cause_works()
    {
        final IoException ioException = new IoException(message, cause);
        assertSame(message, ioException.getMessage());
        assertSame(cause, ioException.getCause());
    }
}

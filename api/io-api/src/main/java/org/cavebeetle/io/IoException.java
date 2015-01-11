package org.cavebeetle.io;

/**
 * The root exception for {@code org.cavebeetle.io}.
 */
public class IoException
        extends
            RuntimeException
{
    private static final long serialVersionUID = 8396781325916098553L;

    /**
     * Creates a new {@code IoException}.
     *
     * @param message
     *            the message explaining the reason for this exception.
     */
    public IoException(
            final String message)
    {
        super(message);
    }

    /**
     * Creates a new {@code IoException}.
     *
     * @param cause
     *            the underlying cause of this exception.
     */
    public IoException(
            final Throwable cause)
    {
        super(cause);
    }

    /**
     * Creates a new {@code IoException}.
     *
     * @param message
     *            the message explaining the reason for this exception.
     * @param cause
     *            the underlying cause of this exception.
     */
    public IoException(
            final String message,
            final Throwable cause)
    {
        super(message, cause);
    }
}

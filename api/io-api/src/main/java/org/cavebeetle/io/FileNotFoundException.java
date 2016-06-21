package org.cavebeetle.io;

/**
 * A {@code FileNotFoundException} is thrown when a file cannot be found.
 */
public final class FileNotFoundException
        extends
            IoException
{
    private static final long serialVersionUID = -6817074737074667523L;

    /**
     * Creates a new {@code FileNotFoundException}.
     *
     * @param message
     *            the message explaining the reason for this exception.
     */
    public FileNotFoundException(final String message)
    {
        super(message);
    }

    /**
     * Creates a new {@code FileNotFoundException}.
     *
     * @param cause
     *            the underlying cause of this exception.
     */
    public FileNotFoundException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Creates a new {@code FileNotFoundException}.
     *
     * @param message
     *            the message explaining the reason for this exception.
     * @param cause
     *            the underlying cause of this exception.
     */
    public FileNotFoundException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}

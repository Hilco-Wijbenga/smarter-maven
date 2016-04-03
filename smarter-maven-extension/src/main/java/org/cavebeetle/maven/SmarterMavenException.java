package org.cavebeetle.maven;

public final class SmarterMavenException
        extends
            IllegalStateException
{
    private static final long serialVersionUID = 3926319105551051031L;

    public SmarterMavenException(final String message)
    {
        super(message);
    }

    public SmarterMavenException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}

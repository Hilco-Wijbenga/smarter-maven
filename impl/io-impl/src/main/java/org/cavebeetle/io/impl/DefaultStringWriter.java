package org.cavebeetle.io.impl;

import javax.inject.Singleton;
import org.cavebeetle.io.StringWriter;

/**
 * The implementation of {@code StringWriter}.
 */
public final class DefaultStringWriter
        implements
            StringWriter
{
    /**
     * The implementation of {@code StringWriter.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        @Override
        public StringWriter newWriter()
        {
            return new DefaultStringWriter();
        }
    }

    private final StringBuilder builder = new StringBuilder();

    @Override
    public void close()
    {
        builder.trimToSize();
    }

    @Override
    public String toString()
    {
        return builder.toString();
    }

    @Override
    public void write(
            final int codePoint)
    {
        builder.appendCodePoint(codePoint);
    }

    @Override
    public void write(
            final String s)
    {
        builder.append(s);
    }
}

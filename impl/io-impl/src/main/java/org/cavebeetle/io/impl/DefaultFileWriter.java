package org.cavebeetle.io.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Character.charCount;
import static java.lang.Character.toChars;
import static org.cavebeetle.io.InternalApi.END_OF_LINE;
import java.io.File;
import java.io.IOException;
import javax.inject.Singleton;
import org.cavebeetle.io.FileWriter;
import org.cavebeetle.io.IoException;

/**
 * The implementation of {@code FileWriter}.
 */
public final class DefaultFileWriter
        implements
            FileWriter
{
    /**
     * The implementation of {@code FileWriter.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        @Override
        public FileWriter newWriter(
                final File file)
        {
            return new DefaultFileWriter(file);
        }
    }

    private final java.io.FileWriter delegate;

    /**
     * Creates a new {@code DefaultFileWriter}.
     *
     * @param file
     *            the file to write to.
     */
    public DefaultFileWriter(
            final File file)
    {
        checkNotNull(file, "Missing 'file'.");
        try
        {
            delegate = new java.io.FileWriter(file);
        }
        catch (final IOException e)
        {
            throw new IoException(e);
        }
    }

    /**
     * Creates a new {@code DefaultFileWriter} using the given delegate. Used for testing.
     *
     * @param delegate
     *            the {@code java.io.FileWriter} instance.
     */
    public DefaultFileWriter(
            final java.io.FileWriter delegate)
    {
        this.delegate = delegate;
    }

    @Override
    public void close()
    {
        try
        {
            delegate.close();
        }
        catch (final IOException e)
        {
            throw new IoException(e);
        }
    }

    @Override
    public void write(
            final int codePoint)
    {
        try
        {
            if (charCount(codePoint) == 1)
            {
                delegate.write(codePoint);
            }
            else
            {
                final char[] codePointChars = toChars(codePoint);
                delegate.write(codePointChars[0]);
                delegate.write(codePointChars[1]);
            }
        }
        catch (final IOException e)
        {
            throw new IoException(e);
        }
    }

    @Override
    public void write(
            final String text)
    {
        checkNotNull(text, "Missing 'text'.");
        try
        {
            delegate.write(text);
        }
        catch (final IOException e)
        {
            throw new IoException(e);
        }
    }

    @Override
    public void writeLine(
            final String text)
    {
        write(text + END_OF_LINE);
    }
}

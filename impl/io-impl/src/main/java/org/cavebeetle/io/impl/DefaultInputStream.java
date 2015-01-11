package org.cavebeetle.io.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import javax.inject.Singleton;
import org.cavebeetle.io.FileNotFoundException;
import org.cavebeetle.io.InputStream;
import org.cavebeetle.io.IoException;

/**
 * The implementation of {@code InputStream}.
 */
public final class DefaultInputStream
        implements
            InputStream
{
    /**
     * The implementation of {@code InputStream.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        @Override
        public InputStream newInputStream(
                final File file)
        {
            return new DefaultInputStream(file);
        }

        @Override
        public InputStream newInputStream(
                final String text)
        {
            return new DefaultInputStream(text);
        }
    }

    private java.io.InputStream delegate;

    /**
     * Creates a new {@code DefaultInputStream}.
     *
     * @param file
     *            the file to read from.
     */
    public DefaultInputStream(
            final File file)
    {
        try
        {
            delegate = new java.io.FileInputStream(file);
        }
        catch (final java.io.FileNotFoundException e)
        {
            throw new FileNotFoundException(e.getMessage(), e);
        }
    }

    /**
     * Creates a new {@code DefaultInputStream}.
     *
     * @param text
     *            the text to read from.
     */
    public DefaultInputStream(
            final String text)
    {
        final byte[] bytes = text.getBytes();
        delegate = new ByteArrayInputStream(bytes);
    }

    @Override
    public void close()
    {
        try
        {
            delegate.close();
        }
        catch (final java.io.IOException e)
        {
            throw new IoException(e.getMessage(), e);
        }
    }

    @Override
    public int read(
            final byte[] buffer)
    {
        try
        {
            return delegate.read(buffer);
        }
        catch (final java.io.IOException e)
        {
            throw new IoException(e.getMessage(), e);
        }
    }
}

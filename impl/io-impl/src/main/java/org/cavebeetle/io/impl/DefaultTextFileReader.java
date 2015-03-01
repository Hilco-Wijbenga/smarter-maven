package org.cavebeetle.io.impl;

import static com.google.common.collect.Lists.newArrayList;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.inject.Singleton;
import org.cavebeetle.io.FileNotFoundException;
import org.cavebeetle.io.IoException;
import org.cavebeetle.io.TextFileReader;

/**
 * The implementation of {@code TextFileReader}.
 */
public final class DefaultTextFileReader
        implements
            TextFileReader
{
    /**
     * The implementation of {@code Reader.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        @Override
        public TextFileReader newTextFileReader(
                final File file)
        {
            return new DefaultTextFileReader(file);
        }

        @Override
        public TextFileReader newTextFileReader(
                final String text)
        {
            return new DefaultTextFileReader(text);
        }
    }

    private java.io.LineNumberReader delegate;

    /**
     * Creates a new {@code DefaultTextFileReader}.
     *
     * @param file
     *            the file to read from.
     */
    public DefaultTextFileReader(
            final File file)
    {
        try
        {
            final java.io.FileReader fileReader = new java.io.FileReader(file);
            delegate = new java.io.LineNumberReader(fileReader);
        }
        catch (final java.io.FileNotFoundException e)
        {
            throw new FileNotFoundException(e.getMessage(), e);
        }
    }

    /**
     * Creates a new {@code DefaultTextFileReader}.
     *
     * @param text
     *            the text to read from.
     */
    public DefaultTextFileReader(
            final String text)
    {
        final java.io.StringReader stringReader = new java.io.StringReader(text);
        delegate = new java.io.LineNumberReader(stringReader);
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
    public String readLine()
    {
        try
        {
            return delegate.readLine();
        }
        catch (final java.io.IOException e)
        {
            throw new IoException(e.getMessage(), e);
        }
    }

    @Override
    public Iterator<String> iterator()
    {
        final List<String> lines = newArrayList();
        while (true)
        {
            final String nextLine = readLine();
            if (nextLine == null)
            {
                break;
            }
            lines.add(nextLine);
        }
        return lines.iterator();
    }
}

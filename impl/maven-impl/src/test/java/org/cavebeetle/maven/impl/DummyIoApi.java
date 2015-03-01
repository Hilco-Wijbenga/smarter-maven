package org.cavebeetle.maven.impl;

import java.io.File;
import org.cavebeetle.io.InputStream;
import org.cavebeetle.io.IoApi;
import org.cavebeetle.io.SourceFiles;
import org.cavebeetle.io.TextFile;
import org.cavebeetle.io.TextFileReader;
import org.cavebeetle.io.Writer;

/**
 * A dummy {@code IoApi} for testing.
 */
public final class DummyIoApi
        implements
            IoApi
{
    @Override
    public InputStream newInputStream(
            final File file)
    {
        return null;
    }

    @Override
    public InputStream newInputStream(
            final String text)
    {
        return null;
    }

    @Override
    public SourceFiles newSourceFiles(
            final File baseDir)
    {
        return null;
    }

    @Override
    public Writer newWriter()
    {
        return null;
    }

    @Override
    public Writer newWriter(
            final File file)
    {
        return null;
    }

    @Override
    public TextFileReader newTextFileReader(
            final File file)
    {
        return null;
    }

    @Override
    public TextFileReader newTextFileReader(
            final String text)
    {
        return null;
    }

    @Override
    public TextFile newTextFile(
            final File file)
    {
        return null;
    }
}

package org.cavebeetle.io.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.File;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cavebeetle.io.FileWriter;
import org.cavebeetle.io.InputStream;
import org.cavebeetle.io.InternalApi;
import org.cavebeetle.io.SourceFiles;
import org.cavebeetle.io.StringWriter;
import org.cavebeetle.io.TextFile;
import org.cavebeetle.io.TextFileReader;
import com.google.inject.Injector;

/**
 * The implementation of {@code InternalApi}.
 */
@Singleton
public final class DefaultInternalApi
        implements
            InternalApi
{
    private final Injector injector;

    /**
     * Creates a new {@code DefaultInternalApi}.
     *
     * @param injector
     *            the Guice {@code Injector} instance.
     */
    @Inject
    public DefaultInternalApi(
            final Injector injector)
    {
        checkNotNull(injector, "Missing 'injector'.");
        this.injector = injector;
    }

    @Override
    public InputStream newInputStream(
            final File file)
    {
        return injector.getInstance(InputStream.Builder.class).newInputStream(file);
    }

    @Override
    public InputStream newInputStream(
            final String text)
    {
        return injector.getInstance(InputStream.Builder.class).newInputStream(text);
    }

    @Override
    public SourceFiles newSourceFiles(
            final File baseDir)
    {
        return injector.getInstance(SourceFiles.Builder.class).newSourceFiles(baseDir);
    }

    @Override
    public SourceFiles newSourceFilesForProjectWithoutDirectory(
            final File pomFile)
    {
        return injector.getInstance(SourceFiles.Builder.class).newSourceFilesForProjectWithoutDirectory(pomFile);
    }

    @Override
    public StringWriter newWriter()
    {
        return injector.getInstance(StringWriter.Builder.class).newWriter();
    }

    @Override
    public FileWriter newWriter(
            final File file)
    {
        return injector.getInstance(FileWriter.Builder.class).newWriter(file);
    }

    @Override
    public TextFileReader newTextFileReader(
            final File file)
    {
        return injector.getInstance(TextFileReader.Builder.class).newTextFileReader(file);
    }

    @Override
    public TextFileReader newTextFileReader(
            final String text)
    {
        return injector.getInstance(TextFileReader.Builder.class).newTextFileReader(text);
    }

    @Override
    public TextFile newTextFile(
            final File file)
    {
        return injector.getInstance(TextFile.Builder.class).newTextFile(file);
    }
}

package org.cavebeetle.io.impl;

import java.io.File;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cavebeetle.io.FileWriter;
import org.cavebeetle.io.InputStream;
import org.cavebeetle.io.InternalApi;
import org.cavebeetle.io.SourceFiles;
import org.cavebeetle.io.StringWriter;
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
}

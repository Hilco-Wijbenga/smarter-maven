package org.cavebeetle.io.impl;

import org.cavebeetle.io.FileWriter;
import org.cavebeetle.io.InputStream;
import org.cavebeetle.io.InternalApi;
import org.cavebeetle.io.IoApi;
import org.cavebeetle.io.SourceFiles;
import org.cavebeetle.io.StringWriter;
import org.cavebeetle.io.TextFile;
import org.cavebeetle.io.TextFileReader;
import com.google.inject.AbstractModule;

/**
 * The Guice module for {@code org.cavebeetle.io}.
 */
public final class IoGuiceModule
        extends
            AbstractModule
{
    @Override
    public void configure()
    {
        bind(FileWriter.Builder.class).to(DefaultFileWriter.DefaultBuilder.class);
        bind(InputStream.Builder.class).to(DefaultInputStream.DefaultBuilder.class);
        bind(InternalApi.class).to(DefaultInternalApi.class);
        bind(IoApi.class).to(DefaultInternalApi.class);
        bind(SourceFiles.Builder.class).to(DefaultSourceFiles.DefaultBuilder.class);
        bind(StringWriter.Builder.class).to(DefaultStringWriter.DefaultBuilder.class);
        bind(TextFile.Builder.class).to(DefaultTextFile.DefaultBuilder.class);
        bind(TextFileReader.Builder.class).to(DefaultTextFileReader.DefaultBuilder.class);
    }
}

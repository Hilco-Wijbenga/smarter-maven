package org.cavebeetle.io.impl;

import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.newArrayList;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cavebeetle.io.IoApi;
import org.cavebeetle.io.TextFile;
import org.cavebeetle.io.TextFileReader;

/**
 * The implementation of {@code TextFile}.
 */
public final class DefaultTextFile
        implements
            TextFile
{
    /**
     * The implementation of {@code TextFile.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        private final IoApi ioApi;

        /**
         * Creates a new {@code DefaultBuilder}.
         *
         * @param ioApi
         *            the {@code IoApi} instance.
         */
        @Inject
        public DefaultBuilder(
                final IoApi ioApi)
        {
            this.ioApi = ioApi;
        }

        @Override
        public TextFile newTextFile(
                final File file)
        {
            return new DefaultTextFile(ioApi, file);
        }
    }

    private final List<String> lines;

    /**
     * Creates a new {@code DefaultTextFile}.
     *
     * @param ioApi
     *            the {@code IoApi} instance.
     * @param textFile
     *            the text file represented by this {@code TextFile}.
     */
    public DefaultTextFile(
            final IoApi ioApi,
            final File textFile)
    {
        final TextFileReader textFileReader = ioApi.newTextFileReader(textFile);
        final List<String> lines_ = newArrayList();
        for (final String line : textFileReader)
        {
            lines_.add(line);
        }
        lines = copyOf(lines_);
    }

    @Override
    public Iterator<String> iterator()
    {
        return lines.iterator();
    }
}

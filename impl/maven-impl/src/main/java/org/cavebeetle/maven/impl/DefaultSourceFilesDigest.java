package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.ImmutableList.copyOf;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.sort;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cavebeetle.io.IoApi;
import org.cavebeetle.io.TextFile;
import org.cavebeetle.io.Writer;
import org.cavebeetle.maven.Digest;
import org.cavebeetle.maven.FileHashGenerator;
import org.cavebeetle.maven.SourceFilesDigest;
import com.google.common.collect.Lists;

/**
 * The implementation of {@code SourceFilesDigest}.
 */
public final class DefaultSourceFilesDigest
        implements
            SourceFilesDigest
{
    /**
     * The implementation of {@code SourceFilesDigest.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        private final IoApi ioApi;
        private final FileHashGenerator fileHashGenerator;

        /**
         * Creates a new {@code DefaultBuilder}.
         *
         * @param ioApi
         *            the {@code IoApi} instance.
         * @param fileHashGenerator
         *            the {@code FileHashGenerator} instance.
         */
        @Inject
        public DefaultBuilder(final IoApi ioApi, final FileHashGenerator fileHashGenerator)
        {
            this.ioApi = ioApi;
            this.fileHashGenerator = fileHashGenerator;
        }

        @Override
        public SourceFilesDigest newSourceFilesDigest(final List<String> sourceFileLines)
        {
            return new DefaultSourceFilesDigest(fileHashGenerator, sourceFileLines);
        }

        @Override
        public SourceFilesDigest newSourceFilesDigest(final File file)
        {
            return new DefaultSourceFilesDigest(ioApi, fileHashGenerator, file);
        }
    }

    private final List<String> sourceFileLines;
    private final Digest digest;

    /**
     * Creates a new {@code DefaultSourceFilesDigest}.
     *
     * @param fileHashGenerator
     *            the {@code FileHashGenerator} instance.
     * @param sourceFileLines
     *            the list of source files.
     */
    public DefaultSourceFilesDigest(final FileHashGenerator fileHashGenerator, final List<String> sourceFileLines)
    {
        checkNotNull(fileHashGenerator, "Missing 'fileHashGenerator'.");
        checkNotNull(sourceFileLines, "Missing 'sourceFileLines'.");
        sort(sourceFileLines);
        this.sourceFileLines = copyOf(sourceFileLines);
        digest = fileHashGenerator.generate(this);
    }

    /**
     * Creates a new {@code DefaultSourceFilesDigest}.
     *
     * @param ioApi
     *            the {@code IoApi} instance.
     * @param fileHashGenerator
     *            the {@code FileHashGenerator} instance.
     * @param file
     *            the file with the list of source files and dependencies.
     */
    public DefaultSourceFilesDigest(final IoApi ioApi, final FileHashGenerator fileHashGenerator, final File file)
    {
        checkNotNull(ioApi, "Missing 'ioApi'.");
        checkNotNull(fileHashGenerator, "Missing 'fileHashGenerator'.");
        checkNotNull(file, "Missing 'file'.");
        final TextFile textFile = ioApi.newTextFile(file);
        final List<String> sourceFileLines_ = newArrayList();
        for (final String line : textFile)
        {
            sourceFileLines_.add(line);
        }
        sort(sourceFileLines_);
        sourceFileLines = copyOf(sourceFileLines_);
        digest = fileHashGenerator.generate(this);
    }

    @Override
    public Digest getDigest()
    {
        return digest;
    }

    @Override
    public void write(final Writer writer)
    {
        for (final String digestLine : this)
        {
            writer.writeLine(digestLine);
        }
    }

    @Override
    public Iterator<String> iterator()
    {
        final List<String> lines = Lists.newArrayList();
        lines.addAll(sourceFileLines);
        return lines.iterator();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + digest.hashCode();
        result = prime * result + sourceFileLines.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final DefaultSourceFilesDigest other = (DefaultSourceFilesDigest) object;
        return digest.equals(other.digest) && sourceFileLines.equals(other.sourceFileLines);
    }
}

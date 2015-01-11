package org.cavebeetle.maven.impl;

import static com.google.common.collect.Maps.newHashMap;
import static java.lang.System.getProperty;
import java.io.File;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cavebeetle.io.InputStream;
import org.cavebeetle.io.IoApi;
import org.cavebeetle.io.SourceFiles;
import org.cavebeetle.io.Writer;
import org.cavebeetle.maven.Digest;
import org.cavebeetle.maven.FileHashGenerator;
import org.cavebeetle.maven.Gav;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.Project;
import org.cavebeetle.maven.SourceFilesHashGenerator;

/**
 * The implementation of {@code SourceFilesHashGenerator}.
 */
@Singleton
public final class DefaultSourceFilesHashGenerator
        implements
            SourceFilesHashGenerator
{
    /**
     * Adds a line listing both a dependency's digest and its GAV (group id, artifact id, and version).
     *
     * @param writer
     *            the {@code Writer} instance.
     * @param dependencyGav
     *            the dependency's GAV.
     * @param dependencyHash
     *            the dependency's cryptographic hash (as a hexadecimal number).
     */
    public static void addDigestLine(
            final Writer writer,
            final Gav dependencyGav,
            final Digest dependencyHash)
    {
        final StringBuilder digestLine = new StringBuilder();
        digestLine.append(dependencyHash).append(':').append(dependencyGav);
        writer.write(digestLine.toString());
        writer.write(END_OF_LINE);
    }

    private static final String END_OF_LINE = getProperty("line.separator");
    private final IoApi ioApi;
    private final FileHashGenerator fileHashGenerator;
    private final Map<Project, Digest> hashCache = newHashMap();

    /**
     * Creates a new {@code SourceFilesHashGenerator}.
     *
     * @param ioApi
     *            the {@code IoApi} instance.
     * @param internalApi
     *            the {@code InternalApi} instance.
     */
    @Inject
    public DefaultSourceFilesHashGenerator(
            final IoApi ioApi,
            final InternalApi internalApi)
    {
        this.ioApi = ioApi;
        fileHashGenerator = internalApi.getFileHashGenerator();
    }

    @Override
    public Digest generate(
            final Project project)
    {
        if (!hashCache.containsKey(project))
        {
            final Writer writer = ioApi.newWriter();
            writeSourceFilesDigest(project, writer);
            final String sourceFilesDigestListingAsText = writer.toString();
            final InputStream inputStream = ioApi.newInputStream(sourceFilesDigestListingAsText);
            try
            {
                final Digest digest = fileHashGenerator.generate(inputStream);
                hashCache.put(project, digest);
            }
            finally
            {
                inputStream.close();
            }
        }
        return hashCache.get(project);
    }

    @Override
    public void generate(
            final Project project,
            final File targetDir)
    {
        targetDir.mkdirs();
        final File projectHashFile = new File(targetDir, SOURCE_FILES_LISTING);
        final Writer writer = ioApi.newWriter(projectHashFile);
        writeSourceFilesDigest(project, writer);
    }

    private void addDigestLine(
            final Writer writer,
            final int ignoreLength,
            final File file)
    {
        final StringBuilder digestLine = new StringBuilder();
        digestLine.append(fileHashGenerator.generate(file)).append(':').append(file.getPath().substring(ignoreLength));
        writer.write(digestLine.toString());
        writer.write(END_OF_LINE);
    }

    private void writeSourceFilesDigest(
            final Project project,
            final Writer writer)
    {
        try
        {
            final File baseDir = project.getBaseDir();
            final int ignoreLength = baseDir.getPath().length() + 1;
            final SourceFiles sourceFiles = ioApi.newSourceFiles(baseDir);
            for (final File file : sourceFiles)
            {
                addDigestLine(writer, ignoreLength, file);
            }
            for (final Project dependency : project.getDependencies())
            {
                final Gav dependencyGav = dependency.getGav();
                final Digest dependencyHash = generate(dependency);
                addDigestLine(writer, dependencyGav, dependencyHash);
            }
        }
        finally
        {
            writer.close();
        }
    }
}

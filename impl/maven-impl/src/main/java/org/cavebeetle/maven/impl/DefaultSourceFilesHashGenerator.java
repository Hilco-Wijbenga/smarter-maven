package org.cavebeetle.maven.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cavebeetle.io.IoApi;
import org.cavebeetle.io.SourceFiles;
import org.cavebeetle.io.Writer;
import org.cavebeetle.maven.Digest;
import org.cavebeetle.maven.FileHashGenerator;
import org.cavebeetle.maven.Gav;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.Project;
import org.cavebeetle.maven.SourceFilesDigest;
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
     * Creates a line listing both a dependency's digest and its GAV (group id, artifact id, and version).
     *
     * @param dependencyGav
     *            the dependency's GAV.
     * @param dependencyDigest
     *            the dependency's cryptographic digest/hash (as a hexadecimal number).
     * @return the created digest line.
     */
    public static String createDigestLine(
            final Gav dependencyGav,
            final Digest dependencyDigest)
    {
        return dependencyDigest + ":" + dependencyGav;
    }

    private final IoApi ioApi;
    private final InternalApi internalApi;
    private final FileHashGenerator fileHashGenerator;
    private final Map<Project, SourceFilesDigest> hashCache = newHashMap();

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
        this.internalApi = internalApi;
        fileHashGenerator = internalApi.getFileHashGenerator();
    }

    @Override
    public SourceFilesDigest generateUsingCache(
            final Project project)
    {
        if (!hashCache.containsKey(project))
        {
            final Writer writer = ioApi.newWriter();
            final SourceFilesDigest sourceFilesDigest = createSourceFilesDigest(project);
            writeSourceFilesDigest(writer, sourceFilesDigest);
            hashCache.put(project, sourceFilesDigest);
        }
        return hashCache.get(project);
    }

    @Override
    public SourceFilesDigest generate(
            final Project project)
    {
        final File targetDir = project.getBuildDir();
        targetDir.mkdirs();
        final File projectHashFile = project.getSourceFilesFile();
        final Writer writer = ioApi.newWriter(projectHashFile);
        final SourceFilesDigest sourceFilesDigest = createSourceFilesDigest(project);
        writeSourceFilesDigest(writer, sourceFilesDigest);
        return sourceFilesDigest;
    }

    private String createDigestLine(
            final int ignoreLength,
            final File file)
    {
        return fileHashGenerator.generate(file) + ":" + file.getPath().substring(ignoreLength);
    }

    private SourceFilesDigest createSourceFilesDigest(
            final Project project)
    {
        final List<String> sourceFileLines = newArrayList();
        final File baseDir = project.getBaseDir();
        final int ignoreLength = baseDir.getPath().length() + 1;
        final SourceFiles sourceFiles = project.isProjectWithoutDirectory()
            ? ioApi.newSourceFilesForProjectWithoutDirectory(project.getMavenProject().getFile())
            : ioApi.newSourceFiles(baseDir);
        for (final File file : sourceFiles)
        {
            final String digestLine = createDigestLine(ignoreLength, file);
            sourceFileLines.add(digestLine);
        }
        return internalApi.newSourceFilesDigest(sourceFileLines);
    }

    private void writeSourceFilesDigest(
            final Writer writer,
            final SourceFilesDigest sourceFilesDigest)
    {
        try
        {
            sourceFilesDigest.write(writer);
        }
        finally
        {
            writer.close();
        }
    }
}

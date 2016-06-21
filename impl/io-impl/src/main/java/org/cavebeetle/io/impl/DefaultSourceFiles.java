package org.cavebeetle.io.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Singleton;
import org.cavebeetle.io.SourceFiles;
import com.google.common.collect.Lists;

/**
 * The implementation of {@code SourceFiles}.
 */
public final class DefaultSourceFiles
        implements
            SourceFiles
{
    /**
     * The implementation of {@code SourceFiles.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        @Override
        public SourceFiles newSourceFiles(
                final File baseDir)
        {
            return new DefaultSourceFiles(baseDir, true);
        }

        @Override
        public SourceFiles newSourceFilesForProjectWithoutDirectory(final File pomFile)
        {
            return new DefaultSourceFiles(pomFile, false);
        }
    }

    private final List<File> files;

    /**
     * Creates a new {@code DefaultSourceFiles}.
     *
     * @param file
     *            the base directory to search or the POM file add.
     * @param directory
     *            whether the given file is the base directory ({@code true}) or the POM file (@code false).
     */
    public DefaultSourceFiles(
            final File file,
            final boolean directory)
    {
        if (directory)
        {
            files = listFiles(file);
        }
        else
        {
            files = Lists.newArrayList(file);
        }
    }

    @Override
    public Iterator<File> iterator()
    {
        return files.iterator();
    }

    private List<File> listFiles(
            final File baseDir)
    {
        final List<File> filesFound = new ArrayList<File>();
        for (final File file : baseDir.listFiles())
        {
            if (file.isDirectory() && file.getName().equals("src"))
            {
                listFiles(baseDir, file.getName(), filesFound);
            }
            else if (file.isFile() && file.getName().equals("pom.xml"))
            {
                filesFound.add(file);
            }
        }
        return filesFound;
    }

    private void listFiles(
            final File baseDir,
            final String currentDir,
            final List<File> filesFound)
    {
        final File dir = new File(baseDir, currentDir);
        for (final File file : dir.listFiles())
        {
            if (file.isDirectory())
            {
                listFiles(baseDir, currentDir + File.separator + file.getName(), filesFound);
            }
            else if (file.isFile())
            {
                filesFound.add(file);
            }
        }
    }
}

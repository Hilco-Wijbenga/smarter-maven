package org.cavebeetle.io;

import java.io.File;

/**
 * A {@code SourceFiles} instance provides a collection of source files.
 */
public interface SourceFiles
        extends
            Iterable<File>
{
    /**
     * A factory for {@code SourceFiles} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code SourceFiles}.
         *
         * @param baseDir
         *            the root directory to search for source files.
         * @return a new {@code SourceFiles}.
         */
        SourceFiles newSourceFiles(
                File baseDir);

        /**
         * Creates a new {@code SourceFiles} for a project without a directory.
         *
         * @param pomFile
         *            the POM file to list as a source file.
         * @return a new {@code SourceFiles}.
         */
        SourceFiles newSourceFilesForProjectWithoutDirectory(
                File pomFile);
    }
}

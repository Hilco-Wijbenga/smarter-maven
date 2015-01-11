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
    }
}

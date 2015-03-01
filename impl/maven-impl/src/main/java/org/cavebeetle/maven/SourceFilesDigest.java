package org.cavebeetle.maven;

import java.io.File;
import java.util.List;
import org.cavebeetle.io.Writer;

/**
 * A {@code SourceFilesDigest} is a listing of all source files of a project.
 */
public interface SourceFilesDigest
        extends
            Iterable<String>
{
    /**
     * A factory of {@code SourceFilesDigest} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code SourceFilesDigest}.
         *
         * @param sourceFileLines
         *            the lines describing all the source files for the project.
         * @return a new {@code SourceFilesDigest}.
         */
        SourceFilesDigest newSourceFilesDigest(
                final List<String> sourceFileLines);

        /**
         * Creates a new {@code SourceFilesDigest}.
         *
         * @param file
         *            the file with the source files for the project.
         * @return a new {@code SourceFilesDigest}.
         */
        SourceFilesDigest newSourceFilesDigest(
                final File file);
    }

    /**
     * Gets the cryptographic digest for this {@code SourceFilesDigest}.
     *
     * @return the cryptographic digest for this {@code SourceFilesDigest}.
     */
    Digest getDigest();

    /**
     * Writes this {@code SourceFilesDigest}.
     *
     * @param writer
     *            the {@code Writer} instance.
     */
    void write(
            Writer writer);
}

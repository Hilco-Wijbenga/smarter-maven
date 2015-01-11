package org.cavebeetle.maven;

import java.io.File;

/**
 * A {@code SourceFilesHashGenerator} facilitates generating digests for a group of source files.
 */
public interface SourceFilesHashGenerator
{
    /**
     * The name of the file that stores the source file digests (in the "target" directory).
     */
    String SOURCE_FILES_LISTING = ".source-files";

    /**
     * Generates a {@code Digest} for the source files of the given {@code Project}.
     *
     * @param project
     *            the {@code Project} of interest.
     * @return a {@code Digest} for the source files of the given {@code Project}.
     */
    Digest generate(
            Project project);

    /**
     * Generates the source files listing for the given {@code Project} in the given directory.
     *
     * @param project
     *            the {@code Project} of interest.
     * @param targetDir
     *            the "target" directory where the source file listing should be stored.
     */
    void generate(
            Project project,
            File targetDir);
}

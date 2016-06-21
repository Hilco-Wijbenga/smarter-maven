package org.cavebeetle.maven;

/**
 * A {@code SourceFilesHashGenerator} facilitates generating digests for a group of source files.
 */
public interface SourceFilesHashGenerator
{
    /**
     * Generates a {@code Digest} for the source files of the given {@code Project}.
     *
     * @param project
     *            the {@code Project} of interest.
     * @return the source files digest.
     */
    SourceFilesDigest generateUsingCache(
            Project project);

    /**
     * Generates the source files listing for the given {@code Project} in the given directory.
     *
     * @param project
     *            the {@code Project} of interest.
     * @return the source files digest.
     */
    SourceFilesDigest generate(
            Project project);
}

package org.cavebeetle.maven;

import java.io.File;

/**
 * A {@code FileHashGenerator} facilitates generating digests.
 */
public interface FileHashGenerator
{
    /**
     * Generates a {@code Digest} for (the contents of) the given {@code File}.
     *
     * @param file
     *            the {@code File} to hash.
     * @return a {@code Digest} for (the contents of) the given {@code File}.
     */
    Digest generate(
            File file);

    /**
     * Generates a {@code Digest} for the given lines.
     *
     * @param lines
     *            the lines to hash.
     * @return a {@code Digest} for the given lines.
     */
    Digest generate(
            Iterable<String> lines);
}

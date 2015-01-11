package org.cavebeetle.maven;

import java.io.File;
import org.cavebeetle.io.InputStream;

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
     * Generates a {@code Digest} for (the contents of) the given {@code InputStream}.
     *
     * @param inputStream
     *            the {@code InputStream} to hash.
     * @return a {@code Digest} for (the contents of) the given {@code InputStream}.
     */
    Digest generate(
            InputStream inputStream);
}

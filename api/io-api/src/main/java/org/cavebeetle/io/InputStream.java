package org.cavebeetle.io;

import java.io.File;

/**
 * An {@code InputStream} represents a cleaned up version of {@code java.io.InputStream}.
 */
public interface InputStream
{
    /**
     * A factory for {@code InputStream} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code InputStream}.
         *
         * @param file
         *            the {@code File} to read from.
         * @return a new {@code InputStream}.
         */
        InputStream newInputStream(File file);

        /**
         * Creates a new {@code InputStream}.
         *
         * @param text
         *            the text to read from.
         * @return a new {@code InputStream}.
         */
        InputStream newInputStream(String text);
    }

    /**
     * Closes this {@code InputStream}.
     */
    void close();

    /**
     * Reads bytes into the provided buffer.
     *
     * @param buffer
     *            the buffer to fill.
     * @return the number of bytes read.
     */
    int read(byte[] buffer);
}

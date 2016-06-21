package org.cavebeetle.io;

import java.io.File;

/**
 * A {@code TextFileReader} represents a cleaned up version of {@code java.io.LineNumberReader}.
 */
public interface TextFileReader
        extends
            Iterable<String>
{
    /**
     * A factory for {@code TextFileReader} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code TextFileReader}.
         *
         * @param file
         *            the {@code File} to read from.
         * @return a new {@code TextFileReader}.
         */
        TextFileReader newTextFileReader(File file);

        /**
         * Creates a new {@code TextFileReader}.
         *
         * @param text
         *            the text to read from.
         * @return a new {@code TextFileReader}.
         */
        TextFileReader newTextFileReader(String text);
    }

    /**
     * Closes this {@code TextFileReader}.
     */
    void close();

    /**
     * Reads a single line of text.
     *
     * @return a single line of text.
     */
    String readLine();
}

package org.cavebeetle.io;

import java.io.File;

/**
 * A {@code Writer} is a cleaned up version of {@code java.io.Writer}.
 */
public interface Writer
{
    /**
     * A factory of {@code Writer} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code Writer}. It will write to a {@code StringBuilder}.
         *
         * @return a new {@code Writer}.
         */
        Writer newWriter();

        /**
         * Creates a new {@code Writer}. It will write to the given {@code File}.
         *
         * @param file
         *            the {@code File} to write to.
         * @return a new {@code Writer}.
         */
        Writer newWriter(
                File file);
    }

    /**
     * Closes this {@code Writer}.
     */
    void close();

    /**
     * Writes a single code point.
     *
     * @param codePoint
     *            the code point to write.
     */
    void write(
            int codePoint);

    /**
     * Writes the given text.
     *
     * @param text
     *            the text to write.
     */
    void write(
            String text);

    /**
     * Writes the given text and append an end-of-line. The type of end-of-line depends on the OS.
     *
     * @param text
     *            the text to write.
     */
    void writeLine(
            String text);
}

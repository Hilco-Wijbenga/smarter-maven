package org.cavebeetle.io;

import java.io.File;

/**
 * A {@code TextFile} represents a text file.
 */
public interface TextFile
        extends
            Iterable<String>
{
    /**
     * A factory for {@code TextFile} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code TextFile}.
         *
         * @param file
         *            the {@code File} to write to.
         * @return a new {@code FileWriter}.
         */
        TextFile newTextFile(
                File file);
    }
}

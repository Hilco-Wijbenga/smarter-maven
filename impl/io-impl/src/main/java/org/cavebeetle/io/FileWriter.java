package org.cavebeetle.io;

import java.io.File;

/**
 * A {@code FileWriter} writes to a {@code File}.
 */
public interface FileWriter
        extends
            Writer
{
    /**
     * A factory for {@code FileWriter} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code FileWriter}.
         *
         * @param file
         *            the {@code File} to write to.
         * @return a new {@code FileWriter}.
         */
        FileWriter newWriter(
                File file);
    }
}

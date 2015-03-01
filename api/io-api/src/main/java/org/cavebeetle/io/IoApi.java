package org.cavebeetle.io;

import static java.lang.System.getProperty;

/**
 * The public API to {@code org.cavebeetle.io}.
 */
public interface IoApi
        extends
            InputStream.Builder,
            TextFile.Builder,
            TextFileReader.Builder,
            SourceFiles.Builder,
            Writer.Builder
{
    /**
     * The OS dependent end-of-line.
     */
    String END_OF_LINE = getProperty("line.separator");
}

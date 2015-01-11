package org.cavebeetle.io;

/**
 * A {@code StringWriter} writes to a {@code StringBuilder}.
 */
public interface StringWriter
        extends
            Writer
{
    /**
     * A factory for {@code StringWriter} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code StringWriter}.
         *
         * @return a new {@code StringWriter}.
         */
        StringWriter newWriter();
    }
}

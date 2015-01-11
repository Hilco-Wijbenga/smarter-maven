package org.cavebeetle.maven;

/**
 * A project's version.
 */
public interface Version
{
    /**
     * The constructor API for {@code Version}.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code Version}.
         *
         * @param version
         *            the text representing the version.
         * @return a new {@code Version}.
         */
        Version newVersion(
                String version);
    }
}

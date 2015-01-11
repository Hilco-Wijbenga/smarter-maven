package org.cavebeetle.maven;

/**
 * The public API for {@code org.cavebeetle.maven}.
 */
public interface MavenApi
{
    /**
     * Gets a {@code MavenExtension} instance.
     *
     * @return a {@code MavenExtension} instance.
     */
    MavenExtension getMavenExtension();
}

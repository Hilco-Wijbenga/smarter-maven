package org.cavebeetle.maven;

/**
 * A project's artifact id.
 */
public interface ArtifactId
{
    /**
     * The constructor API for {@code ArtifactId}.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code ArtifactId}.
         *
         * @param artifactId
         *            the text representing the artifact id.
         * @return a new {@code ArtifactId}.
         */
        ArtifactId newArtifactId(String artifactId);
    }
}

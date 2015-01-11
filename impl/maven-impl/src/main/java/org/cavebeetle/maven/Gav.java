package org.cavebeetle.maven;

/**
 * A combination of a group id, an artifact id, and a version.
 */
public interface Gav
{
    /**
     * A factory of {@code Gav} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code Gav}.
         *
         * @param groupId
         *            the group id.
         * @param artifactId
         *            the artifact id.
         * @param version
         *            the version.
         * @return a new {@code Gav}.
         */
        Gav newGav(
                GroupId groupId,
                ArtifactId artifactId,
                Version version);
    }

    /**
     * Gets the group id.
     *
     * @return the group id.
     */
    GroupId getGroupId();

    /**
     * Gets the artifact id.
     *
     * @return the artifact id.
     */
    ArtifactId getArtifactId();

    /**
     * Gets the version.
     *
     * @return the version.
     */
    Version getVersion();

    /**
     * Returns whether this {@code Gav} represents a snapshot version.
     *
     * @return {@code true} if and only if this {@code Gav} represents a snapshot version.
     */
    boolean isSnapshot();
}

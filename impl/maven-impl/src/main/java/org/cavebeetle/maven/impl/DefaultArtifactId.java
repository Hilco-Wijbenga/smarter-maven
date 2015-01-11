package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkArgument;
import javax.inject.Singleton;
import org.cavebeetle.maven.ArtifactId;

/**
 * The implementation of {@code ArtifactId}.
 */
public final class DefaultArtifactId
        implements
            ArtifactId
{
    /**
     * The implementation of {@code ArtifactId.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        @Override
        public ArtifactId newArtifactId(
                final String artifactId)
        {
            return new DefaultArtifactId(artifactId);
        }
    }

    private final String artifactId;

    /**
     * Creates a new {@code DefaultArtifactId}.
     *
     * @param artifactId
     *            the text representing the artifact id.
     */
    public DefaultArtifactId(
            final String artifactId)
    {
        checkArgument(artifactId != null && !artifactId.isEmpty(), "Missing 'artifactId'.");
        this.artifactId = artifactId;
    }

    @Override
    public int hashCode()
    {
        final int prime = 37;
        final int result = prime + artifactId.hashCode();
        return result;
    }

    @Override
    public boolean equals(
            final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final DefaultArtifactId other = (DefaultArtifactId) object;
        return artifactId.equals(other.artifactId);
    }

    @Override
    public String toString()
    {
        return artifactId;
    }
}

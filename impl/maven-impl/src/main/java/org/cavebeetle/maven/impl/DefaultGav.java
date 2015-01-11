package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cavebeetle.maven.ArtifactId;
import org.cavebeetle.maven.Gav;
import org.cavebeetle.maven.GroupId;
import org.cavebeetle.maven.SnapshotDetector;
import org.cavebeetle.maven.Version;

/**
 * The implementation of {@code Gav}.
 */
public final class DefaultGav
        implements
            Gav
{
    /**
     * The implementation of {@code Gav.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        private final SnapshotDetector snapshotDetector;

        /**
         * Creates a new {@code DefaultBuilder}.
         *
         * @param snapshotDetector
         *            the {@code SnapshotDetector} instance.
         */
        @Inject
        public DefaultBuilder(
                final SnapshotDetector snapshotDetector)
        {
            this.snapshotDetector = snapshotDetector;
        }

        @Override
        public Gav newGav(
                final GroupId groupId,
                final ArtifactId artifactId,
                final Version version)
        {
            return new DefaultGav(snapshotDetector, groupId, artifactId, version);
        }
    }

    private final SnapshotDetector snapshotDetector;
    private final GroupId groupId;
    private final ArtifactId artifactId;
    private final Version version;
    private final String gav;

    /**
     * Creates a new {@code DefaultGav}.
     *
     * @param snapshotDetector
     *            the {@code SnapshotDetector} instance.
     * @param groupId
     *            the group id.
     * @param artifactId
     *            the artifact id.
     * @param version
     *            the version.
     */
    public DefaultGav(
            final SnapshotDetector snapshotDetector,
            final GroupId groupId,
            final ArtifactId artifactId,
            final Version version)
    {
        checkNotNull(snapshotDetector, "Missing 'snapshotDetector'.");
        checkNotNull(groupId, "Missing 'groupId'.");
        checkNotNull(artifactId, "Missing 'artifactId'.");
        checkNotNull(version, "Missing 'version'.");
        this.snapshotDetector = snapshotDetector;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        gav = groupId + ":" + artifactId + ":" + version;
    }

    @Override
    public boolean isSnapshot()
    {
        return snapshotDetector.isSnapshot(this);
    }

    @Override
    public GroupId getGroupId()
    {
        return groupId;
    }

    @Override
    public ArtifactId getArtifactId()
    {
        return artifactId;
    }

    @Override
    public Version getVersion()
    {
        return version;
    }

    @Override
    public int hashCode()
    {
        final int prime = 43;
        final int result = prime + gav.hashCode();
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
        final DefaultGav other = (DefaultGav) object;
        return gav.equals(other.gav);
    }

    @Override
    public String toString()
    {
        return gav;
    }
}

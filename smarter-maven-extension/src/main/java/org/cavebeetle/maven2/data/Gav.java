package org.cavebeetle.maven2.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

public final class Gav
        implements
            Comparable<Gav>
{
    private static final Interner<Gav> INTERNER = Interners.newWeakInterner();

    public static final Gav make(final GroupId groupId, final ArtifactId artifactId, final Version version)
    {
        return INTERNER.intern(new Gav(groupId, artifactId, version));
    }

    private final GroupId groupId;
    private final ArtifactId artifactId;
    private final Version version;

    Gav(final GroupId groupId, final ArtifactId artifactId, final Version version)
    {
        Preconditions.checkNotNull(groupId, "Missing 'groupId'.");
        Preconditions.checkNotNull(artifactId, "Missing 'artifactId'.");
        Preconditions.checkNotNull(version, "Missing 'version'.");
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public GroupId groupId()
    {
        return groupId;
    }

    public ArtifactId artifactId()
    {
        return artifactId;
    }

    public Version version()
    {
        return version;
    }

    @Override
    public String toString()
    {
        return String.format("[Gav groupId=%s artifactId=%s version=%s]", groupId, artifactId, version);
    }

    @Override
    public int compareTo(final Gav other)
    {
        return ComparisonChain
                .start()
                .compare(groupId, other.groupId())
                .compare(artifactId, other.artifactId())
                .compare(version, other.version())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + groupId.hashCode();
        result = prime * result + artifactId.hashCode();
        result = prime * result + version.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final Gav other = (Gav) object;
        return compareTo(other) == 0;
    }
}

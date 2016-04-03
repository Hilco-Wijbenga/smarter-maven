package org.cavebeetle.maven2.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

public final class ArtifactId
        implements
            Comparable<ArtifactId>
{
    private final String value;

    public ArtifactId(final String value)
    {
        Preconditions.checkArgument(value != null && !value.isEmpty(), "Missing 'value'.");
        this.value = value;
    }

    public String value()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return String.format("[ArtifactId value='%s']", value);
    }

    @Override
    public int compareTo(final ArtifactId other)
    {
        return ComparisonChain
                .start()
                .compare(value, other.value())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + value.hashCode();
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
        final ArtifactId other = (ArtifactId) object;
        return compareTo(other) == 0;
    }
}

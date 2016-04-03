package org.cavebeetle.maven2.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

public final class UpstreamProject
        implements
            Comparable<UpstreamProject>
{
    private final Project value;
    private final UpstreamReason upstreamReason;

    public UpstreamProject(final Project value, final UpstreamReason upstreamReason)
    {
        Preconditions.checkArgument(value != null, "Missing 'value'.");
        Preconditions.checkArgument(upstreamReason != null, "Missing 'upstreamReason'.");
        this.value = value;
        this.upstreamReason = upstreamReason;
    }

    public Project value()
    {
        return value;
    }

    public UpstreamReason upstreamReason()
    {
        return upstreamReason;
    }

    @Override
    public String toString()
    {
        return String.format("[UpstreamProject value='%s' upstreamReason=%s]", value, upstreamReason);
    }

    @Override
    public int compareTo(final UpstreamProject other)
    {
        return ComparisonChain
                .start()
                .compare(value, other.value())
                .compare(upstreamReason, other.upstreamReason())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + value.hashCode();
        result = prime * result + upstreamReason.hashCode();
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
        final UpstreamProject other = (UpstreamProject) object;
        return compareTo(other) == 0;
    }
}

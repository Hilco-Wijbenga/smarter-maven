package org.cavebeetle.maven2.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

public final class GroupId
        implements
            Comparable<GroupId>
{
    private static final Interner<GroupId> INTERNER = Interners.newWeakInterner();

    public static final GroupId make(final String groupId)
    {
        return INTERNER.intern(new GroupId(groupId));
    }

    private final String value;

    GroupId(final String value)
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
        return String.format("[GroupId value='%s']", value);
    }

    @Override
    public int compareTo(final GroupId other)
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
        final GroupId other = (GroupId) object;
        return compareTo(other) == 0;
    }
}

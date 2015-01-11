package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkArgument;
import javax.inject.Singleton;
import org.cavebeetle.maven.GroupId;

/**
 * The implementation of {@code GroupId}.
 */
public final class DefaultGroupId
        implements
            GroupId
{
    /**
     * The implementation of {@code GroupId.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        @Override
        public GroupId newGroupId(
                final String groupId)
        {
            return new DefaultGroupId(groupId);
        }
    }

    private final String groupId;

    /**
     * Creates a new {@code DefaultGroupId}.
     *
     * @param groupId
     *            the text representing the group id.
     */
    public DefaultGroupId(
            final String groupId)
    {
        checkArgument(groupId != null && !groupId.isEmpty(), "Missing 'groupId'.");
        this.groupId = groupId;
    }

    @Override
    public int hashCode()
    {
        final int prime = 41;
        final int result = prime + groupId.hashCode();
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
        final DefaultGroupId other = (DefaultGroupId) object;
        return groupId.equals(other.groupId);
    }

    @Override
    public String toString()
    {
        return groupId;
    }
}

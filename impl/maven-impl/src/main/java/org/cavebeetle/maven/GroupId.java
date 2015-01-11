package org.cavebeetle.maven;

/**
 * A project's group id.
 */
public interface GroupId
{
    /**
     * The constructor API for {@code GroupId}.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code GroupId}.
         *
         * @param groupId
         *            the text representing the group id.
         * @return a new {@code GroupId}.
         */
        GroupId newGroupId(
                String groupId);
    }
}

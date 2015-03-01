package org.cavebeetle.maven.plugins;

import javax.xml.stream.Location;

public final class GroupId
        implements
            HasLocation
{
    private final Location location;
    private final String groupId;

    public GroupId(
            final Location location,
            final String groupId)
    {
        this.location = location;
        this.groupId = groupId;
    }

    @Override
    public Location getLocation()
    {
        return location;
    }

    @Override
    public String toString()
    {
        return groupId;
    }
}

package org.cavebeetle.maven.plugins;

import javax.xml.stream.Location;

public final class Version
        implements
            HasLocation
{
    private final Location location;
    private final String version;

    public Version(
            final Location location,
            final String version)
    {
        this.location = location;
        this.version = version;
    }

    @Override
    public Location getLocation()
    {
        return location;
    }

    @Override
    public String toString()
    {
        return version;
    }
}

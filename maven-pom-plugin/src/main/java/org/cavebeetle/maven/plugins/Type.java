package org.cavebeetle.maven.plugins;

import javax.xml.stream.Location;

public final class Type
        implements
            HasLocation
{
    public static final Type DEFAULT = new Type("jar");
    private final Location location;
    private final String type;

    public Type(
            final String type)
    {
        location = null;
        this.type = type;
    }

    public Type(
            final Location location,
            final String type)
    {
        this.location = location;
        this.type = type;
    }

    @Override
    public Location getLocation()
    {
        return location;
    }

    @Override
    public String toString()
    {
        return type;
    }
}

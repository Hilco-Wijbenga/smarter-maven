package org.cavebeetle.maven.plugins;

import javax.xml.stream.Location;

public final class RelativePath
        implements
            HasLocation
{
    public static final RelativePath DEFAULT = new RelativePath("..");
    private final Location location;
    private final String relativePath;

    private RelativePath(
            final String relativePath)
    {
        location = null;
        this.relativePath = relativePath;
    }

    public RelativePath(
            final Location location,
            final String relativePath)
    {
        this.location = location;
        this.relativePath = relativePath;
    }

    @Override
    public Location getLocation()
    {
        return location;
    }

    @Override
    public String toString()
    {
        return relativePath;
    }
}

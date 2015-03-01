package org.cavebeetle.maven.plugins;

import javax.xml.stream.Location;

public final class ArtifactId
        implements
            HasLocation
{
    private final Location location;
    private final String artifactId;

    public ArtifactId(
            final Location location,
            final String artifactId)
    {
        this.location = location;
        this.artifactId = artifactId;
    }

    @Override
    public Location getLocation()
    {
        return location;
    }

    @Override
    public String toString()
    {
        return artifactId;
    }
}

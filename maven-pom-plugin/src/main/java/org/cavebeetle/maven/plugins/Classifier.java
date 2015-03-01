package org.cavebeetle.maven.plugins;

import javax.xml.stream.Location;

public final class Classifier
        implements
            HasLocation
{
    public static final Classifier DEFAULT = new Classifier("");
    private final Location location;
    private final String classifier;

    public Classifier(
            final String classifier)
    {
        location = null;
        this.classifier = classifier;
    }

    public Classifier(
            final Location location,
            final String classifier)
    {
        this.location = location;
        this.classifier = classifier;
    }

    @Override
    public Location getLocation()
    {
        return location;
    }

    @Override
    public String toString()
    {
        return classifier;
    }
}

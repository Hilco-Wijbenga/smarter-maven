package org.cavebeetle.maven;

public final class GavTuple
{
    private final Gav projectGav;
    private final Gav dependencyGav;

    public GavTuple(
            final Gav projectGav,
            final Gav dependencyGav)
    {
        this.projectGav = projectGav;
        this.dependencyGav = dependencyGav;
    }

    public Gav getProjectGav()
    {
        return projectGav;
    }

    public Gav getDependencyGav()
    {
        return dependencyGav;
    }

    @Override
    public String toString()
    {
        return "( " + projectGav + " --> " + dependencyGav + " )";
    }
}

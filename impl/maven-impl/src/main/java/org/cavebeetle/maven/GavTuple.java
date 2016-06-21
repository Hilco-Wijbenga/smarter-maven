package org.cavebeetle.maven;

/**
 * A {@code GavTuple} stores both a project's GAV, and the GAV of one of its dependencies.
 */
public final class GavTuple
{
    private final Gav projectGav;
    private final Gav dependencyGav;

    /**
     * Creates a new {@code GavTuple}.
     *
     * @param projectGav
     *            the {@code Project}'s GAV.
     * @param dependencyGav
     *            the dependency's GAV.
     */
    public GavTuple(final Gav projectGav, final Gav dependencyGav)
    {
        this.projectGav = projectGav;
        this.dependencyGav = dependencyGav;
    }

    /**
     * Gets the {@code Project}'s GAV.
     *
     * @return the {@code Project}'s GAV.
     */
    public Gav getProjectGav()
    {
        return projectGav;
    }

    /**
     * Gets the dependency's GAV.
     *
     * @return the dependency's GAV.
     */
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

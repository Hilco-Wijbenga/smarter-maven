package org.cavebeetle.maven;

/**
 * A map from {@code Gav}s to their corresponding {@code Project}s.
 */
public interface GavToProjectMap
        extends
            Iterable<Gav>
{
    /**
     * A factory of {@code GavToProjectMap} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code GavToProjectMap}.
         *
         * @return a new {@code GavToProjectMap}.
         */
        GavToProjectMap newGavToProjectMap();
    }

    /**
     * Returns whether the given {@code Gav} is a key in this {@code GavToProjectMap}.
     *
     * @param gav
     *            the {@code Gav} to investigate.
     * @return {@code true} if and only if the given {@code Gav} is a key in this {@code GavToProjectMap}.
     */
    boolean containsProjectForGav(
            Gav gav);

    /**
     * Gets the {@code Project} associated with the given {@code Gav}.
     *
     * @param gav
     *            the {@code Gav} to look for.
     * @return the {@code Project} associated with the given {@code Gav}.
     */
    Project getProject(
            Gav gav);

    /**
     * Associates the given {@code Gav} with the given {@code Project}.
     *
     * @param gav
     *            the {@code Gav} key.
     * @param project
     *            the {@code Project} value.
     */
    void putProject(
            Gav gav,
            Project project);
}

package org.cavebeetle.maven;

import java.util.List;
import com.google.common.base.Optional;

/**
 * An {@code InvalidProjectHierarchyDetector} detects whether any non-snapshot project in the project hierarchy has
 * snapshot dependencies.
 */
public interface InvalidProjectHierarchyDetector
{
    /**
     * Aborts the build if an invalid project hierarchy is detected. If a non-snapshot project has a snapshot dependency
     * then the project hierarchy is deemed invalid.
     *
     * @param gavToProjectMap
     *            the {@code GavToProjectMap} with all projects.
     * @return the error message explaining which projects are invalid.
     */
    Optional<String> getInvalidProjectHierarchyError(
            GavToProjectMap gavToProjectMap);

    /**
     * Gets the list of warnings that list the (potential) problems with the project hierarchy.
     *
     * @param gavToProjectMap
     *            the {@code GavToProjectMap} with all projects.
     * @return the list of warnings that list the (potential) problems with the project hierarchy.
     */
    List<String> getProjectHierarchyWarnings(
            GavToProjectMap gavToProjectMap);
}

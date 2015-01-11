package org.cavebeetle.maven;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;

/**
 * A {@code GavGenerator} facilitates generating {@code Gav}s.
 */
public interface GavGenerator
{
    /**
     * <p>
     * Returns the GAV (groupId, artifactId, and version) for the given {@code Dependency}.
     * </p>
     * <p>
     * <strong>Note: returns {@code null} if the given {@code Dependency} is {@code null}.</strong>
     * </p>
     *
     * @param dependency
     *            the {@code Dependency} of interest.
     * @return the {@code Gav} for the given {@code Dependency} or {@code null}.
     */
    Gav getGav(
            Dependency dependency);

    /**
     * <p>
     * Returns the GAV (groupId, artifactId, and version) for the given {@code MavenProject}.
     * </p>
     * <p>
     * <strong>Note: returns {@code null} if the given {@code MavenProject} is {@code null}.</strong>
     * </p>
     *
     * @param mavenProject
     *            the {@code MavenProject} of interest.
     * @return the {@code Gav} for the given {@code MavenProject} or {@code null}.
     */
    Gav getGav(
            MavenProject mavenProject);
}

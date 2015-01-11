package org.cavebeetle.maven;

import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;

/**
 * A {@code SnapshotDetector} detects whether a {@code Project} has a snapshot version.
 */
public interface SnapshotDetector
{
    /**
     * Returns whether the given {@code MavenProject} has a snapshot version.
     *
     * @param mavenProject
     *            the {@code MavenProject} of interest.
     * @return {@code true} if and only if the given {@code MavenProject} has a snapshot version.
     */
    boolean isSnapshot(
            MavenProject mavenProject);

    /**
     * Returns whether the given {@code Dependency} has a snapshot version.
     *
     * @param dependency
     *            the {@code Dependency} of interest.
     * @return {@code true} if and only if the given {@code Dependency} has a snapshot version.
     */
    boolean isSnapshot(
            Dependency dependency);

    /**
     * Returns whether the given {@code Gav} has a snapshot version.
     *
     * @param gav
     *            the {@code Gav} of interest.
     * @return {@code true} if and only if the given {@code Gav} has a snapshot version.
     */
    boolean isSnapshot(
            Gav gav);
}

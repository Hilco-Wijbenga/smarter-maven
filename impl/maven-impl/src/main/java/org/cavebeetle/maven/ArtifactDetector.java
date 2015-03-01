package org.cavebeetle.maven;

import java.io.File;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

/**
 * An {@code ArtifactDetector} detects whether a project is available in the local repository.
 */
public interface ArtifactDetector
{
    /**
     * Returns whether the given project is available in the local repository.
     *
     * @param session
     *            the {@code MavenSession} instance.
     * @param project
     *            the project to search for.
     * @return {@code true} if and only if the given project is available in the local repository.
     */
    boolean hasArtifactInLocalRepository(
            MavenSession session,
            MavenProject project);

    /**
     * Returns the location of the local Maven repo. Normally, this is {@code ~/.m2/repository}.
     *
     * @param mavenSession
     *            the {@code MavenSession} instance.
     * @return the location of the local Maven repo.
     */
    File getLocalRepositoryDirectory(
            MavenSession mavenSession);

    /**
     * Returns whether the given project is available in a remote repository.
     *
     * @param project
     *            the project to search for.
     * @return {@code true} if and only if the given project is available in a remote repository.
     */
    boolean hasArtifactInRemoteRepository(
            MavenProject project);
}

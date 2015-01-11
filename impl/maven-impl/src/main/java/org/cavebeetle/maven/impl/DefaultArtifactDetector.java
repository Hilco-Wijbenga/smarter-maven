package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.File;
import javax.inject.Singleton;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven.ArtifactDetector;

/**
 * The implementation of {@code ArtifactDetector}.
 */
@Singleton
public final class DefaultArtifactDetector
        implements
            ArtifactDetector
{
    @Override
    public boolean hasArtifactInLocalRepository(
            final MavenSession mavenSession,
            final MavenProject mavenProject)
    {
        checkNotNull(mavenSession, "Missing 'mavenSession'.");
        checkNotNull(mavenProject, "Missing 'mavenProject'.");
        final ArtifactRepository artifactRepository = mavenSession.getLocalRepository();
        final File localRepositoryDir = new File(artifactRepository.getBasedir());
        final String pathToArtifact = artifactRepository.pathOf(mavenProject.getArtifact());
        final File artifactFile = new File(localRepositoryDir, pathToArtifact);
        return artifactFile.exists();
    }

    @Override
    public boolean hasArtifactInRemoteRepository(
            final MavenProject mavenProject)
    {
        checkNotNull(mavenProject, "Missing 'mavenProject'.");
        final Artifact artifact = mavenProject.getArtifact();
        for (final ArtifactRepository remoteRepo : mavenProject.getRemoteArtifactRepositories())
        {
            if (remoteRepo.findVersions(artifact).contains(artifact.getVersion()))
            {
                return true;
            }
        }
        return false;
    }
}

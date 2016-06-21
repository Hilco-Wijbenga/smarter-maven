package org.cavebeetle.maven.impl;

import javax.inject.Singleton;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven.Gav;
import org.cavebeetle.maven.SnapshotDetector;

/**
 * The implementation of {@code SnapshotDetector}.
 */
@Singleton
public final class DefaultSnapshotDetector
        implements
            SnapshotDetector
{
    @Override
    public boolean isSnapshot(final MavenProject mavenProject)
    {
        try
        {
            final Artifact artifact = mavenProject.getArtifact();
            final ArtifactVersion artifactVersion = artifact.getSelectedVersion();
            return isSnapshot(artifactVersion.toString());
        }
        catch (final Exception e)
        {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean isSnapshot(final Dependency dependency)
    {
        return isSnapshot(dependency.getVersion());
    }

    @Override
    public boolean isSnapshot(final Gav gav)
    {
        return isSnapshot(gav.getVersion().toString());
    }

    private boolean isSnapshot(final String version)
    {
        return version.endsWith("-SNAPSHOT");
    }
}

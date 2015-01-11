package org.cavebeetle.maven.impl;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.maven.model.Dependency;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven.ArtifactId;
import org.cavebeetle.maven.Gav;
import org.cavebeetle.maven.GavGenerator;
import org.cavebeetle.maven.GroupId;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.Version;

/**
 * The implementation of {@code GavGenerator}.
 */
@Singleton
public final class DefaultGavGenerator
        implements
            GavGenerator
{
    private final InternalApi internalApi;

    /**
     * Creates a new {@code DefaultGavGenerator}.
     *
     * @param internalApi
     *            the {@code InternalApi} instance.
     */
    @Inject
    public DefaultGavGenerator(
            final InternalApi internalApi)
    {
        this.internalApi = internalApi;
    }

    @Override
    public Gav getGav(
            final Dependency dependency)
    {
        final GroupId groupId = internalApi.newGroupId(dependency.getGroupId());
        final ArtifactId artifactId = internalApi.newArtifactId(dependency.getArtifactId());
        final Version version = internalApi.newVersion(dependency.getVersion());
        return internalApi.newGav(groupId, artifactId, version);
    }

    @Override
    public Gav getGav(
            final MavenProject mavenProject)
    {
        final GroupId groupId = internalApi.newGroupId(mavenProject.getGroupId());
        final ArtifactId artifactId = internalApi.newArtifactId(mavenProject.getArtifactId());
        final Version version = internalApi.newVersion(mavenProject.getVersion());
        return internalApi.newGav(groupId, artifactId, version);
    }
}

package org.cavebeetle.maven.impl;

import java.io.File;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.cavebeetle.maven.ActiveDetector;
import org.cavebeetle.maven.AfterProjectsRead;
import org.cavebeetle.maven.AfterProjectsReadInternal;
import org.cavebeetle.maven.AfterSessionStart;
import org.cavebeetle.maven.ArtifactDetector;
import org.cavebeetle.maven.ArtifactId;
import org.cavebeetle.maven.CryptographicHash;
import org.cavebeetle.maven.CryptographicHashAlgorithm;
import org.cavebeetle.maven.Digest;
import org.cavebeetle.maven.DirtDetector;
import org.cavebeetle.maven.DirtyReason;
import org.cavebeetle.maven.FileHashGenerator;
import org.cavebeetle.maven.Gav;
import org.cavebeetle.maven.GavGenerator;
import org.cavebeetle.maven.GavToProjectMap;
import org.cavebeetle.maven.GroupId;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.InvalidProjectHierarchyDetector;
import org.cavebeetle.maven.MavenExecutionListener;
import org.cavebeetle.maven.MavenExtension;
import org.cavebeetle.maven.Project;
import org.cavebeetle.maven.SnapshotDetector;
import org.cavebeetle.maven.SourceFilesDigest;
import org.cavebeetle.maven.SourceFilesHashGenerator;
import org.cavebeetle.maven.Version;
import org.codehaus.plexus.logging.Logger;
import com.google.inject.Injector;

/**
 * The implementation of {@code InternalApi}.
 */
@Singleton
public final class DefaultInternalApi
        implements
            InternalApi
{
    private final Injector injector;

    /**
     * Creates a new {@code DefaultInternalApi}.
     *
     * @param injector
     *            the Guice {@code Injector} instance.
     */
    @Inject
    public DefaultInternalApi(final Injector injector)
    {
        this.injector = injector;
    }

    @Override
    public MavenExtension getMavenExtension()
    {
        return injector.getInstance(MavenExtension.class);
    }

    @Override
    public ActiveDetector getActiveDetector()
    {
        return injector.getInstance(ActiveDetector.class);
    }

    @Override
    public AfterSessionStart getAfterSessionStart()
    {
        return injector.getInstance(AfterSessionStart.class);
    }

    @Override
    public AfterProjectsRead getAfterProjectsRead()
    {
        return injector.getInstance(AfterProjectsRead.class);
    }

    @Override
    public AfterProjectsReadInternal getAfterProjectsReadInternal()
    {
        return injector.getInstance(AfterProjectsReadInternal.class);
    }

    @Override
    public InvalidProjectHierarchyDetector getInvalidProjectHierarchyDetector()
    {
        return injector.getInstance(InvalidProjectHierarchyDetector.class);
    }

    @Override
    public ArtifactDetector getArtifactDetector()
    {
        return injector.getInstance(ArtifactDetector.class);
    }

    @Override
    public FileHashGenerator getFileHashGenerator()
    {
        return injector.getInstance(FileHashGenerator.class);
    }

    @Override
    public DirtDetector getDirtDetector()
    {
        return injector.getInstance(DirtDetector.class);
    }

    @Override
    public GavGenerator getGavGenerator()
    {
        return injector.getInstance(GavGenerator.class);
    }

    @Override
    public MavenExecutionListener getMavenExecutionListener()
    {
        return injector.getInstance(MavenExecutionListener.class);
    }

    @Override
    public SnapshotDetector getSnapshotDetector()
    {
        return injector.getInstance(SnapshotDetector.class);
    }

    @Override
    public SourceFilesHashGenerator getSourceFilesHashGenerator()
    {
        return injector.getInstance(SourceFilesHashGenerator.class);
    }

    @Override
    public Project newProject(
            final Logger logger,
            final MavenSession mavenSession,
            final ProjectBuilder mavenProjectBuilder,
            final MavenProject mavenProject,
            final GavToProjectMap gavToProjectMap)
    {
        final Project.Builder projectBuilder = injector.getInstance(Project.Builder.class);
        return projectBuilder.newProject(logger, mavenSession, mavenProjectBuilder, mavenProject, gavToProjectMap);
    }

    @Override
    public CryptographicHash newCryptographicHash(final CryptographicHashAlgorithm cryptographicHashAlgorithm)
    {
        return injector.getInstance(CryptographicHash.Builder.class).newCryptographicHash(cryptographicHashAlgorithm);
    }

    @Override
    public GavToProjectMap newGavToProjectMap()
    {
        return injector.getInstance(GavToProjectMap.Builder.class).newGavToProjectMap();
    }

    @Override
    public Digest newDigest(final byte[] bytes)
    {
        return injector.getInstance(Digest.Builder.class).newDigest(bytes);
    }

    @Override
    public Gav newGav(final GroupId groupId, final ArtifactId artifactId, final Version version)
    {
        return injector.getInstance(Gav.Builder.class).newGav(groupId, artifactId, version);
    }

    @Override
    public GroupId newGroupId(final String groupId)
    {
        return injector.getInstance(GroupId.Builder.class).newGroupId(groupId);
    }

    @Override
    public ArtifactId newArtifactId(final String artifactId)
    {
        return injector.getInstance(ArtifactId.Builder.class).newArtifactId(artifactId);
    }

    @Override
    public DirtyReason newDirtyReason(final boolean dirty, final String reason)
    {
        return injector.getInstance(DirtyReason.Builder.class).newDirtyReason(dirty, reason);
    }

    @Override
    public Version newVersion(final String version)
    {
        return injector.getInstance(Version.Builder.class).newVersion(version);
    }

    @Override
    public SourceFilesDigest newSourceFilesDigest(final List<String> sourceFileLines)
    {
        final SourceFilesDigest.Builder builder = injector.getInstance(SourceFilesDigest.Builder.class);
        return builder.newSourceFilesDigest(sourceFileLines);
    }

    @Override
    public SourceFilesDigest newSourceFilesDigest(final File file)
    {
        final SourceFilesDigest.Builder builder = injector.getInstance(SourceFilesDigest.Builder.class);
        return builder.newSourceFilesDigest(file);
    }
}

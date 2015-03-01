package org.cavebeetle.maven.impl;

import org.cavebeetle.maven.ActiveDetector;
import org.cavebeetle.maven.AfterProjectsRead;
import org.cavebeetle.maven.AfterProjectsReadInternal;
import org.cavebeetle.maven.AfterSessionStart;
import org.cavebeetle.maven.ArtifactDetector;
import org.cavebeetle.maven.ArtifactId;
import org.cavebeetle.maven.CryptographicHash;
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
import org.cavebeetle.maven.MavenApi;
import org.cavebeetle.maven.MavenExecutionListener;
import org.cavebeetle.maven.MavenExtension;
import org.cavebeetle.maven.Project;
import org.cavebeetle.maven.SnapshotDetector;
import org.cavebeetle.maven.SourceFilesDigest;
import org.cavebeetle.maven.SourceFilesHashGenerator;
import org.cavebeetle.maven.Version;
import com.google.inject.AbstractModule;

/**
 * The Guice module describing the required bindings.
 */
public final class MavenGuiceModule
        extends
            AbstractModule
{
    @Override
    public void configure()
    {
        bind(ActiveDetector.class).to(DefaultActiveDetector.class);
        bind(AfterProjectsRead.class).to(DefaultAfterProjectsRead.class);
        bind(AfterProjectsReadInternal.class).to(DefaultAfterProjectsReadInternal.class);
        bind(AfterSessionStart.class).to(DefaultAfterSessionStart.class);
        bind(ArtifactDetector.class).to(DefaultArtifactDetector.class);
        bind(ArtifactId.Builder.class).to(DefaultArtifactId.DefaultBuilder.class);
        bind(CryptographicHash.Builder.class).to(DefaultCryptographicHash.DefaultBuilder.class);
        bind(Digest.Builder.class).to(DefaultDigest.DefaultBuilder.class);
        bind(DirtDetector.class).to(DefaultDirtDetector.class);
        bind(DirtyReason.Builder.class).to(DefaultDirtyReason.DefaultBuilder.class);
        bind(FileHashGenerator.class).to(DefaultFileHashGenerator.class);
        bind(Gav.Builder.class).to(DefaultGav.DefaultBuilder.class);
        bind(GavGenerator.class).to(DefaultGavGenerator.class);
        bind(GavToProjectMap.Builder.class).to(DefaultGavToProjectMap.DefaultBuilder.class);
        bind(GroupId.Builder.class).to(DefaultGroupId.DefaultBuilder.class);
        bind(InternalApi.class).to(DefaultInternalApi.class);
        bind(InvalidProjectHierarchyDetector.class).to(DefaultInvalidProjectHierarchyDetector.class);
        bind(MavenApi.class).to(DefaultInternalApi.class);
        bind(MavenExecutionListener.class).to(DefaultMavenExecutionListener.class);
        bind(MavenExtension.class).to(DefaultMavenExtension.class);
        bind(Project.Builder.class).to(DefaultProject.DefaultBuilder.class);
        bind(SnapshotDetector.class).to(DefaultSnapshotDetector.class);
        bind(SourceFilesDigest.Builder.class).to(DefaultSourceFilesDigest.DefaultBuilder.class);
        bind(SourceFilesHashGenerator.class).to(DefaultSourceFilesHashGenerator.class);
        bind(Version.Builder.class).to(DefaultVersion.DefaultBuilder.class);
    }
}

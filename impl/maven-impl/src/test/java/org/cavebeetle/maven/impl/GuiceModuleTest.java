package org.cavebeetle.maven.impl;

import static com.google.inject.Guice.createInjector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
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
import org.junit.Before;
import org.junit.Test;
import com.google.inject.Injector;

/**
 * The unit tests for Guice.
 */
public final class GuiceModuleTest
{
    private Injector injector;

    /**
     * Sets up each unit test case.
     */
    @Before
    public void setUp()
    {
        final MavenGuiceModule guiceModule = new MavenGuiceModule();
        injector = createInjector(guiceModule, new DummyGuiceModule());
    }

    /**
     * Checks that no Guice bindings were missed.
     */
    @Test
    public final void check_that_no_Guice_bindings_were_missed()
    {
        assertEquals(25, injector.getBindings().size() - 4);
    }

    /**
     * Checks that Guice creates a singleton {@code MavenApi}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_MavenApi()
    {
        final Class<MavenApi> instanceType = MavenApi.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code InternalApi}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_InternalApi()
    {
        final Class<InternalApi> instanceType = InternalApi.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates the same instance for both {@code MavenApi} and {@code InternalApi}.
     */
    @Test
    public final void check_that_Guice_creates_the_same_instance_for_both_MavenApi_and_InternalApi()
    {
        assertSame(injector.getInstance(MavenApi.class), injector.getInstance(InternalApi.class));
    }

    /**
     * Checks that Guice creates a singleton {@code ActiveDetector}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_ActiveDetector()
    {
        final Class<ActiveDetector> instanceType = ActiveDetector.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code ArtifactDetector}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_ArtifactDetector()
    {
        final Class<ArtifactDetector> instanceType = ArtifactDetector.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code DirtDetector}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_DirtDetector()
    {
        final Class<DirtDetector> instanceType = DirtDetector.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code FileHashGenerator}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_FileHashGenerator()
    {
        final Class<FileHashGenerator> instanceType = FileHashGenerator.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code GavGenerator}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_GavGenerator()
    {
        final Class<GavGenerator> instanceType = GavGenerator.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code AfterProjectsRead}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_AfterProjectsRead()
    {
        final Class<AfterProjectsRead> instanceType = AfterProjectsRead.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code AfterProjectsReadInternal}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_AfterProjectsReadInternal()
    {
        final Class<AfterProjectsReadInternal> instanceType = AfterProjectsReadInternal.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code InvalidProjectHierarchyDetector}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_InvalidProjectHierarchyDetector()
    {
        final Class<InvalidProjectHierarchyDetector> instanceType = InvalidProjectHierarchyDetector.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code AfterSessionStart}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_AfterSessionStart()
    {
        final Class<AfterSessionStart> instanceType = AfterSessionStart.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code MavenExecutionListener}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_MavenExecutionListener()
    {
        final Class<MavenExecutionListener> instanceType = MavenExecutionListener.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code MavenExtension}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_MavenExtension()
    {
        final Class<MavenExtension> instanceType = MavenExtension.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code SnapshotDetector}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_SnapshotDetector()
    {
        final Class<SnapshotDetector> instanceType = SnapshotDetector.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code SourceFilesHashGenerator}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_SourceFilesHashGenerator()
    {
        final Class<SourceFilesHashGenerator> instanceType = SourceFilesHashGenerator.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code Project.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_Project_Builder()
    {
        final Class<Project.Builder> instanceType = Project.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code CryptographicHash.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_CryptographicHash_Builder()
    {
        final Class<CryptographicHash.Builder> instanceType = CryptographicHash.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code GavToProjectMap.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_GavToProjectMap_Builder()
    {
        final Class<GavToProjectMap.Builder> instanceType = GavToProjectMap.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code Gav.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_Gav_Builder()
    {
        final Class<Gav.Builder> instanceType = Gav.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code GroupId.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_GroupId_Builder()
    {
        final Class<GroupId.Builder> instanceType = GroupId.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code ArtifactId.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_ArtifactId_Builder()
    {
        final Class<ArtifactId.Builder> instanceType = ArtifactId.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code Version.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_Version_Builder()
    {
        final Class<Version.Builder> instanceType = Version.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code Digest.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_Digest_Builder()
    {
        final Class<Digest.Builder> instanceType = Digest.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code DirtyReason.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_DirtyReason_Builder()
    {
        final Class<DirtyReason.Builder> instanceType = DirtyReason.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code SourceFilesDigest.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_SourceFilesDigest_Builder()
    {
        final Class<SourceFilesDigest.Builder> instanceType = SourceFilesDigest.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }
}

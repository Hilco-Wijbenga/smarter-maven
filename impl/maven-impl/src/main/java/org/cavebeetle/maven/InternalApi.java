package org.cavebeetle.maven;

/**
 * The internal API of {@code org.cavebeetle.maven}.
 */
public interface InternalApi
        extends
            MavenApi,
            Project.Builder,
            CryptographicHash.Builder,
            GavToProjectMap.Builder,
            Digest.Builder,
            Gav.Builder,
            GroupId.Builder,
            ArtifactId.Builder,
            DirtyReason.Builder,
            Version.Builder,
            SourceFilesDigest.Builder
{
    /**
     * Gets the singleton {@code ActiveDetector} instance.
     *
     * @return the singleton {@code ActiveDetector} instance.
     */
    ActiveDetector getActiveDetector();

    /**
     * Gets the singleton {@code AfterSessionStart} instance.
     *
     * @return the singleton {@code AfterSessionStart} instance.
     */
    AfterSessionStart getAfterSessionStart();

    /**
     * Gets the singleton {@code AfterProjectsRead} instance.
     *
     * @return the singleton {@code AfterProjectsRead} instance.
     */
    AfterProjectsRead getAfterProjectsRead();

    /**
     * Gets the singleton {@code AfterProjectsReadInternal} instance.
     *
     * @return the singleton {@code AfterProjectsReadInternal} instance.
     */
    AfterProjectsReadInternal getAfterProjectsReadInternal();

    /**
     * Gets the singleton {@code InvalidProjectHierarchyDetector} instance.
     *
     * @return the singleton {@code InvalidProjectHierarchyDetector} instance.
     */
    InvalidProjectHierarchyDetector getInvalidProjectHierarchyDetector();

    /**
     * Gets the singleton {@code ArtifactDetector} instance.
     *
     * @return the singleton {@code ArtifactDetector} instance.
     */
    ArtifactDetector getArtifactDetector();

    /**
     * Gets the singleton {@code FileHashGenerator} instance.
     *
     * @return the singleton {@code FileHashGenerator} instance.
     */
    FileHashGenerator getFileHashGenerator();

    /**
     * Gets the singleton {@code DirtDetector} instance.
     *
     * @return the singleton {@code DirtDetector} instance.
     */
    DirtDetector getDirtDetector();

    /**
     * Gets the singleton {@code GavGenerator} instance.
     *
     * @return the singleton {@code GavGenerator} instance.
     */
    GavGenerator getGavGenerator();

    /**
     * Gets the singleton {@code MavenExecutionListener} instance.
     *
     * @return the singleton {@code MavenExecutionListener} instance.
     */
    MavenExecutionListener getMavenExecutionListener();

    /**
     * Gets the singleton {@code SnapshotDetector} instance.
     *
     * @return the singleton {@code SnapshotDetector} instance.
     */
    SnapshotDetector getSnapshotDetector();

    /**
     * Gets the singleton {@code SourceFilesHashGenerator} instance.
     *
     * @return the singleton {@code SourceFilesHashGenerator} instance.
     */
    SourceFilesHashGenerator getSourceFilesHashGenerator();
}

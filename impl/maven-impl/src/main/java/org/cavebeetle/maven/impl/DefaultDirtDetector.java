package org.cavebeetle.maven.impl;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static org.cavebeetle.maven.DirtyReason.CHANGES_DETECTED;
import static org.cavebeetle.maven.DirtyReason.NOT_DIRTY;
import static org.cavebeetle.maven.DirtyReason.NOT_IN_LOCAL_REPO;
import static org.cavebeetle.maven.DirtyReason.NO_BUILD_DIRECTORY;
import static org.cavebeetle.maven.DirtyReason.NO_HASH_FILE;
import static org.cavebeetle.maven.DirtyReason.PUBLISHED;
import static org.cavebeetle.maven.SourceFilesHashGenerator.SOURCE_FILES_LISTING;
import static org.cavebeetle.maven.impl.Equalizer.isEqual;
import java.io.File;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven.ArtifactDetector;
import org.cavebeetle.maven.Digest;
import org.cavebeetle.maven.DirtDetector;
import org.cavebeetle.maven.DirtyReason;
import org.cavebeetle.maven.FileHashGenerator;
import org.cavebeetle.maven.GavGenerator;
import org.cavebeetle.maven.GavToProjectMap;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.Project;
import org.cavebeetle.maven.SnapshotDetector;
import org.cavebeetle.maven.SourceFilesHashGenerator;
import org.codehaus.plexus.logging.Logger;
import com.google.common.base.Optional;

/**
 * The implementation of {@code DirtDetector}.
 */
@Singleton
public final class DefaultDirtDetector
        implements
            DirtDetector
{
    /**
     * Returns the first of the {@code Project}'s dirty dependencies or {@code Optional.absent()} if there are none.
     *
     * @param project
     *            the {@code Project} to investigate.
     * @param includeModules
     *            whether to include the project's modules when determining "dirtiness".
     * @return the first of the {@code Project}'s dirty dependencies or {@code Optional.absent()} if there are none.
     */
    public static Optional<Project> hasDirtyDependency(
            final Project project,
            final boolean includeModules)
    {
        for (final Project dependency : project.getDependencies())
        {
            final DirtyReason dependencyDirtyReason = dependency.findDirtyReason(false);
            if (dependencyDirtyReason.isDirty())
            {
                return of(dependency);
            }
        }
        if (includeModules)
        {
            for (final Project module : project.getModules())
            {
                final DirtyReason moduleDirtyReason = module.findDirtyReason(false);
                if (moduleDirtyReason.isDirty())
                {
                    return of(module);
                }
            }
        }
        return absent();
    }

    private final InternalApi internalApi;
    private final FileHashGenerator fileHashGenerator;
    private final SnapshotDetector snapshotDetector;
    private final ArtifactDetector artifactDetector;
    private final GavGenerator gavGenerator;
    private final SourceFilesHashGenerator sourceFilesHashGenerator;

    /**
     * Creates a new {@code DefaultDirtDetector}.
     *
     * @param internalApi
     *            the {@code InternalApi} instance.
     */
    @Inject
    public DefaultDirtDetector(
            final InternalApi internalApi)
    {
        this.internalApi = internalApi;
        artifactDetector = internalApi.getArtifactDetector();
        fileHashGenerator = internalApi.getFileHashGenerator();
        gavGenerator = internalApi.getGavGenerator();
        snapshotDetector = internalApi.getSnapshotDetector();
        sourceFilesHashGenerator = internalApi.getSourceFilesHashGenerator();
    }

    @Override
    public DirtyReason findDirtyReason(
            final Logger logger,
            final MavenSession session,
            final MavenProject mavenProject,
            final GavToProjectMap gavToProjectMap,
            final boolean includeModules)
    {
        if (!snapshotDetector.isSnapshot(mavenProject) && artifactDetector.hasArtifactInRemoteRepository(mavenProject))
        {
            return PUBLISHED;
        }
        final File targetDir = new File(mavenProject.getBuild().getDirectory());
        final boolean targetDirExists = targetDir.exists();
        final boolean artifactInLocalRepository = artifactDetector.hasArtifactInLocalRepository(session, mavenProject);
        final File projectHashFile = new File(targetDir, SOURCE_FILES_LISTING);
        final boolean projectHashFileExists = projectHashFile.exists();
        if (!targetDirExists)
        {
            return NO_BUILD_DIRECTORY;
        }
        if (!artifactInLocalRepository)
        {
            return NOT_IN_LOCAL_REPO;
        }
        if (!projectHashFileExists)
        {
            return NO_HASH_FILE;
        }
        final Digest currentProjectHash = fileHashGenerator.generate(projectHashFile);
        final Project project = gavToProjectMap.getProject(gavGenerator.getGav(mavenProject));
        final Digest projectHash = sourceFilesHashGenerator.generate(project);
        if (!isEqual(currentProjectHash, projectHash))
        {
            return CHANGES_DETECTED;
        }
        final Optional<Project> maybeFirstDirtyDependency = hasDirtyDependency(project, includeModules);
        if (maybeFirstDirtyDependency.isPresent())
        {
            return internalApi.newDirtyReason(true, maybeFirstDirtyDependency.get().getGav().toString());
        }
        return NOT_DIRTY;
    }
}

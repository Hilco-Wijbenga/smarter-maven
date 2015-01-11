package org.cavebeetle.maven.impl;

import static com.google.common.collect.Lists.newArrayList;
import static java.io.File.createTempFile;
import static java.lang.System.getProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code DefaultArtifactDetector}.
 */
public final class DefaultArtifactDetectorTest
{
    private MavenSession mockMavenSession;
    private MavenProject mockMavenProject;
    private DefaultArtifactDetector artifactDetector;
    private Artifact mockArtifact;
    private List<ArtifactRepository> remoteArtifactRepositories;
    private ArtifactRepository mockArtifactRepository;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        mockMavenSession = mock(MavenSession.class);
        mockMavenProject = mock(MavenProject.class);
        artifactDetector = new DefaultArtifactDetector();
        mockArtifact = mock(Artifact.class);
        remoteArtifactRepositories = newArrayList();
        mockArtifactRepository = mock(ArtifactRepository.class);
        remoteArtifactRepositories.add(mockArtifactRepository);
        when(mockMavenProject.getArtifact()).thenReturn(mockArtifact);
        when(mockMavenSession.getLocalRepository()).thenReturn(mockArtifactRepository);
        when(mockMavenProject.getRemoteArtifactRepositories()).thenReturn(remoteArtifactRepositories);
    }

    /**
     * Tests that a missing {@code MavenVersion} in {@code ArtifactDetector#hasArtifactInLocalRepository} is handled
     * correctly.
     */
    @Test
    public final void a_missing_MavenVersion_in_ArtifactDetector_hasArtifactInLocalRepository_is_handled_correctly()
    {
        try
        {
            artifactDetector.hasArtifactInLocalRepository(null, mockMavenProject);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'mavenSession'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code MavenProject} in {@code ArtifactDetector#hasArtifactInLocalRepository} is handled
     * correctly.
     */
    @Test
    public final void a_missing_MavenProject_in_ArtifactDetector_hasArtifactInLocalRepository_is_handled_correctly()
    {
        try
        {
            artifactDetector.hasArtifactInLocalRepository(mockMavenSession, null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'mavenProject'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code MavenProject} in {@code ArtifactDetector#hasArtifactInRemoteRepository} is handled
     * correctly.
     */
    @Test
    public final void a_missing_MavenProject_in_ArtifactDetector_hasArtifactInRemoteRepository_is_handled_correctly()
    {
        try
        {
            artifactDetector.hasArtifactInRemoteRepository(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'mavenProject'.", e.getMessage());
        }
    }

    /**
     * Tests that an artifact that does not exist is not found.
     */
    @Test
    public final void an_artifact_that_does_not_exist_is_not_found()
    {
        final String localRepository = getProperty("java.io.tmpdir");
        when(mockArtifactRepository.getBasedir()).thenReturn(localRepository);
        when(mockArtifactRepository.pathOf(mockArtifact)).thenReturn(localRepository + "/does-not-exist");
        assertFalse(artifactDetector.hasArtifactInLocalRepository(mockMavenSession, mockMavenProject));
    }

    /**
     * Tests that an artifact that exists is found.
     */
    @Test
    public final void an_artifact_that_exists_is_found()
    {
        final String localRepository = getProperty("java.io.tmpdir");
        final File artifactFile = createRandomJar(localRepository);
        when(mockArtifactRepository.getBasedir()).thenReturn(localRepository);
        when(mockArtifactRepository.pathOf(mockArtifact)).thenReturn(artifactFile.getName());
        assertTrue(artifactDetector.hasArtifactInLocalRepository(mockMavenSession, mockMavenProject));
    }

    /**
     * Tests that an artifact that is not available is not found.
     */
    @Test
    public final void an_artifact_that_is_not_available_is_not_found()
    {
        when(mockArtifact.getVersion()).thenReturn("4");
        final List<String> artifactVersions = newArrayList("1", "2", "3");
        when(mockArtifactRepository.findVersions(mockArtifact)).thenReturn(artifactVersions);
        assertFalse(artifactDetector.hasArtifactInRemoteRepository(mockMavenProject));
    }

    /**
     * Tests that an artifact that is available is found.
     */
    @Test
    public final void an_artifact_that_is_available_is_found()
    {
        when(mockArtifact.getVersion()).thenReturn("3");
        final List<String> artifactVersions = newArrayList("1", "2", "3");
        when(mockArtifactRepository.findVersions(mockArtifact)).thenReturn(artifactVersions);
        assertTrue(artifactDetector.hasArtifactInRemoteRepository(mockMavenProject));
    }

    private File createRandomJar(
            final String localRepository)
    {
        try
        {
            final File localRepositoryDir = new File(localRepository);
            final File artifactFile = createTempFile("artifact-", ".jar", localRepositoryDir);
            return artifactFile;
        }
        catch (final IOException e)
        {
            throw new IllegalStateException(e);
        }
    }
}

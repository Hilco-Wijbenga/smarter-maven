package org.cavebeetle.maven.impl;

import static com.google.common.collect.Lists.newArrayList;
import static org.cavebeetle.maven.DirtyReason.CHANGES_DETECTED;
import static org.cavebeetle.maven.DirtyReason.NOT_DIRTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.cavebeetle.maven.Gav;
import org.cavebeetle.maven.GavGenerator;
import org.cavebeetle.maven.GavToProjectMap;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.MavenExecutionListener;
import org.cavebeetle.maven.Project;
import org.codehaus.plexus.logging.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code DefaultAfterProjectsReadInternal}.
 */
public final class DefaultAfterProjectsReadInternalTest
{
    private static final class Tuple
    {
        public final Gav GAV;
        public final Project PROJECT;

        public Tuple(
                final Gav gav,
                final Project project)
        {
            GAV = gav;
            PROJECT = project;
        }
    }

    private InternalApi mockInternalApi;
    private GavGenerator mockGavGenerator;
    private Logger mockLogger;
    private MavenSession mockMavenSession;
    private ProjectBuilder mockProjectBuilder;
    private GavToProjectMap mockGavToProjectMap;
    private MavenExecutionListener mockMavenExecutionListener;
    private DefaultAfterProjectsReadInternal afterProjectsRead;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        mockInternalApi = mock(InternalApi.class);
        mockGavGenerator = mock(GavGenerator.class);
        when(mockInternalApi.getGavGenerator()).thenReturn(mockGavGenerator);
        mockLogger = mock(Logger.class);
        mockMavenSession = mock(MavenSession.class);
        mockProjectBuilder = mock(ProjectBuilder.class);
        mockGavToProjectMap = mock(GavToProjectMap.class);
        when(mockInternalApi.newGavToProjectMap()).thenReturn(mockGavToProjectMap);
        mockMavenExecutionListener = mock(MavenExecutionListener.class);
        when(mockInternalApi.getMavenExecutionListener()).thenReturn(mockMavenExecutionListener);
        afterProjectsRead = new DefaultAfterProjectsReadInternal(mockInternalApi);
    }

    /**
     * Tests that a missing {@code InternalApi} is handled correctly.
     */
    @Test
    public final void a_missing_InternalApi_is_handled_correctly()
    {
        try
        {
            new DefaultAfterProjectsReadInternal(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'internalApi'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code Logger} in {@code AfterProjectsReadInternal#initializeGavToProjectMap} is handled
     * correctly.
     */
    @Test
    public final void a_missing_Logger_in_AfterProjectsReadInternal_initializeGavToProjectMap_is_handled_correctly()
    {
        try
        {
            afterProjectsRead.initializeGavToProjectMap(null, mockMavenSession, mockProjectBuilder);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'logger'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code MavenSession} in {@code AfterProjectsReadInternal#initializeGavToProjectMap} is
     * handled correctly.
     */
    @Test
    public final void a_missing_MavenSession_in_AfterProjectsReadInternal_initializeGavToProjectMap_is_handled_correctly()
    {
        try
        {
            afterProjectsRead.initializeGavToProjectMap(mockLogger, null, mockProjectBuilder);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'mavenSession'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code ProjectBuilder} in {@code AfterProjectsReadInternal#initializeGavToProjectMap} is
     * handled correctly.
     */
    @Test
    public final void a_missing_ProjectBuilder_in_AfterProjectsReadInternal_initializeGavToProjectMap_is_handled_correctly()
    {
        try
        {
            afterProjectsRead.initializeGavToProjectMap(mockLogger, mockMavenSession, null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'projectBuilder'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code Logger} in {@code AfterProjectsReadInternal#getMavenExecutionRequest} is handled
     * correctly.
     */
    @Test
    public final void a_missing_Logger_in_AfterProjectsReadInternal_getMavenExecutionRequest_is_handled_correctly()
    {
        try
        {
            afterProjectsRead.getMavenExecutionRequest(null, mockMavenSession, mockGavToProjectMap);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'logger'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code MavenSession} in {@code AfterProjectsReadInternal#getMavenExecutionRequest} is
     * handled correctly.
     */
    @Test
    public final void a_missing_MavenSession_in_AfterProjectsReadInternal_getMavenExecutionRequest_is_handled_correctly()
    {
        try
        {
            afterProjectsRead.getMavenExecutionRequest(mockLogger, null, mockGavToProjectMap);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'mavenSession'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code GavToProjectMap} in {@code AfterProjectsReadInternal#getMavenExecutionRequest} is
     * handled correctly.
     */
    @Test
    public final void a_missing_GavToProjectMap_in_AfterProjectsReadInternal_getMavenExecutionRequest_is_handled_correctly()
    {
        try
        {
            afterProjectsRead.getMavenExecutionRequest(mockLogger, mockMavenSession, null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'gavToProjectMap'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code MavenSession} in {@code AfterProjectsReadInternal#collectDirtyProjects} is handled
     * correctly.
     */
    @Test
    public final void a_missing_MavenSession_in_AfterProjectsReadInternal_collectDirtyProjects_is_handled_correctly()
    {
        try
        {
            afterProjectsRead.collectDirtyProjects(mockLogger, null, mockGavToProjectMap);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'mavenSession'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code GavToProjectMap} in {@code AfterProjectsReadInternal#collectDirtyProjects} is handled
     * correctly.
     */
    @Test
    public final void a_missing_GavToProjectMap_in_AfterProjectsReadInternal_collectDirtyProjects_is_handled_correctly()
    {
        try
        {
            afterProjectsRead.collectDirtyProjects(mockLogger, mockMavenSession, null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'gavToProjectMap'.", e.getMessage());
        }
    }

    /**
     * Tests that the {@code GavToProjectMap} is initialized correctly with 1 available {@code MavenProject}.
     */
    @Test
    public final void the_GavToProjectMap_is_initialized_correctly_with_1_available_MavenProject()
    {
        final List<Tuple> tuples = init(1);
        final GavToProjectMap gavToProjectMap =
            afterProjectsRead.initializeGavToProjectMap(mockLogger, mockMavenSession, mockProjectBuilder);
        verifyResult(tuples, gavToProjectMap);
    }

    /**
     * Tests that the {@code GavToProjectMap} is initialized correctly with 2 available {@code MavenProject}s.
     */
    @Test
    public final void the_GavToProjectMap_is_initialized_correctly_with_2_available_MavenProjects()
    {
        final List<Tuple> tuples = init(2);
        final GavToProjectMap gavToProjectMap =
            afterProjectsRead.initializeGavToProjectMap(mockLogger, mockMavenSession, mockProjectBuilder);
        verifyResult(tuples, gavToProjectMap);
    }

    /**
     * Tests that the {@code GavToProjectMap} is initialized correctly with 3 available {@code MavenProject}s.
     */
    @Test
    public final void the_GavToProjectMap_is_initialized_correctly_with_3_available_MavenProjects()
    {
        final List<Tuple> tuples = init(3);
        final GavToProjectMap gavToProjectMap =
            afterProjectsRead.initializeGavToProjectMap(mockLogger, mockMavenSession, mockProjectBuilder);
        verifyResult(tuples, gavToProjectMap);
    }

    /**
     * Tests that the {@code MavenExecutionListener} and {@code MavenExecutionRequest} are initialized correctly.
     */
    @Test
    public final void the_MavenExecutionListener_and_MavenExecutionRequest_are_initialized_correctly()
    {
        final MavenExecutionRequest mockMavenExecutionRequest = mock(MavenExecutionRequest.class);
        when(mockMavenSession.getRequest()).thenReturn(mockMavenExecutionRequest);
        final MavenExecutionRequest mavenExecutionRequest =
            afterProjectsRead.getMavenExecutionRequest(mockLogger, mockMavenSession, mockGavToProjectMap);
        assertSame(mockMavenExecutionRequest, mavenExecutionRequest);
        verify(mockMavenExecutionListener).init(mockLogger, mockMavenSession, mockGavToProjectMap);
        verify(mockMavenExecutionRequest).setExecutionListener(mockMavenExecutionListener);
    }

    /**
     * Tests that collecting dirty projects works correctly.
     */
    @Test
    public final void collecting_dirty_projects_works_correctly()
    {
        final List<Gav> mavenProjectGavs = newArrayList();
        final List<MavenProject> mavenProjects = newArrayList();
        for (int i = 0; i < 4; i++)
        {
            final MavenProject mockMavenProject = mock(MavenProject.class);
            final Gav mockProjectGav = mock(Gav.class);
            when(mockGavGenerator.getGav(mockMavenProject)).thenReturn(mockProjectGav);
            final Project mockProject = mock(Project.class);
            when(mockProject.findDirtyReason(eq(true))).thenReturn(i % 2 == 0 ? CHANGES_DETECTED : NOT_DIRTY);
            when(mockGavToProjectMap.getProject(mockProjectGav)).thenReturn(mockProject);
            mavenProjectGavs.add(mockProjectGav);
            mavenProjects.add(mockMavenProject);
        }
        when(mockGavToProjectMap.iterator()).thenReturn(mavenProjectGavs.iterator());
        when(mockMavenSession.getProjects()).thenReturn(mavenProjects);
        final List<MavenProject> dirtyProjects =
            afterProjectsRead.collectDirtyProjects(mockLogger, mockMavenSession, mockGavToProjectMap);
        assertEquals(2, dirtyProjects.size());
        assertSame(mavenProjects.get(0), dirtyProjects.get(0));
        assertSame(mavenProjects.get(2), dirtyProjects.get(1));
    }

    /**
     * Tests that creating a dummy Maven project works correctly.
     */
    @Test
    public final void test_dummy()
    {
        final MavenProject dummyMavenProject = afterProjectsRead.createDummyProjectToIndicateNothingToDo();
        assertEquals("nothing", dummyMavenProject.getArtifactId());
        assertEquals("(everything is up-to-date).", dummyMavenProject.getVersion());
    }

    private List<Tuple> init(
            final int count)
    {
        final List<MavenProject> mavenProjects = createMavenProjects(count);
        when(mockMavenSession.getProjects()).thenReturn(mavenProjects);
        final List<Tuple> tuples = createTuples(mavenProjects);
        return tuples;
    }

    private void verifyResult(
            final List<Tuple> tuples,
            final GavToProjectMap gavToProjectMap)
    {
        assertSame(mockGavToProjectMap, gavToProjectMap);
        for (final Tuple tuple : tuples)
        {
            verify(mockGavToProjectMap).putProject(tuple.GAV, tuple.PROJECT);
        }
    }

    private List<MavenProject> createMavenProjects(
            final int count)
    {
        final List<MavenProject> mockMavenProjects = newArrayList();
        for (int i = 0; i < count; i++)
        {
            final MavenProject mockMavenProject = mock(MavenProject.class);
            mockMavenProjects.add(mockMavenProject);
        }
        return mockMavenProjects;
    }

    private List<Tuple> createTuples(
            final List<MavenProject> mavenProjects)
    {
        final List<Tuple> tuples = newArrayList();
        for (final MavenProject mavenProject : mavenProjects)
        {
            final Tuple tuple = mockProject(mavenProject);
            tuples.add(tuple);
        }
        return tuples;
    }

    private Tuple mockProject(
            final MavenProject mockMavenProject)
    {
        final Gav mockProjectGav = mock(Gav.class);
        when(mockGavGenerator.getGav(mockMavenProject)).thenReturn(mockProjectGav);
        final Project mockProject = mock(Project.class);
        when(
                mockInternalApi
                        .newProject(mockLogger,
                                mockMavenSession,
                                mockProjectBuilder,
                                mockMavenProject,
                                mockGavToProjectMap))
                .thenReturn(mockProject);
        return new Tuple(mockProjectGav, mockProject);
    }
}

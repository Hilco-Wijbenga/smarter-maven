package org.cavebeetle.maven.impl;

import static com.google.common.base.Optional.of;
import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import org.apache.maven.BuildAbort;
import org.apache.maven.execution.MavenExecutionRequest;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.cavebeetle.maven.ActiveDetector;
import org.cavebeetle.maven.AfterProjectsReadInternal;
import org.cavebeetle.maven.GavToProjectMap;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.InvalidProjectHierarchyDetector;
import org.codehaus.plexus.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import com.google.common.base.Optional;

/**
 * The unit tests for {@code DefaultAfterProjectsReadInternal}.
 */
public final class DefaultAfterProjectsReadTest
{
    private Logger mockLogger;
    private RuntimeInformation mockRuntimeInformation;
    private MavenSession mockMavenSession;
    private ProjectBuilder mockProjectBuilder;
    private ActiveDetector mockActiveDetector;
    private InternalApi mockInternalApi;
    private GavToProjectMap mockGavToProjectMap;
    private AfterProjectsReadInternal mockAfterProjectsReadInternal;
    private InvalidProjectHierarchyDetector mockInvalidProjectHierarchyDetector;
    private DefaultAfterProjectsRead afterProjectsRead;
    private MavenExecutionRequest mockMavenExecutionRequest;
    private List<String> selectedProjects;

    /**
     * Sets up each unit test.
     */
    @SuppressWarnings("boxing")
    @Before
    public void setUp()
    {
        mockLogger = mock(Logger.class);
        mockRuntimeInformation = mock(RuntimeInformation.class);
        mockMavenSession = mock(MavenSession.class);
        mockProjectBuilder = mock(ProjectBuilder.class);
        mockActiveDetector = mock(ActiveDetector.class);
        mockInternalApi = mock(InternalApi.class);
        Mockito.when(mockInternalApi.getActiveDetector()).thenReturn(mockActiveDetector);
        Mockito.when(mockActiveDetector.isSmarterMavenActive(Matchers.any(MavenSession.class))).thenReturn(true);
        Mockito.when(mockActiveDetector.showBanner(Matchers.any(MavenSession.class))).thenReturn(true);
        mockGavToProjectMap = mock(GavToProjectMap.class);
        mockAfterProjectsReadInternal = mock(AfterProjectsReadInternal.class);
        when(mockInternalApi.getAfterProjectsReadInternal()).thenReturn(mockAfterProjectsReadInternal);
        mockInvalidProjectHierarchyDetector = mock(InvalidProjectHierarchyDetector.class);
        when(mockInternalApi.getInvalidProjectHierarchyDetector()).thenReturn(mockInvalidProjectHierarchyDetector);
        afterProjectsRead = new DefaultAfterProjectsRead(mockInternalApi);
        when(mockAfterProjectsReadInternal.initializeGavToProjectMap(mockLogger, mockMavenSession, mockProjectBuilder))
                .thenReturn(mockGavToProjectMap);
        mockMavenExecutionRequest = mock(MavenExecutionRequest.class);
        selectedProjects = newArrayList();
        when(mockMavenExecutionRequest.getSelectedProjects())
                .thenReturn(selectedProjects);
        when(mockAfterProjectsReadInternal.getMavenExecutionRequest(mockLogger, mockMavenSession, mockGavToProjectMap))
                .thenReturn(mockMavenExecutionRequest);
    }

    /**
     * Tests that a missing {@code AfterProjectsReadInternal} instance is handled correctly.
     */
    @Test
    public final void a_missing_AfterProjectsReadInternal_instance_is_handled_correctly()
    {
        try
        {
            new DefaultAfterProjectsRead(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals(e.getMessage(), "Missing 'internalApi'.");
        }
    }

    /**
     * Tests that a missing {@code Logger} instance is handled correctly.
     */
    @Test
    public final void a_missing_Logger_instance_is_handled_correctly()
    {
        try
        {
            afterProjectsRead.afterProjectsRead(null, mockRuntimeInformation, mockMavenSession, mockProjectBuilder);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals(e.getMessage(), "Missing 'logger'.");
        }
    }

    /**
     * Tests that a missing {@code RuntimeInformation} instance is handled correctly.
     */
    @Test
    public final void a_missing_RuntimeInformation_instance_is_handled_correctly()
    {
        try
        {
            afterProjectsRead.afterProjectsRead(mockLogger, null, mockMavenSession, mockProjectBuilder);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals(e.getMessage(), "Missing 'runtimeInformation'.");
        }
    }

    /**
     * Tests that a missing {@code MavenSession} instance is handled correctly.
     */
    @Test
    public final void a_missing_MavenSession_instance_is_handled_correctly()
    {
        try
        {
            afterProjectsRead.afterProjectsRead(mockLogger, mockRuntimeInformation, null, mockProjectBuilder);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals(e.getMessage(), "Missing 'mavenSession'.");
        }
    }

    /**
     * Tests that a missing {@code ProjectBuilder} instance is handled correctly.
     */
    @Test
    public final void a_missing_ProjectBuilder_instance_is_handled_correctly()
    {
        try
        {
            afterProjectsRead.afterProjectsRead(mockLogger, mockRuntimeInformation, mockMavenSession, null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals(e.getMessage(), "Missing 'projectBuilder'.");
        }
    }

    /**
     * Tests that the Smarter Maven extension aborts the build if an invalid project hierarchy is detected.
     */
    @Test
    public final void the_Smarter_Maven_extension_aborts_the_build_if_an_invalid_project_hierarchy_is_detected()
    {
        selectedProjects.add("one");
        final String errorMessage = "an error";
        when(mockInvalidProjectHierarchyDetector.getInvalidProjectHierarchyError(mockGavToProjectMap))
                .thenReturn(of(errorMessage));
        try
        {
            afterProjectsRead.afterProjectsRead(mockLogger, mockRuntimeInformation, mockMavenSession, mockProjectBuilder);
        }
        catch (final BuildAbort e)
        {
            assertSame(errorMessage, e.getMessage());
        }
    }

    /**
     * Tests that the Smarter Maven extension does nothing when invoked with a list of projects to build.
     */
    @Test
    public final void the_Smarter_Maven_extension_does_nothing_when_invoked_with_a_list_of_projects_to_build()
    {
        selectedProjects.add("one");
        when(mockInvalidProjectHierarchyDetector.getInvalidProjectHierarchyError(mockGavToProjectMap))
                .thenReturn(Optional.<String> absent());
        afterProjectsRead.afterProjectsRead(mockLogger, mockRuntimeInformation, mockMavenSession, mockProjectBuilder);
        verify(mockLogger, never()).info(anyString());
    }

    /**
     * Tests that an extra empty line is logged when dirty projects are found.
     */
    @Test
    public final void an_extra_empty_line_is_logged_when_dirty_projects_are_found()
    {
        final MavenProject dirtyProject = mock(MavenProject.class);
        final List<MavenProject> dirtyProjects = newArrayList(dirtyProject);
        when(mockAfterProjectsReadInternal.collectDirtyProjects(mockLogger, mockMavenSession, mockGavToProjectMap))
                .thenReturn(dirtyProjects);
        when(mockInvalidProjectHierarchyDetector.getInvalidProjectHierarchyError(mockGavToProjectMap))
                .thenReturn(Optional.<String> absent());
        afterProjectsRead.afterProjectsRead(mockLogger, mockRuntimeInformation, mockMavenSession, mockProjectBuilder);
        verify(mockLogger, times(2)).info(eq(""));
        verify(mockMavenSession).setProjects(dirtyProjects);
    }

    /**
     * Tests that a dummy Maven project is added to the list of dirty projects if no dirty projects are found.
     */
    @Test
    public final void a_dummy_Maven_project_is_added_to_the_list_of_dirty_projects_if_no_dirty_projects_are_found()
    {
        final List<MavenProject> dirtyProjects = newArrayList();
        when(mockAfterProjectsReadInternal.collectDirtyProjects(mockLogger, mockMavenSession, mockGavToProjectMap))
                .thenReturn(dirtyProjects);
        final MavenProject mockDummyProject = mock(MavenProject.class);
        when(mockAfterProjectsReadInternal.createDummyProjectToIndicateNothingToDo())
                .thenReturn(mockDummyProject);
        when(mockInvalidProjectHierarchyDetector.getInvalidProjectHierarchyError(mockGavToProjectMap))
                .thenReturn(Optional.<String> absent());
        afterProjectsRead.afterProjectsRead(mockLogger, mockRuntimeInformation, mockMavenSession, mockProjectBuilder);
        verify(mockLogger).info(eq(""));
        verify(mockMavenSession).setProjects(dirtyProjects);
        assertEquals(1, dirtyProjects.size());
        assertSame(mockDummyProject, dirtyProjects.get(0));
    }
}

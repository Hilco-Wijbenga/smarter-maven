package org.cavebeetle.maven2;

import java.util.Set;
import org.cavebeetle.maven2.data.DownstreamProject;
import org.cavebeetle.maven2.data.Project;
import org.cavebeetle.maven2.data.UpstreamProject;
import com.google.common.collect.Sets;

public final class DownstreamProjectFinder
{
    public static StrictMap<Project, Set<DownstreamProject>> makeMap(final ProjectFinder projectFinder_)
    {
        final StrictMap.Mutable<Project, Set<DownstreamProject>> map = StrictMap.Builder.make();
        for (final Project project : projectFinder_)
        {
            map.put(project, Sets.<DownstreamProject> newHashSet());
        }
        return map;
    }

    private final StrictMap<Project, Set<DownstreamProject>> projectToDownstreamProjectsMap;

    public DownstreamProjectFinder(final ProjectFinder projectFinder, final UpstreamProjectFinder upstreamProjectFinder)
    {
        projectToDownstreamProjectsMap = makeMap(projectFinder);
        for (final Project project : projectFinder)
        {
            final Set<UpstreamProject> upstreamProjects = upstreamProjectFinder.get(project);
            for (final UpstreamProject upstreamProject : upstreamProjects)
            {
                final DownstreamProject downstreamProject = DownstreamProject.make(project);
                projectToDownstreamProjectsMap.get(upstreamProject.value()).add(downstreamProject);
            }
        }
    }

    public Set<DownstreamProject> get(final Project project)
    {
        return projectToDownstreamProjectsMap.get(project);
    }
}

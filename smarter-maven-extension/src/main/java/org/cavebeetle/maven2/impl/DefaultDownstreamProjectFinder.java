package org.cavebeetle.maven2.impl;

import java.util.Set;
import org.cavebeetle.maven2.DownstreamProjectFinder;
import org.cavebeetle.maven2.ProjectFinder;
import org.cavebeetle.maven2.UpstreamProjectFinder;
import org.cavebeetle.maven2.data.DownstreamProject;
import org.cavebeetle.maven2.data.Project;
import org.cavebeetle.maven2.data.UpstreamProject;
import org.cavebeetle.maven2.impl.StrictMap.Builder;
import org.cavebeetle.maven2.impl.StrictMap.Mutable;
import com.google.common.collect.Sets;

public final class DefaultDownstreamProjectFinder
        implements
            DownstreamProjectFinder
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

    public DefaultDownstreamProjectFinder(final ProjectFinder projectFinder, final UpstreamProjectFinder upstreamProjectFinder)
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

    @Override
    public Set<DownstreamProject> get(final Project project)
    {
        return projectToDownstreamProjectsMap.get(project);
    }
}

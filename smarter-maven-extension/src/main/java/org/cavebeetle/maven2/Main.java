package org.cavebeetle.maven2;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.PomFile;
import org.cavebeetle.maven2.data.Project;
import org.cavebeetle.maven2.data.UpstreamProject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public final class Main
{
    public static void main(final String[] args)
    {
        final File file = new File("/home/hilco/workspaces/smarter-maven/pom.xml");
        final PomFile rootPomFile = PomFile.make(file);
        final MavenProjectCache mavenProjectCache = new MavenProjectCache();
        final ProjectFinder projectFinder = new ProjectFinder(mavenProjectCache, rootPomFile);
        final UpstreamProjectFinder upstreamProjectFinder = new UpstreamProjectFinder(mavenProjectCache, projectFinder);
        for (final Project project_ : projectFinder)
        {
            System.out.println(String.format("%s -->", projectToText(project_)));
            final Set<UpstreamProject> set = upstreamProjectFinder.get(project_);
            System.out.println("    " + set.size());
            final List<UpstreamProject> upstreamProjects_ = Lists.newArrayList(set);
            Collections.sort(upstreamProjects_);
            final List<UpstreamProject> upstreamProjects = ImmutableList.copyOf(upstreamProjects_);
            for (final UpstreamProject upstreamProject : upstreamProjects)
            {
                System.out.println(String.format("    %s (%s)", projectToText(upstreamProject.value()), upstreamProject.upstreamReason()));
            }
        }
        final StrictMap.Mutable<Project, Set<Project>> projectToDownstreamProjectsMap_ = StrictMap.Builder.make();
        for (final Project project : projectFinder)
        {
            projectToDownstreamProjectsMap_.put(project, Sets.<Project> newHashSet());
        }
        final StrictMap<Project, Set<Project>> projectToDownstreamProjectsMap = projectToDownstreamProjectsMap_.freeze();
        for (final Project project : projectFinder)
        {
            final Set<UpstreamProject> upstreamProjects = upstreamProjectFinder.get(project);
            for (final UpstreamProject upstreamProject : upstreamProjects)
            {
                projectToDownstreamProjectsMap.get(upstreamProject.value()).add(project);
            }
        }
        System.out.println();
        System.out.println("Downstream Projects");
        System.out.println("-------------------");
        for (final Project project_ : projectToDownstreamProjectsMap)
        {
            System.out.println(String.format("%s -->", projectToText(project_)));
            final List<Project> downstreamProjects_ = Lists.newArrayList(projectToDownstreamProjectsMap.get(project_));
            Collections.sort(downstreamProjects_);
            final List<Project> downstreamProjects = ImmutableList.copyOf(downstreamProjects_);
            for (final Project downstreamProject : downstreamProjects)
            {
                System.out.println(String.format("    %s", projectToText(downstreamProject)));
            }
        }
    }

    public static final String projectToText(final Project project)
    {
        final Gav gav = project.gav();
        return String.format(
                "%s:%s:%s:%s",
                gav.groupId().value(),
                gav.artifactId().value(),
                project.packaging().value(),
                gav.version().value());
    }
}

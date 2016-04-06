package org.cavebeetle.maven2;

import java.io.File;
import java.util.Collections;
import java.util.List;
import org.cavebeetle.maven2.data.DownstreamProject;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.PomFile;
import org.cavebeetle.maven2.data.Project;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public final class Main
{
    public static void main(final String[] args)
    {
        final File file = new File("/home/hilco/workspaces/smarter-maven/pom.xml");
        final PomFile rootPomFile = PomFile.make(file);
        final MavenProjectCache mavenProjectCache = new MavenProjectCache();
        final ProjectFinder projectFinder = new ProjectFinder(mavenProjectCache, rootPomFile);
        final UpstreamProjectFinder upstreamProjectFinder = new UpstreamProjectFinder(mavenProjectCache, projectFinder);
        final DownstreamProjectFinder downstreamProjectFinder = new DownstreamProjectFinder(projectFinder, upstreamProjectFinder);
        for (final Project project_ : projectFinder)
        {
            System.out.println(String.format("%s -->", projectToText(project_)));
            final List<DownstreamProject> downstreamProjects_ = Lists.newArrayList(downstreamProjectFinder.get(project_));
            Collections.sort(downstreamProjects_);
            final List<DownstreamProject> downstreamProjects = ImmutableList.copyOf(downstreamProjects_);
            for (final DownstreamProject downstreamProject : downstreamProjects)
            {
                System.out.println(String.format("    %s (%s)", projectToText(downstreamProject.value()), downstreamProject.reason()));
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

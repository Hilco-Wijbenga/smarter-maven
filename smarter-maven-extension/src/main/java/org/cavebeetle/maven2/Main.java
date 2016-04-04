package org.cavebeetle.maven2;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.PomFile;
import org.cavebeetle.maven2.data.Project;
import org.cavebeetle.maven2.data.UpstreamProject;
import org.cavebeetle.maven2.data.UpstreamReason;
import com.google.common.base.Optional;
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
        final StrictMap.Mutable<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap = StrictMap.Builder.make();
        for (final Project project : projectFinder)
        {
            projectToUpstreamProjectsMap.put(project, Sets.<UpstreamProject> newHashSet());
            final MavenProject mavenProject = mavenProjectCache.get(project.pomFile());
            addParentToUpstreamProjects(projectFinder, projectToUpstreamProjectsMap, project, mavenProject);
            addModulesToUpstreamProjects(projectFinder, mavenProjectCache, projectToUpstreamProjectsMap, project, mavenProject);
            addDependenciesToUpstreamProjects(projectFinder, projectToUpstreamProjectsMap, project, mavenProject);
            addPluginsAndPluginDependenciesToUpstreamProjects(projectFinder, projectToUpstreamProjectsMap, project, mavenProject);
            addExtensionsToUpstreamProjects(projectFinder, projectToUpstreamProjectsMap, project, mavenProject);
        }
        System.out.println();
        System.out.println("Upstream Projects");
        System.out.println("-----------------");
        for (final Project project_ : projectToUpstreamProjectsMap)
        {
            System.out.println(String.format("%s -->", projectToText(project_)));
            final Set<UpstreamProject> set = projectToUpstreamProjectsMap.get(project_);
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
            final Set<UpstreamProject> upstreamProjects = projectToUpstreamProjectsMap.get(project);
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

    public static void addParentToUpstreamProjects(
            final ProjectFinder projectFinder,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project,
            final MavenProject mavenProject)
    {
        final Optional<Parent> maybeParent = Optional.fromNullable(mavenProject.getModel().getParent());
        if (maybeParent.isPresent())
        {
            final Parent parent = maybeParent.get();
            addToUpstreamProjects(projectFinder, GavMapper.PARENT_TO_GAV, parent, projectToUpstreamProjectsMap, project, UpstreamReason.PARENT);
        }
    }

    public static void addModulesToUpstreamProjects(
            final ProjectFinder projectFinder,
            final MavenProjectCache mavenProjectCache,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project,
            final MavenProject mavenProject)
    {
        for (final String module : mavenProject.getModules())
        {
            final PomFile modulePomFile = ProjectFinder.getPomFile(project.pomFile(), module);
            final MavenProject moduleMavenProject = mavenProjectCache.get(modulePomFile);
            addToUpstreamProjects(projectFinder, GavMapper.MAVEN_PROJECT_TO_GAV, moduleMavenProject, projectToUpstreamProjectsMap, project, UpstreamReason.MODULE);
        }
    }

    public static void addDependenciesToUpstreamProjects(
            final ProjectFinder projectFinder,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project,
            final MavenProject mavenProject)
    {
        for (final Dependency dependency : mavenProject.getDependencies())
        {
            addToUpstreamProjects(projectFinder, GavMapper.DEPENDENCY_TO_GAV, dependency, projectToUpstreamProjectsMap, project, UpstreamReason.DEPENDENCY);
        }
    }

    public static void addPluginsAndPluginDependenciesToUpstreamProjects(
            final ProjectFinder projectFinder,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project,
            final MavenProject mavenProject)
    {
        for (final Plugin plugin : mavenProject.getBuild().getPlugins())
        {
            addToUpstreamProjects(projectFinder, GavMapper.PLUGIN_TO_GAV, plugin, projectToUpstreamProjectsMap, project, UpstreamReason.PLUGIN);
            for (final Dependency pluginDependency : plugin.getDependencies())
            {
                addToUpstreamProjects(projectFinder, GavMapper.DEPENDENCY_TO_GAV, pluginDependency, projectToUpstreamProjectsMap, project, UpstreamReason.PLUGIN_DEPENDENCY);
            }
        }
    }

    public static void addExtensionsToUpstreamProjects(
            final ProjectFinder projectFinder,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project,
            final MavenProject mavenProject)
    {
        for (final Extension extension : mavenProject.getBuild().getExtensions())
        {
            addToUpstreamProjects(projectFinder, GavMapper.EXTENSION_TO_GAV, extension, projectToUpstreamProjectsMap, project, UpstreamReason.EXTENSION);
        }
    }

    @SuppressWarnings("boxing")
    public static final <SOURCE> void addToUpstreamProjects(
            final ProjectFinder projectFinder,
            final GavMapper<SOURCE> gavMapper,
            final SOURCE source,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project,
            final UpstreamReason upstreamReason)
    {
        final Gav gav = gavMapper.map(source);
        final Optional<Project> maybeProject = projectFinder.getProject(gav);
        if (maybeProject.isPresent())
        {
            final UpstreamProject upstreamProject = UpstreamProject.make(maybeProject.get(), upstreamReason);
            projectToUpstreamProjectsMap.get(project).add(upstreamProject);
            System.out.println(
                    String.format(
                            "##### %s - Add UpstreamProject %s:%s [%d]",
                            projectToText(project),
                            projectToText(upstreamProject.value()),
                            upstreamProject.upstreamReason(),
                            projectToUpstreamProjectsMap.get(project).size()));
        }
    }
}

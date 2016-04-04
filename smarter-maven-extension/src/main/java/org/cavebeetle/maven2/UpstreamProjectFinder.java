package org.cavebeetle.maven2;

import static org.cavebeetle.maven2.GavMapper.DEPENDENCY_TO_GAV;
import static org.cavebeetle.maven2.GavMapper.EXTENSION_TO_GAV;
import static org.cavebeetle.maven2.GavMapper.MAVEN_PROJECT_TO_GAV;
import static org.cavebeetle.maven2.GavMapper.PARENT_TO_GAV;
import static org.cavebeetle.maven2.GavMapper.PLUGIN_TO_GAV;
import static org.cavebeetle.maven2.data.UpstreamReason.DEPENDENCY;
import static org.cavebeetle.maven2.data.UpstreamReason.EXTENSION;
import static org.cavebeetle.maven2.data.UpstreamReason.MODULE;
import static org.cavebeetle.maven2.data.UpstreamReason.PARENT;
import static org.cavebeetle.maven2.data.UpstreamReason.PLUGIN;
import static org.cavebeetle.maven2.data.UpstreamReason.PLUGIN_DEPENDENCY;
import java.util.Set;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.Module;
import org.cavebeetle.maven2.data.Project;
import org.cavebeetle.maven2.data.UpstreamProject;
import org.cavebeetle.maven2.data.UpstreamReason;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;

public final class UpstreamProjectFinder
{
    private final MavenProjectCache mavenProjectCache;
    private final ProjectFinder projectFinder;
    private final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap;

    public UpstreamProjectFinder(final MavenProjectCache mavenProjectCache, final ProjectFinder projectFinder)
    {
        this.mavenProjectCache = mavenProjectCache;
        this.projectFinder = projectFinder;
        final StrictMap.Mutable<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap_ = StrictMap.Builder.make();
        for (final Project project : projectFinder)
        {
            projectToUpstreamProjectsMap_.put(project, Sets.<UpstreamProject> newHashSet());
        }
        projectToUpstreamProjectsMap = projectToUpstreamProjectsMap_;
        initialize();
    }

    public Set<UpstreamProject> get(final Project project)
    {
        return projectToUpstreamProjectsMap.get(project);
    }

    public void initialize()
    {
        for (final Project project : projectFinder)
        {
            final MavenProject mavenProject = mavenProjectCache.get(project.pomFile());
            final Set<UpstreamProject> upstreamProjects = projectToUpstreamProjectsMap.get(project);
            addParentToUpstreamProjects(upstreamProjects, mavenProject);
            addModulesToUpstreamProjects(upstreamProjects, project, mavenProject);
            addDependenciesToUpstreamProjects(upstreamProjects, mavenProject);
            addPluginsAndDependenciesToUpstreamProjects(upstreamProjects, mavenProject);
            addExtensionsToUpstreamProjects(upstreamProjects, mavenProject);
        }
    }

    public void addParentToUpstreamProjects(
            final Set<UpstreamProject> upstreamProjects,
            final MavenProject mavenProject)
    {
        final Optional<Parent> maybeParent = Optional.fromNullable(mavenProject.getModel().getParent());
        if (maybeParent.isPresent())
        {
            final Parent parent = maybeParent.get();
            addToUpstreamProjects(PARENT_TO_GAV, parent, upstreamProjects, PARENT);
        }
    }

    public void addModulesToUpstreamProjects(
            final Set<UpstreamProject> upstreamProjects,
            final Project project,
            final MavenProject mavenProject)
    {
        for (final String moduleName : mavenProject.getModules())
        {
            final Module module = Module.make(project.pomFile(), moduleName);
            final MavenProject moduleMavenProject = mavenProjectCache.get(module.value());
            addToUpstreamProjects(MAVEN_PROJECT_TO_GAV, moduleMavenProject, upstreamProjects, MODULE);
        }
    }

    public void addDependenciesToUpstreamProjects(
            final Set<UpstreamProject> upstreamProjects,
            final MavenProject mavenProject)
    {
        for (final Dependency dependency : mavenProject.getDependencies())
        {
            addToUpstreamProjects(DEPENDENCY_TO_GAV, dependency, upstreamProjects, DEPENDENCY);
        }
    }

    public void addPluginsAndDependenciesToUpstreamProjects(
            final Set<UpstreamProject> upstreamProjects,
            final MavenProject mavenProject)
    {
        for (final Plugin plugin : mavenProject.getBuild().getPlugins())
        {
            addToUpstreamProjects(PLUGIN_TO_GAV, plugin, upstreamProjects, PLUGIN);
            for (final Dependency pluginDependency : plugin.getDependencies())
            {
                addToUpstreamProjects(DEPENDENCY_TO_GAV, pluginDependency, upstreamProjects, PLUGIN_DEPENDENCY);
            }
        }
    }

    public void addExtensionsToUpstreamProjects(
            final Set<UpstreamProject> upstreamProjects,
            final MavenProject mavenProject)
    {
        for (final Extension extension : mavenProject.getBuild().getExtensions())
        {
            addToUpstreamProjects(EXTENSION_TO_GAV, extension, upstreamProjects, EXTENSION);
        }
    }

    public final <SOURCE> void addToUpstreamProjects(
            final GavMapper<SOURCE> gavMapper,
            final SOURCE source,
            final Set<UpstreamProject> upstreamProjects,
            final UpstreamReason upstreamReason)
    {
        final Gav gav = gavMapper.map(source);
        final Optional<Project> maybeProject = projectFinder.getProject(gav);
        if (maybeProject.isPresent())
        {
            final UpstreamProject upstreamProject = UpstreamProject.make(maybeProject.get(), upstreamReason);
            upstreamProjects.add(upstreamProject);
        }
    }
}

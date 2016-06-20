package org.cavebeetle.maven.impl;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Optional.of;
import static com.google.common.collect.Lists.newArrayList;
import java.io.File;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.project.ProjectBuildingException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingResult;
import org.cavebeetle.maven.DirtDetector;
import org.cavebeetle.maven.DirtyReason;
import org.cavebeetle.maven.Gav;
import org.cavebeetle.maven.GavGenerator;
import org.cavebeetle.maven.GavToProjectMap;
import org.cavebeetle.maven.InternalApi;
import org.cavebeetle.maven.Project;
import org.codehaus.plexus.logging.Logger;
import com.google.common.base.Optional;

/**
 * The implementation of {@code Project}.
 */
public final class DefaultProject
        implements
            Project
{
    /**
     * The implementation of {@code Project.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        private final InternalApi internalApi;

        /**
         * Creates a new {@code DefaultBuilder}.
         *
         * @param internalApi
         *            the {@code InternalApi} instance.
         */
        @Inject
        public DefaultBuilder(
                final InternalApi internalApi)
        {
            this.internalApi = internalApi;
        }

        @Override
        public Project newProject(
                final Logger logger,
                final MavenSession mavenSession,
                final ProjectBuilder mavenProjectBuilder,
                final MavenProject mavenProject,
                final GavToProjectMap gavToProjectMap)
        {
            return new DefaultProject(internalApi, logger, mavenSession, mavenProjectBuilder, mavenProject, gavToProjectMap);
        }
    }

    private final DirtDetector dirtDetector;
    private final GavGenerator gavGenerator;
    private final Logger logger;
    private final MavenProject mavenProject;
    private final Gav gav;
    private final File buildDir;
    private final ProjectBuilder projectBuilder;
    private final MavenSession session;
    private final GavToProjectMap gavToProjectMap;
    private List<Project> dependencies;
    private List<Project> modules;
    private DirtyReason dirtReason;

    /**
     * Creates a new {@code DefaultProject}.
     *
     * @param internalApi
     *            the {@code InternalApi} instance.
     * @param logger
     *            the {@code Logger} instance.
     * @param session
     *            the {@code MavenSession} instance.
     * @param projectBuilder
     *            the {@code ProjectBuilder} instance.
     * @param mavenProject
     *            the {@code MavenProject} representing this project.
     * @param gavToProjectMap
     *            the {@code GavToProjectMap} instance.
     */
    public DefaultProject(
            final InternalApi internalApi,
            final Logger logger,
            final MavenSession session,
            final ProjectBuilder projectBuilder,
            final MavenProject mavenProject,
            final GavToProjectMap gavToProjectMap)
    {
        dirtDetector = internalApi.getDirtDetector();
        gavGenerator = internalApi.getGavGenerator();
        this.logger = logger;
        this.session = session;
        this.projectBuilder = projectBuilder;
        this.mavenProject = mavenProject;
        this.gavToProjectMap = gavToProjectMap;
        gav = gavGenerator.getGav(mavenProject);
        buildDir = new File(mavenProject.getBuild().getDirectory());
    }

    @Override
    public MavenProject getMavenProject()
    {
        return mavenProject;
    }

    @Override
    public File getBaseDir()
    {
        return mavenProject.getBasedir();
    }

    @Override
    public File getBuildDir()
    {
        return buildDir;
    }

    @Override
    public Gav getGav()
    {
        return gav;
    }

    @Override
    public DirtyReason findDirtyReason(
            final boolean includeModules)
    {
        if (dirtReason == null)
        {
            final DirtyReason dirtDetected;
            dirtDetected = dirtDetector.findDirtyReason(logger, session, mavenProject, gavToProjectMap, includeModules);
            dirtReason = dirtDetected;
        }
        return dirtReason;
    }

    @Override
    public Iterable<Project> getDependencies()
    {
        if (dependencies == null)
        {
            init();
        }
        return dependencies;
    }

    @Override
    public Iterable<Project> getModules()
    {
        if (modules == null)
        {
            init();
        }
        return modules;
    }

    @Override
    public int hashCode()
    {
        final int prime = 43;
        final int result = prime + gav.hashCode();
        return result;
    }

    @Override
    public boolean equals(
            final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final DefaultProject other = (DefaultProject) object;
        return gav.equals(other.gav);
    }

    @Override
    public String toString()
    {
        return "[Project " + getGav() + "]";
    }

    private void init()
    {
        final List<Project> dependencies_ = newArrayList();
        final Optional<Project> maybeParentProject = findParent();
        if (maybeParentProject.isPresent())
        {
            final Project parentProject = maybeParentProject.get();
            dependencies_.add(parentProject);
        }
        dependencies_.addAll(findDirectDependencies());
        dependencies_.addAll(findPluginDependencies());
        dependencies = dependencies_;
        modules = findModules();
    }

    private Optional<Project> findParent()
    {
        final Optional<MavenProject> maybeMavenProjectParent = fromNullable(mavenProject.getParent());
        if (maybeMavenProjectParent.isPresent())
        {
            final Gav parentGav = gavGenerator.getGav(maybeMavenProjectParent.get());
            if (gavToProjectMap.containsProjectForGav(parentGav))
            {
                final Project projectParent = gavToProjectMap.getProject(parentGav);
                return of(projectParent);
            }
        }
        return absent();
    }

    private List<Project> findDirectDependencies()
    {
        final List<Project> dependencies_ = newArrayList();
        final List<Dependency> directDependencies = mavenProject.getDependencies();
        for (final Dependency dependency : directDependencies)
        {
            final Gav dependencyGav = gavGenerator.getGav(dependency);
            if (gavToProjectMap.containsProjectForGav(dependencyGav))
            {
                final Project projectDependency = gavToProjectMap.getProject(dependencyGav);
                dependencies_.add(projectDependency);
            }
        }
        return dependencies_;
    }

    private List<Project> findPluginDependencies()
    {
        final List<Project> dependencies_ = newArrayList();
        final List<Plugin> plugins = mavenProject.getBuildPlugins();
        for (final Plugin plugin : plugins)
        {
            final List<Dependency> pluginDependencies = plugin.getDependencies();
            for (final Dependency dependency : pluginDependencies)
            {
                final Gav dependencyGav = gavGenerator.getGav(dependency);
                if (gavToProjectMap.containsProjectForGav(dependencyGav))
                {
                    final Project projectDependency = gavToProjectMap.getProject(dependencyGav);
                    dependencies_.add(projectDependency);
                }
            }
        }
        return dependencies_;
    }

    private List<Project> findModules()
    {
        final List<Project> modules_ = newArrayList();
        final File baseDir = mavenProject.getBasedir();
        for (final String moduleName : mavenProject.getModules())
        {
            final File moduleFile = new File(baseDir, moduleName);
            final File modulePomFile;
            if (moduleFile.isFile())
            {
                modulePomFile = moduleFile;
            }
            else
            {
                modulePomFile = new File(moduleFile, "pom.xml");
            }
            try
            {
                final ProjectBuildingRequest request = session.getProjectBuildingRequest();
                final ProjectBuildingResult projectBuildingResult = projectBuilder.build(modulePomFile, request);
                final MavenProject mavenProjectModule = projectBuildingResult.getProject();
                final Gav moduleGav = gavGenerator.getGav(mavenProjectModule);
                if (gavToProjectMap.containsProjectForGav(moduleGav))
                {
                    final Project module = gavToProjectMap.getProject(moduleGav);
                    modules_.add(module);
                }
            }
            catch (final ProjectBuildingException e)
            {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
        return modules_;
    }
}

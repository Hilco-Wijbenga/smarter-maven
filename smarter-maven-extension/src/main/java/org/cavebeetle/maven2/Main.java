package org.cavebeetle.maven2;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven2.data.ArtifactId;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.GroupId;
import org.cavebeetle.maven2.data.Packaging;
import org.cavebeetle.maven2.data.PomFile;
import org.cavebeetle.maven2.data.Project;
import org.cavebeetle.maven2.data.UpstreamProject;
import org.cavebeetle.maven2.data.UpstreamReason;
import org.cavebeetle.maven2.data.Version;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public final class Main
{
    public static void main(final String[] args)
    {
        final File file = new File("/home/hilco/workspaces/smarter-maven/pom.xml");
        final PomFile rootPomFile = newPomFile(file);
        final StrictMap<Gav, Project> gavToProjectMap = findProjects(StrictMap.Builder.<Gav, Project> newStrictMap(), rootPomFile);
        final StrictMap.Mutable<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap = StrictMap.Builder.newStrictMap();
        for (final Gav gav : gavToProjectMap)
        {
            final Project project = gavToProjectMap.get(gav);
            projectToUpstreamProjectsMap.put(project, Sets.<UpstreamProject> newHashSet());
        }
        for (final Gav gav : gavToProjectMap)
        {
            final Project project = gavToProjectMap.get(gav);
            final MavenProject mavenProject = mapPomFileToMavenProject(project.pomFile());
            addParentToUpstreamProjects(gavToProjectMap, projectToUpstreamProjectsMap, project, mavenProject);
            addModulesToUpstreamProjects(gavToProjectMap, projectToUpstreamProjectsMap, project, mavenProject);
            addDependenciesToUpstreamProjects(gavToProjectMap, projectToUpstreamProjectsMap, project, mavenProject);
            addPluginsAndPluginDependenciesToUpstreamProjects(gavToProjectMap, projectToUpstreamProjectsMap, project, mavenProject);
            addExtensionsToUpstreamProjects(gavToProjectMap, projectToUpstreamProjectsMap, project, mavenProject);
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
        final StrictMap.Mutable<Project, Set<Project>> projectToDownstreamProjectsMap_ = StrictMap.Builder.newStrictMap();
        for (final Gav gav : gavToProjectMap)
        {
            final Project project = gavToProjectMap.get(gav);
            projectToDownstreamProjectsMap_.put(project, Sets.<Project> newHashSet());
        }
        final StrictMap<Project, Set<Project>> projectToDownstreamProjectsMap = projectToDownstreamProjectsMap_.freeze();
        for (final Gav gav : gavToProjectMap)
        {
            final Project project = gavToProjectMap.get(gav);
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

    public static final Set<UpstreamProject> findAllUpstreamProjects(
            final Set<UpstreamProject> allUpstreamProjects,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project)
    {
        for (final UpstreamProject upstreamProject : projectToUpstreamProjectsMap.get(project))
        {
            if (!allUpstreamProjects.contains(upstreamProject))
            {
                allUpstreamProjects.add(upstreamProject);
                findAllUpstreamProjects(allUpstreamProjects, projectToUpstreamProjectsMap, upstreamProject.value());
            }
        }
        return allUpstreamProjects;
    }

    public static void addParentToUpstreamProjects(
            final StrictMap<Gav, Project> gavToProjectMap,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project,
            final MavenProject mavenProject)
    {
        final Optional<Parent> maybeParent = Optional.fromNullable(mavenProject.getModel().getParent());
        if (maybeParent.isPresent())
        {
            final Parent parent = maybeParent.get();
            addToUpstreamProjects(GavMapper.PARENT_TO_GAV, parent, gavToProjectMap, projectToUpstreamProjectsMap, project, UpstreamReason.PARENT);
        }
    }

    public static void addModulesToUpstreamProjects(
            final StrictMap<Gav, Project> gavToProjectMap,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project,
            final MavenProject mavenProject)
    {
        for (final String module : mavenProject.getModules())
        {
            final PomFile modulePomFile = mapModuleToPomFile(project.pomFile(), module);
            final MavenProject moduleMavenProject = mapPomFileToMavenProject(modulePomFile);
            addToUpstreamProjects(GavMapper.MAVEN_PROJECT_TO_GAV, moduleMavenProject, gavToProjectMap, projectToUpstreamProjectsMap, project, UpstreamReason.MODULE);
        }
    }

    public static void addDependenciesToUpstreamProjects(
            final StrictMap<Gav, Project> gavToProjectMap,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project,
            final MavenProject mavenProject)
    {
        for (final Dependency dependency : mavenProject.getDependencies())
        {
            addToUpstreamProjects(GavMapper.DEPENDENCY_TO_GAV, dependency, gavToProjectMap, projectToUpstreamProjectsMap, project, UpstreamReason.DEPENDENCY);
        }
    }

    public static void addPluginsAndPluginDependenciesToUpstreamProjects(
            final StrictMap<Gav, Project> gavToProjectMap,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project,
            final MavenProject mavenProject)
    {
        for (final Plugin plugin : mavenProject.getBuild().getPlugins())
        {
            addToUpstreamProjects(GavMapper.PLUGIN_TO_GAV, plugin, gavToProjectMap, projectToUpstreamProjectsMap, project, UpstreamReason.PLUGIN);
            for (final Dependency pluginDependency : plugin.getDependencies())
            {
                addToUpstreamProjects(GavMapper.DEPENDENCY_TO_GAV, pluginDependency, gavToProjectMap, projectToUpstreamProjectsMap, project, UpstreamReason.PLUGIN_DEPENDENCY);
            }
        }
    }

    public static void addExtensionsToUpstreamProjects(
            final StrictMap<Gav, Project> gavToProjectMap,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project,
            final MavenProject mavenProject)
    {
        for (final Extension extension : mavenProject.getBuild().getExtensions())
        {
            addToUpstreamProjects(GavMapper.EXTENSION_TO_GAV, extension, gavToProjectMap, projectToUpstreamProjectsMap, project, UpstreamReason.EXTENSION);
        }
    }

    @SuppressWarnings("boxing")
    public static final <SOURCE> void addToUpstreamProjects(
            final GavMapper<SOURCE> gavMapper,
            final SOURCE source,
            final StrictMap<Gav, Project> gavToProjectMap,
            final StrictMap<Project, Set<UpstreamProject>> projectToUpstreamProjectsMap,
            final Project project,
            final UpstreamReason upstreamReason)
    {
        final Gav gav = gavMapper.map(source);
        final Optional<Project> maybeProject = Optional.fromNullable(gavToProjectMap.get(gav));
        if (maybeProject.isPresent())
        {
            final UpstreamProject upstreamProject = newUpstreamProject(maybeProject.get(), upstreamReason);
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

    private static final Cache<PomFile, MavenProject> MAVEN_PROJECT_CACHE = CacheBuilder.newBuilder().build();

    public static final MavenProject mapPomFileToMavenProject(final PomFile pomFile)
    {
        return SmarterMavenRuntime.getValueFromCache(MAVEN_PROJECT_CACHE, pomFile, new Callable<MavenProject>()
        {
            @Override
            public MavenProject call()
            {
                final ModelBuilder modelBuilder = new DefaultModelBuilderFactory().newInstance();
                final ModelBuildingRequest modelBuildingRequest = new DefaultModelBuildingRequest();
                modelBuildingRequest.setPomFile(pomFile.value());
                modelBuildingRequest.setProcessPlugins(false);
                modelBuildingRequest.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL);
                final ModelBuildingResult modelBuildingResult =
                    SmarterMavenRuntime.toModelBuildingResult(modelBuilder, modelBuildingRequest);
                final Model model = modelBuildingResult.getEffectiveModel();
                final MavenProject mavenProject = new MavenProject(model);
                mavenProject.setFile(pomFile.value());
                return mavenProject;
            }
        });
    }

    public static final StrictMap<Gav, Project> findProjects(
            final StrictMap.Mutable<Gav, Project> gavToProjectMap,
            final PomFile pomFile)
    {
        final MavenProject mavenProject = mapPomFileToMavenProject(pomFile);
        final Gav gav = GavMapper.MAVEN_PROJECT_TO_GAV.map(mavenProject);
        final Packaging type = newPackaging(mavenProject.getPackaging());
        final Project project = newProject(gav, type, pomFile);
        System.out.println(String.format("Found %s (%s)", gav, pomFile));
        gavToProjectMap.put(gav, project);
        for (final String module : mavenProject.getModules())
        {
            final PomFile modulePomFile = mapModuleToPomFile(pomFile, module);
            findProjects(gavToProjectMap, modulePomFile);
        }
        return gavToProjectMap;
    }

    public static final PomFile mapModuleToPomFile(final PomFile pomFile, final String module)
    {
        final File moduleFile = SmarterMavenRuntime.toCanonicalFile(new File(pomFile.value().getParentFile(), module));
        return moduleFile.isFile()
            ? newPomFile(moduleFile)
            : newPomFile(SmarterMavenRuntime.toCanonicalFile(new File(moduleFile, "pom.xml")));
    }

    public static final PomFile newPomFile(final File pomFile)
    {
        Preconditions.checkNotNull(pomFile, "Missing 'pomFile'.");
        try
        {
            return new PomFile(pomFile.getCanonicalFile());
        }
        catch (final IOException e)
        {
            throw new IllegalStateException(e);
        }
    }

    private static final Interner<GroupId> GROUP_ID_INTERNER = Interners.newWeakInterner();

    public static final GroupId newGroupId(final String groupId)
    {
        return GROUP_ID_INTERNER.intern(new GroupId(groupId));
    }

    private static final Interner<ArtifactId> ARTIFACT_ID_INTERNER = Interners.newWeakInterner();

    public static final ArtifactId newArtifactId(final String artifactId)
    {
        return ARTIFACT_ID_INTERNER.intern(new ArtifactId(artifactId));
    }

    private static final Interner<Version> VERSION_INTERNER = Interners.newWeakInterner();

    public static final Version newVersion(final String version)
    {
        return VERSION_INTERNER.intern(new Version(version));
    }

    private static final Interner<Gav> GAV_INTERNER = Interners.newWeakInterner();

    public static final Gav newGav(final GroupId groupId, final ArtifactId artifactId, final Version version)
    {
        return GAV_INTERNER.intern(new Gav(groupId, artifactId, version));
    }

    private static final Interner<Packaging> PACKAGING_INTERNER = Interners.newWeakInterner();

    public static final Packaging newPackaging(final String packaging)
    {
        return PACKAGING_INTERNER.intern(new Packaging(packaging));
    }

    private static final Interner<Project> PROJECT_INTERNER = Interners.newWeakInterner();

    public static final Project newProject(final Gav gav, final Packaging type, final PomFile pomFile)
    {
        return PROJECT_INTERNER.intern(new Project(gav, type, pomFile));
    }

    private static final Interner<UpstreamProject> UPSTREAM_PROJECT_INTERNER = Interners.newWeakInterner();

    public static final UpstreamProject newUpstreamProject(final Project project, final UpstreamReason upstreamReason)
    {
        return UPSTREAM_PROJECT_INTERNER.intern(new UpstreamProject(project, upstreamReason));
    }
}

interface GavMapper<SOURCE>
{
    Gav map(SOURCE source);

    GavMapper<MavenProject> MAVEN_PROJECT_TO_GAV = new DefaultMavenProjectToGavMapper();
    GavMapper<Parent> PARENT_TO_GAV = new DefaultParentToGavMapper();
    GavMapper<Dependency> DEPENDENCY_TO_GAV = new DefaultDependencyToGavMapper();
    GavMapper<Plugin> PLUGIN_TO_GAV = new DefaultPluginToGavMapper();
    GavMapper<Extension> EXTENSION_TO_GAV = new DefaultExtensionToGavMapper();

    public static final class DefaultMavenProjectToGavMapper
            implements
                GavMapper<MavenProject>
    {
        @Override
        public Gav map(final MavenProject source)
        {
            return Main.newGav(
                    Main.newGroupId(source.getGroupId()),
                    Main.newArtifactId(source.getArtifactId()),
                    Main.newVersion(source.getVersion()));
        }
    }

    public static final class DefaultParentToGavMapper
            implements
                GavMapper<Parent>
    {
        @Override
        public Gav map(final Parent source)
        {
            return Main.newGav(
                    Main.newGroupId(source.getGroupId()),
                    Main.newArtifactId(source.getArtifactId()),
                    Main.newVersion(source.getVersion()));
        }
    }

    public static final class DefaultDependencyToGavMapper
            implements
                GavMapper<Dependency>
    {
        @Override
        public Gav map(final Dependency source)
        {
            return Main.newGav(
                    Main.newGroupId(source.getGroupId()),
                    Main.newArtifactId(source.getArtifactId()),
                    Main.newVersion(source.getVersion()));
        }
    }

    public static final class DefaultPluginToGavMapper
            implements
                GavMapper<Plugin>
    {
        @Override
        public Gav map(final Plugin source)
        {
            return Main.newGav(
                    Main.newGroupId(source.getGroupId()),
                    Main.newArtifactId(source.getArtifactId()),
                    Main.newVersion(source.getVersion()));
        }
    }

    public static final class DefaultExtensionToGavMapper
            implements
                GavMapper<Extension>
    {
        @Override
        public Gav map(final Extension source)
        {
            return Main.newGav(
                    Main.newGroupId(source.getGroupId()),
                    Main.newArtifactId(source.getArtifactId()),
                    Main.newVersion(source.getVersion()));
        }
    }
}

package org.cavebeetle.maven;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public final class Main
{
    public static void main(final String[] args)
    {
        final File file = new File("/home/hilco/workspaces/smarter-maven/pom.xml");
        final PomFile_ rootPomFile = newPomFile(file);
        final Map<Gav_, Project_> gavToProjectMap =
            ImmutableMap.copyOf(
                    findProjects(Maps.<Gav_, Project_> newConcurrentMap(), rootPomFile));
        final Map<Project_, Set<UpstreamProject_>> projectToUpstreamProjectsMap = Maps.newConcurrentMap();
        for (final Gav_ gav : gavToProjectMap.keySet())
        {
            final Project_ project = gavToProjectMap.get(gav);
            projectToUpstreamProjectsMap.put(project, Sets.<UpstreamProject_> newHashSet());
        }
        for (final Gav_ gav : gavToProjectMap.keySet())
        {
            final Project_ project = gavToProjectMap.get(gav);
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
        for (final Project_ project_ : projectToUpstreamProjectsMap.keySet())
        {
            System.out.println(String.format("%s -->", projectToText(project_)));
            final List<UpstreamProject_> upstreamProjects_ = Lists.newArrayList(projectToUpstreamProjectsMap.get(project_));
            Collections.sort(upstreamProjects_);
            final List<UpstreamProject_> upstreamProjects = ImmutableList.copyOf(upstreamProjects_);
            for (final UpstreamProject_ upstreamProject : upstreamProjects)
            {
                System.out.println(String.format("    %s (%s)", projectToText(upstreamProject.value()), upstreamProject.upstreamReason()));
            }
        }
        for (final Project_ project_ : projectToUpstreamProjectsMap.keySet())
        {
            System.out.println(String.format("%s -->", projectToText(project_)));
            final Set<UpstreamProject_> allUpstreamProjects = findAllUpstreamProjects(Sets.<UpstreamProject_> newHashSet(), projectToUpstreamProjectsMap, project_);
            for (final UpstreamProject_ upstreamProject : allUpstreamProjects)
            {
                System.out.println(String.format("    %s (%s)", projectToText(upstreamProject.value()), upstreamProject.upstreamReason()));
            }
        }
        final Map<Project_, Set<Project_>> projectToDownstreamProjectsMap = Maps.newConcurrentMap();
        for (final Gav_ gav : gavToProjectMap.keySet())
        {
            final Project_ project = gavToProjectMap.get(gav);
            projectToDownstreamProjectsMap.put(project, Sets.<Project_> newHashSet());
        }
        for (final Gav_ gav : gavToProjectMap.keySet())
        {
            final Project_ project = gavToProjectMap.get(gav);
            final Set<UpstreamProject_> upstreamProjects = projectToUpstreamProjectsMap.get(project);
            for (final UpstreamProject_ upstreamProject : upstreamProjects)
            {
                projectToDownstreamProjectsMap.get(upstreamProject.value()).add(project);
            }
        }
        System.out.println();
        System.out.println("Downstream Projects");
        System.out.println("-------------------");
        for (final Project_ project_ : projectToDownstreamProjectsMap.keySet())
        {
            System.out.println(String.format("%s -->", projectToText(project_)));
            final List<Project_> downstreamProjects_ = Lists.newArrayList(projectToDownstreamProjectsMap.get(project_));
            Collections.sort(downstreamProjects_);
            final List<Project_> downstreamProjects = ImmutableList.copyOf(downstreamProjects_);
            for (final Project_ downstreamProject : downstreamProjects)
            {
                System.out.println(String.format("    %s", projectToText(downstreamProject)));
            }
        }
    }

    public static final String projectToText(final Project_ project)
    {
        final Gav_ gav = project.gav();
        return String.format(
                "%s:%s:%s:%s",
                gav.groupId().value(),
                gav.artifactId().value(),
                project.packaging().value(),
                gav.version().value());
    }

    public static final Set<UpstreamProject_> findAllUpstreamProjects(
            final Set<UpstreamProject_> allUpstreamProjects,
            final Map<Project_, Set<UpstreamProject_>> projectToUpstreamProjectsMap,
            final Project_ project)
    {
        for (final UpstreamProject_ upstreamProject : projectToUpstreamProjectsMap.get(project))
        {
            if (!allUpstreamProjects.contains(upstreamProject))
            {
                allUpstreamProjects.add(upstreamProject);
                findAllUpstreamProjects(allUpstreamProjects, projectToUpstreamProjectsMap, upstreamProject.value());
            }
        }
        return allUpstreamProjects;
    }

    public static void addParentToUpstreamProjects(final Map<Gav_, Project_> gavToProjectMap, final Map<Project_, Set<UpstreamProject_>> projectToUpstreamProjectsMap, final Project_ project, final MavenProject mavenProject)
    {
        final Optional<Parent> maybeParent = Optional.fromNullable(mavenProject.getModel().getParent());
        if (maybeParent.isPresent())
        {
            final Parent parent = maybeParent.get();
            addToUpstreamProjects(GavMapper.PARENT_TO_GAV, parent, gavToProjectMap, projectToUpstreamProjectsMap, project, UpstreamReason.PARENT);
        }
    }

    public static void addModulesToUpstreamProjects(final Map<Gav_, Project_> gavToProjectMap, final Map<Project_, Set<UpstreamProject_>> projectToUpstreamProjectsMap, final Project_ project, final MavenProject mavenProject)
    {
        for (final String module : mavenProject.getModules())
        {
            final PomFile_ modulePomFile = mapModuleToPomFile(project.pomFile(), module);
            final MavenProject moduleMavenProject = mapPomFileToMavenProject(modulePomFile);
            addToUpstreamProjects(GavMapper.MAVEN_PROJECT_TO_GAV, moduleMavenProject, gavToProjectMap, projectToUpstreamProjectsMap, project, UpstreamReason.MODULE);
        }
    }

    public static void addDependenciesToUpstreamProjects(final Map<Gav_, Project_> gavToProjectMap, final Map<Project_, Set<UpstreamProject_>> projectToUpstreamProjectsMap, final Project_ project, final MavenProject mavenProject)
    {
        for (final Dependency dependency : mavenProject.getDependencies())
        {
            addToUpstreamProjects(GavMapper.DEPENDENCY_TO_GAV, dependency, gavToProjectMap, projectToUpstreamProjectsMap, project, UpstreamReason.DEPENDENCY);
        }
    }

    public static void addPluginsAndPluginDependenciesToUpstreamProjects(final Map<Gav_, Project_> gavToProjectMap, final Map<Project_, Set<UpstreamProject_>> projectToUpstreamProjectsMap, final Project_ project, final MavenProject mavenProject)
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

    public static void addExtensionsToUpstreamProjects(final Map<Gav_, Project_> gavToProjectMap, final Map<Project_, Set<UpstreamProject_>> projectToUpstreamProjectsMap, final Project_ project, final MavenProject mavenProject)
    {
        for (final Extension extension : mavenProject.getBuild().getExtensions())
        {
            addToUpstreamProjects(GavMapper.EXTENSION_TO_GAV, extension, gavToProjectMap, projectToUpstreamProjectsMap, project, UpstreamReason.EXTENSION);
        }
    }

    public static final <SOURCE> void addToUpstreamProjects(
            final GavMapper<SOURCE> gavMapper,
            final SOURCE source,
            final Map<Gav_, Project_> gavToProjectMap,
            final Map<Project_, Set<UpstreamProject_>> projectToUpstreamProjectsMap,
            final Project_ project,
            final UpstreamReason upstreamReason)
    {
        final Gav_ gav = gavMapper.map(source);
        final Optional<Project_> maybeProject = Optional.fromNullable(gavToProjectMap.get(gav));
        if (maybeProject.isPresent())
        {
            final UpstreamProject_ upstreamProject = newUpstreamProject(maybeProject.get(), upstreamReason);
            projectToUpstreamProjectsMap.get(project).add(upstreamProject);
            System.out.println(
                    String.format(
                            "##### %s - Add UpstreamProject %s:%s",
                            projectToText(project),
                            upstreamProject.value(),
                            upstreamProject.upstreamReason()));
        }
    }

    private static final Cache<PomFile_, MavenProject> MAVEN_PROJECT_CACHE = CacheBuilder.newBuilder().build();

    public static final MavenProject mapPomFileToMavenProject(final PomFile_ pomFile)
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

    public static final Map<Gav_, Project_> findProjects(
            final Map<Gav_, Project_> gavToProjectMap,
            final PomFile_ pomFile)
    {
        final MavenProject mavenProject = mapPomFileToMavenProject(pomFile);
        final Gav_ gav = GavMapper.MAVEN_PROJECT_TO_GAV.map(mavenProject);
        final Packaging_ type = newPackaging(mavenProject.getPackaging());
        final Project_ project = newProject(gav, type, pomFile);
        System.out.println(String.format("Found %s (%s)", gav, pomFile));
        gavToProjectMap.put(gav, project);
        for (final String module : mavenProject.getModules())
        {
            final PomFile_ modulePomFile = mapModuleToPomFile(pomFile, module);
            findProjects(gavToProjectMap, modulePomFile);
        }
        return gavToProjectMap;
    }

    public static final PomFile_ mapModuleToPomFile(final PomFile_ pomFile, final String module)
    {
        final File moduleFile = SmarterMavenRuntime.toCanonicalFile(new File(pomFile.value().getParentFile(), module));
        return moduleFile.isFile()
            ? newPomFile(moduleFile)
            : newPomFile(SmarterMavenRuntime.toCanonicalFile(new File(moduleFile, "pom.xml")));
    }

    public static final PomFile_ newPomFile(final File pomFile)
    {
        Preconditions.checkNotNull(pomFile, "Missing 'pomFile'.");
        try
        {
            return new PomFile_(pomFile.getCanonicalFile());
        }
        catch (final IOException e)
        {
            throw new IllegalStateException(e);
        }
    }

    private static final Interner<GroupId_> GROUP_ID_INTERNER = Interners.newWeakInterner();

    public static final GroupId_ newGroupId(final String groupId)
    {
        return GROUP_ID_INTERNER.intern(new GroupId_(groupId));
    }

    private static final Interner<ArtifactId_> ARTIFACT_ID_INTERNER = Interners.newWeakInterner();

    public static final ArtifactId_ newArtifactId(final String artifactId)
    {
        return ARTIFACT_ID_INTERNER.intern(new ArtifactId_(artifactId));
    }

    private static final Interner<Version_> VERSION_INTERNER = Interners.newWeakInterner();

    public static final Version_ newVersion(final String version)
    {
        return VERSION_INTERNER.intern(new Version_(version));
    }

    private static final Interner<Gav_> GAV_INTERNER = Interners.newWeakInterner();

    public static final Gav_ newGav(final GroupId_ groupId, final ArtifactId_ artifactId, final Version_ version)
    {
        return GAV_INTERNER.intern(new Gav_(groupId, artifactId, version));
    }

    private static final Interner<Packaging_> PACKAGING_INTERNER = Interners.newWeakInterner();

    public static final Packaging_ newPackaging(final String packaging)
    {
        return PACKAGING_INTERNER.intern(new Packaging_(packaging));
    }

    private static final Interner<Project_> PROJECT_INTERNER = Interners.newWeakInterner();

    public static final Project_ newProject(final Gav_ gav, final Packaging_ type, final PomFile_ pomFile)
    {
        return PROJECT_INTERNER.intern(new Project_(gav, type, pomFile));
    }

    private static final Interner<UpstreamProject_> UPSTREAM_PROJECT_INTERNER = Interners.newWeakInterner();

    public static final UpstreamProject_ newUpstreamProject(final Project_ project, final UpstreamReason upstreamReason)
    {
        return UPSTREAM_PROJECT_INTERNER.intern(new UpstreamProject_(project, upstreamReason));
    }
}

enum UpstreamReason
{
    PARENT,
    MODULE,
    DEPENDENCY,
    PLUGIN,
    PLUGIN_DEPENDENCY,
    EXTENSION;
}

class UpstreamProject_
        implements
            Comparable<UpstreamProject_>
{
    private final Project_ value;
    private final UpstreamReason upstreamReason;

    public UpstreamProject_(final Project_ value, final UpstreamReason upstreamReason)
    {
        Preconditions.checkArgument(value != null, "Missing 'value'.");
        Preconditions.checkArgument(upstreamReason != null, "Missing 'upstreamReason'.");
        this.value = value;
        this.upstreamReason = upstreamReason;
    }

    public Project_ value()
    {
        return value;
    }

    public UpstreamReason upstreamReason()
    {
        return upstreamReason;
    }

    @Override
    public String toString()
    {
        return String.format("[UpstreamProject value='%s' upstreamReason=%s]", value, upstreamReason);
    }

    @Override
    public int compareTo(final UpstreamProject_ other)
    {
        return ComparisonChain
                .start()
                .compare(value, other.value())
                .compare(upstreamReason, other.upstreamReason())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + value.hashCode();
        result = prime * result + upstreamReason.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final UpstreamProject_ other = (UpstreamProject_) object;
        return compareTo(other) == 0;
    }
}

class PomFile_
        implements
            Comparable<PomFile_>
{
    private final File value;

    public PomFile_(final File value)
    {
        Preconditions.checkArgument(value != null && value.isFile(), "Missing 'value'.");
        this.value = value;
    }

    public File value()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return String.format("[PomFile value='%s']", value.getPath());
    }

    @Override
    public int compareTo(final PomFile_ other)
    {
        return ComparisonChain
                .start()
                .compare(value, other.value())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + value.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final PomFile_ other = (PomFile_) object;
        return compareTo(other) == 0;
    }
}

class GroupId_
        implements
            Comparable<GroupId_>
{
    private final String value;

    public GroupId_(final String value)
    {
        Preconditions.checkArgument(value != null && !value.isEmpty(), "Missing 'value'.");
        this.value = value;
    }

    public String value()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return String.format("[GroupId value='%s']", value);
    }

    @Override
    public int compareTo(final GroupId_ other)
    {
        return ComparisonChain
                .start()
                .compare(value, other.value())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + value.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final GroupId_ other = (GroupId_) object;
        return compareTo(other) == 0;
    }
}

class ArtifactId_
        implements
            Comparable<ArtifactId_>
{
    private final String value;

    public ArtifactId_(final String value)
    {
        Preconditions.checkArgument(value != null && !value.isEmpty(), "Missing 'value'.");
        this.value = value;
    }

    public String value()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return String.format("[ArtifactId value='%s']", value);
    }

    @Override
    public int compareTo(final ArtifactId_ other)
    {
        return ComparisonChain
                .start()
                .compare(value, other.value())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + value.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final ArtifactId_ other = (ArtifactId_) object;
        return compareTo(other) == 0;
    }
}

class Version_
        implements
            Comparable<Version_>
{
    private final String value;

    public Version_(final String value)
    {
        Preconditions.checkArgument(value != null && !value.isEmpty(), "Missing 'value'.");
        this.value = value;
    }

    public String value()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return String.format("[Version value='%s']", value);
    }

    @Override
    public int compareTo(final Version_ other)
    {
        return ComparisonChain
                .start()
                .compare(value, other.value())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + value.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final Version_ other = (Version_) object;
        return compareTo(other) == 0;
    }
}

class Gav_
        implements
            Comparable<Gav_>
{
    private final GroupId_ groupId;
    private final ArtifactId_ artifactId;
    private final Version_ version;

    public Gav_(final GroupId_ groupId, final ArtifactId_ artifactId, final Version_ version)
    {
        Preconditions.checkNotNull(groupId, "Missing 'groupId'.");
        Preconditions.checkNotNull(artifactId, "Missing 'artifactId'.");
        Preconditions.checkNotNull(version, "Missing 'version'.");
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public GroupId_ groupId()
    {
        return groupId;
    }

    public ArtifactId_ artifactId()
    {
        return artifactId;
    }

    public Version_ version()
    {
        return version;
    }

    @Override
    public String toString()
    {
        return String.format("[Gav groupId=%s artifactId=%s version=%s]", groupId, artifactId, version);
    }

    @Override
    public int compareTo(final Gav_ other)
    {
        return ComparisonChain
                .start()
                .compare(groupId, other.groupId())
                .compare(artifactId, other.artifactId())
                .compare(version, other.version())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + groupId.hashCode();
        result = prime * result + artifactId.hashCode();
        result = prime * result + version.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final Gav_ other = (Gav_) object;
        return compareTo(other) == 0;
    }
}

class Packaging_
        implements
            Comparable<Packaging_>
{
    private final String value;

    public Packaging_(final String value)
    {
        Preconditions.checkArgument(value != null && !value.isEmpty(), "Missing 'value'.");
        this.value = value;
    }

    public String value()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return String.format("[Packaging value='%s']", value);
    }

    @Override
    public int compareTo(final Packaging_ other)
    {
        return ComparisonChain
                .start()
                .compare(value, other.value())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + value.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final Packaging_ other = (Packaging_) object;
        return compareTo(other) == 0;
    }
}

class Project_
        implements
            Comparable<Project_>
{
    private final Gav_ gav;
    private final Packaging_ packaging;
    private final PomFile_ pomFile;

    public Project_(final Gav_ gav, final Packaging_ packaging, final PomFile_ pomFile)
    {
        Preconditions.checkNotNull(gav, "Missing 'gav'.");
        Preconditions.checkNotNull(packaging, "Missing 'packaging'.");
        Preconditions.checkNotNull(pomFile, "Missing 'pomFile'.");
        this.gav = gav;
        this.packaging = packaging;
        this.pomFile = pomFile;
    }

    public Gav_ gav()
    {
        return gav;
    }

    public Packaging_ packaging()
    {
        return packaging;
    }

    public PomFile_ pomFile()
    {
        return pomFile;
    }

    @Override
    public String toString()
    {
        return String.format("[Project gav=%s packaging=%s pomFile=%s]", gav, packaging, pomFile);
    }

    @Override
    public int compareTo(final Project_ other)
    {
        return ComparisonChain.start()
                .compare(gav, other.gav())
                .compare(packaging, other.packaging())
                .compare(pomFile, other.pomFile())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + gav.hashCode();
        result = prime * result + packaging.hashCode();
        result = prime * result + pomFile.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final Project_ other = (Project_) object;
        return compareTo(other) == 0;
    }
}

interface GavMapper<SOURCE>
{
    Gav_ map(SOURCE source);

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
        public Gav_ map(final MavenProject source)
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
        public Gav_ map(final Parent source)
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
        public Gav_ map(final Dependency source)
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
        public Gav_ map(final Plugin source)
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
        public Gav_ map(final Extension source)
        {
            return Main.newGav(
                    Main.newGroupId(source.getGroupId()),
                    Main.newArtifactId(source.getArtifactId()),
                    Main.newVersion(source.getVersion()));
        }
    }
}

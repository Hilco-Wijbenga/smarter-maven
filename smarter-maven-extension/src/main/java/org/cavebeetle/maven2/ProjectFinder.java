package org.cavebeetle.maven2;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.Packaging;
import org.cavebeetle.maven2.data.PomFile;
import org.cavebeetle.maven2.data.Project;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public final class ProjectFinder
        implements
            Iterable<Project>
{
    public static final StrictMap<Gav, Project> initialize(final MavenProjectCache mavenProjectCache, final PomFile pomFile)
    {
        final StrictMap.Mutable<Gav, Project> gavToProjectMap = StrictMap.Builder.newStrictMap();
        findAll(mavenProjectCache, gavToProjectMap, pomFile);
        return gavToProjectMap;
    }

    public static final void findAll(
            final MavenProjectCache mavenProjectCache,
            final StrictMap.Mutable<Gav, Project> gavToProjectMap,
            final PomFile pomFile)
    {
        final MavenProject mavenProject = mavenProjectCache.get(pomFile);
        final Gav gav = GavMapper.MAVEN_PROJECT_TO_GAV.map(mavenProject);
        final Packaging type = Packaging.make(mavenProject.getPackaging());
        final Project project = Project.make(gav, type, pomFile);
        System.out.println(String.format("Found %s (%s)", gav, pomFile));
        gavToProjectMap.put(gav, project);
        for (final String module : mavenProject.getModules())
        {
            final PomFile modulePomFile = getPomFile(pomFile, module);
            findAll(mavenProjectCache, gavToProjectMap, modulePomFile);
        }
    }

    public static PomFile getPomFile(final PomFile pomFile, final String module)
    {
        final File moduleFile = new File(pomFile.value().getParentFile(), module);
        return moduleFile.isFile()
            ? PomFile.make(moduleFile)
            : PomFile.make(new File(moduleFile, "pom.xml"));
    }

    private final MavenProjectCache mavenProjectCache;
    private final StrictMap<Gav, Project> gavToProjectMap;
    private final List<Project> projects;

    public ProjectFinder(final MavenProjectCache mavenProjectCache, final PomFile pomFile)
    {
        this.mavenProjectCache = mavenProjectCache;
        gavToProjectMap = initialize(mavenProjectCache, pomFile);
        final List<Project> projects_ = Lists.newArrayList();
        for (final Gav gav : gavToProjectMap)
        {
            projects_.add(gavToProjectMap.get(gav));
        }
        projects = ImmutableList.copyOf(projects_);
    }

    public Optional<Project> getProject(final Gav gav)
    {
        return Optional.fromNullable(gavToProjectMap.get(gav));
    }

    @Override
    public Iterator<Project> iterator()
    {
        return projects.iterator();
    }
}

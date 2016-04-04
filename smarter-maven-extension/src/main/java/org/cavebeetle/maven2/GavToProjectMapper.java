package org.cavebeetle.maven2;

import java.io.File;
import java.util.List;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.Packaging;
import org.cavebeetle.maven2.data.PomFile;
import org.cavebeetle.maven2.data.Project;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public final class GavToProjectMapper
{
    private final MavenProjectCache mavenProjectCache;
    private final StrictMap.Mutable<Gav, Project> gavToProjectMap;

    public GavToProjectMapper()
    {
        mavenProjectCache = new MavenProjectCache();
        gavToProjectMap = StrictMap.Builder.make();
    }

    public List<Project> findAll(final PomFile pomFile)
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
            findAll(modulePomFile);
        }
        final List<Project> projects = Lists.newArrayList();
        for (final Gav gav_ : gavToProjectMap)
        {
            projects.add(gavToProjectMap.get(gav_));
        }
        return ImmutableList.copyOf(projects);
    }

    public Optional<Project> getProject(final Gav gav)
    {
        return Optional.fromNullable(gavToProjectMap.get(gav));
    }

    public PomFile getPomFile(final PomFile pomFile, final String module)
    {
        final File moduleFile = new File(pomFile.value().getParentFile(), module);
        return moduleFile.isFile()
            ? PomFile.make(moduleFile)
            : PomFile.make(new File(moduleFile, "pom.xml"));
    }
}

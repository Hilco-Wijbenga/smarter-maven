package org.cavebeetle.maven2;

import java.io.File;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.Packaging;
import org.cavebeetle.maven2.data.PomFile;
import org.cavebeetle.maven2.data.Project;

public final class ProjectFinder
{
    private final MavenProjectCache mavenProjectCache;

    public ProjectFinder()
    {
        mavenProjectCache = new MavenProjectCache();
    }

    public StrictMap<Gav, Project> find(final StrictMap.Mutable<Gav, Project> gavToProjectMap, final PomFile pomFile)
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
            find(gavToProjectMap, modulePomFile);
        }
        return gavToProjectMap;
    }

    public PomFile getPomFile(final PomFile pomFile, final String module)
    {
        final File moduleFile = new File(pomFile.value().getParentFile(), module);
        return moduleFile.isFile()
            ? PomFile.make(moduleFile)
            : PomFile.make(new File(moduleFile, "pom.xml"));
    }
}

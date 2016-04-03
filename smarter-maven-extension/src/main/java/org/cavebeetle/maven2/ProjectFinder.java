package org.cavebeetle.maven2;

import java.io.File;
import java.util.concurrent.Callable;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.Packaging;
import org.cavebeetle.maven2.data.PomFile;
import org.cavebeetle.maven2.data.Project;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public final class ProjectFinder
{
    public static final StrictMap<Gav, Project> findProjects(
            final StrictMap.Mutable<Gav, Project> gavToProjectMap,
            final PomFile pomFile)
    {
        final MavenProject mavenProject = mapPomFileToMavenProject(pomFile);
        final Gav gav = GavMapper.MAVEN_PROJECT_TO_GAV.map(mavenProject);
        final Packaging type = Packaging.make(mavenProject.getPackaging());
        final Project project = Project.make(gav, type, pomFile);
        System.out.println(String.format("Found %s (%s)", gav, pomFile));
        gavToProjectMap.put(gav, project);
        for (final String module : mavenProject.getModules())
        {
            final PomFile modulePomFile = mapModuleToPomFile(pomFile, module);
            findProjects(gavToProjectMap, modulePomFile);
        }
        return gavToProjectMap;
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

    public static final PomFile mapModuleToPomFile(final PomFile pomFile, final String module)
    {
        final File moduleFile = SmarterMavenRuntime.toCanonicalFile(new File(pomFile.value().getParentFile(), module));
        return moduleFile.isFile()
            ? PomFile.make(moduleFile)
            : PomFile.make(SmarterMavenRuntime.toCanonicalFile(new File(moduleFile, "pom.xml")));
    }

    public ProjectFinder()
    {
    }
}

package org.cavebeetle.maven2.impl;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.apache.maven.model.Model;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.model.building.DefaultModelBuildingRequest;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven2.MavenProjectCache;
import org.cavebeetle.maven2.SmarterMavenException;
import org.cavebeetle.maven2.data.PomFile;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public final class DefaultMavenProjectCache
        implements
            MavenProjectCache
{
    public static final ModelBuildingResult toModelBuildingResult(
            final ModelBuilder modelBuilder,
            final ModelBuildingRequest modelBuildingRequest)
    {
        try
        {
            return modelBuilder.build(modelBuildingRequest);
        }
        catch (final ModelBuildingException e)
        {
            throw new SmarterMavenException("Unable to create a ModelBuildingResult.", e);
        }
    }

    public static final <KEY, VALUE> VALUE getValueFromCache(
            final Cache<KEY, VALUE> cache,
            final KEY key,
            final Callable<VALUE> callable)
    {
        try
        {
            return cache.get(key, callable);
        }
        catch (final ExecutionException e)
        {
            throw new SmarterMavenException("Unable to get the requested value from the cache.", e);
        }
    }

    public static final class MavenProjectCallable
            implements
                Callable<MavenProject>
    {
        private final PomFile pomFile;

        public MavenProjectCallable(final PomFile pomFile)
        {
            this.pomFile = pomFile;
        }

        @Override
        public MavenProject call()
        {
            final ModelBuildingResult modelBuildingResult = makeModelBuildingResult();
            final Model model = modelBuildingResult.getEffectiveModel();
            final MavenProject mavenProject = new MavenProject(model);
            mavenProject.setFile(pomFile.value());
            return mavenProject;
        }

        public ModelBuildingResult makeModelBuildingResult()
        {
            final ModelBuilder modelBuilder = new DefaultModelBuilderFactory().newInstance();
            final ModelBuildingRequest modelBuildingRequest = makeModelBuildingRequest();
            return toModelBuildingResult(modelBuilder, modelBuildingRequest);
        }

        public ModelBuildingRequest makeModelBuildingRequest()
        {
            final ModelBuildingRequest modelBuildingRequest = new DefaultModelBuildingRequest();
            modelBuildingRequest.setPomFile(pomFile.value());
            modelBuildingRequest.setProcessPlugins(false);
            modelBuildingRequest.setValidationLevel(ModelBuildingRequest.VALIDATION_LEVEL_MINIMAL);
            return modelBuildingRequest;
        }
    }

    private final Cache<PomFile, MavenProject> mavenProjectCache;

    public DefaultMavenProjectCache()
    {
        mavenProjectCache = CacheBuilder.newBuilder().build();
    }

    @Override
    public MavenProject get(final PomFile pomFile)
    {
        return getValueFromCache(mavenProjectCache, pomFile, new MavenProjectCallable(pomFile));
    }
}

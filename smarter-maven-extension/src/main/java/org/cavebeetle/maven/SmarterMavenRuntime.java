package org.cavebeetle.maven;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.apache.maven.model.building.ModelBuilder;
import org.apache.maven.model.building.ModelBuildingException;
import org.apache.maven.model.building.ModelBuildingRequest;
import org.apache.maven.model.building.ModelBuildingResult;
import com.google.common.cache.Cache;

public final class SmarterMavenRuntime
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

    public static final File toCanonicalFile(final File file)
    {
        try
        {
            return file.getCanonicalFile();
        }
        catch (final IOException e)
        {
            throw new SmarterMavenException("Unable to get a canonical file.", e);
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
}

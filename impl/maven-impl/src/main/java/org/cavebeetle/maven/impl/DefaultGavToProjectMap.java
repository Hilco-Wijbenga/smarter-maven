package org.cavebeetle.maven.impl;

import static com.google.common.collect.Maps.newHashMap;
import java.util.Iterator;
import java.util.Map;
import javax.inject.Singleton;
import org.cavebeetle.maven.Gav;
import org.cavebeetle.maven.GavToProjectMap;
import org.cavebeetle.maven.Project;

/**
 * The implementation of {@code GavToProjectMap}.
 */
public final class DefaultGavToProjectMap
        implements
            GavToProjectMap
{
    /**
     * The implementation of {@code GavToProjectMap.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        @Override
        public GavToProjectMap newGavToProjectMap()
        {
            return new DefaultGavToProjectMap();
        }
    }

    private final Map<Gav, Project> gavToProjectMap;

    /**
     * Creates a new {@code DefaultGavToProjectMap}.
     */
    public DefaultGavToProjectMap()
    {
        gavToProjectMap = newHashMap();
    }

    @Override
    public Project getProject(final Gav gav)
    {
        return gavToProjectMap.get(gav);
    }

    @Override
    public void putProject(final Gav gav, final Project project)
    {
        gavToProjectMap.put(gav, project);
    }

    @Override
    public boolean containsProjectForGav(final Gav gav)
    {
        return gavToProjectMap.containsKey(gav);
    }

    @Override
    public Iterator<Gav> iterator()
    {
        return gavToProjectMap.keySet().iterator();
    }
}

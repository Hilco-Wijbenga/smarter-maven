package org.cavebeetle.maven2.impl;

import java.util.Iterator;
import org.cavebeetle.maven2.GavToProjectMapper;
import org.cavebeetle.maven2.MavenProjectCache;
import org.cavebeetle.maven2.ProjectFinder;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.PomFile;
import org.cavebeetle.maven2.data.Project;
import com.google.common.base.Optional;

public final class DefaultProjectFinder
        implements
            ProjectFinder
{
    private final GavToProjectMapper gavToProjectMapper;

    public DefaultProjectFinder(final MavenProjectCache mavenProjectCache, final PomFile pomFile)
    {
        gavToProjectMapper = new DefaultGavToProjectMapper(mavenProjectCache, pomFile);
    }

    @Override
    public Optional<Project> getProject(final Gav gav)
    {
        return gavToProjectMapper.map(gav);
    }

    @Override
    public Iterator<Project> iterator()
    {
        return gavToProjectMapper.projects().iterator();
    }
}

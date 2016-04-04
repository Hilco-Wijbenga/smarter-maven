package org.cavebeetle.maven2;

import java.util.Iterator;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.PomFile;
import org.cavebeetle.maven2.data.Project;
import com.google.common.base.Optional;

public final class ProjectFinder
        implements
            Iterable<Project>
{
    private final GavToProjectMapper gavToProjectMapper;

    public ProjectFinder(final MavenProjectCache mavenProjectCache, final PomFile pomFile)
    {
        gavToProjectMapper = new GavToProjectMapper(mavenProjectCache, pomFile);
    }

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

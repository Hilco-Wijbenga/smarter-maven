package org.cavebeetle.maven2.impl;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven2.GavMapper;
import org.cavebeetle.maven2.GavToProjectMapper;
import org.cavebeetle.maven2.MavenProjectCache;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.Module;
import org.cavebeetle.maven2.data.Packaging;
import org.cavebeetle.maven2.data.PomFile;
import org.cavebeetle.maven2.data.Project;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public final class DefaultGavToProjectMapper
        implements
            GavToProjectMapper
{
    public static StrictMap<Gav, Project> initialize(
            final MavenProjectCache mavenProjectCache,
            final PomFile rootPomFile)
    {
        final StrictMap.Mutable<Gav, Project> gavToProjectMap = StrictMap.Builder.make();
        final Set<PomFile> pomFiles = Sets.newHashSet();
        final Queue<PomFile> pomFileQueue = Lists.newLinkedList();
        pomFileQueue.add(rootPomFile);
        while (!pomFileQueue.isEmpty())
        {
            final PomFile pomFile = pomFileQueue.poll();
            if (!pomFiles.contains(pomFile))
            {
                pomFiles.add(pomFile);
                final MavenProject mavenProject = mavenProjectCache.get(pomFile);
                addProject(gavToProjectMap, pomFile, mavenProject);
                for (final String moduleName : mavenProject.getModules())
                {
                    final Module module = Module.make(pomFile, moduleName);
                    pomFileQueue.offer(module.value());
                }
            }
        }
        return gavToProjectMap.freeze();
    }

    public static void addProject(
            final StrictMap.Mutable<Gav, Project> gavToProjectMap,
            final PomFile pomFile,
            final MavenProject mavenProject)
    {
        final Gav gav = GavMapper.MAVEN_PROJECT_TO_GAV.map(mavenProject);
        final Packaging type = Packaging.make(mavenProject.getPackaging());
        final Project project = Project.make(gav, type, pomFile);
        gavToProjectMap.put(gav, project);
    }

    private final StrictMap<Gav, Project> gavToProjectMap;
    private final List<Project> projects;

    public DefaultGavToProjectMapper(final MavenProjectCache mavenProjectCache, final PomFile pomFile)
    {
        gavToProjectMap = initialize(mavenProjectCache, pomFile);
        final List<Project> projects_ = Lists.newArrayList();
        for (final Gav gav : gavToProjectMap)
        {
            final Project project = gavToProjectMap.get(gav);
            projects_.add(project);
        }
        projects = ImmutableList.copyOf(projects_);
    }

    @Override
    public Optional<Project> map(final Gav gav)
    {
        return Optional.fromNullable(gavToProjectMap.get(gav));
    }

    @Override
    public List<Project> projects()
    {
        return projects;
    }
}

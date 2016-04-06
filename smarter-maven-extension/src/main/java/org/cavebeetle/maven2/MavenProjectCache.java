package org.cavebeetle.maven2;

import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven2.data.PomFile;

public interface MavenProjectCache
{
    MavenProject get(PomFile pomFile);
}

package org.cavebeetle.maven2;

import java.util.Set;
import org.cavebeetle.maven2.data.DownstreamProject;
import org.cavebeetle.maven2.data.Project;

public interface DownstreamProjectFinder
{
    Set<DownstreamProject> get(Project project);
}

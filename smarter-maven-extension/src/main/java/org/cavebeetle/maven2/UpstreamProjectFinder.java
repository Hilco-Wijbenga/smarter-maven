package org.cavebeetle.maven2;

import java.util.Set;
import org.cavebeetle.maven2.data.Project;
import org.cavebeetle.maven2.data.UpstreamProject;

public interface UpstreamProjectFinder
{
    Set<UpstreamProject> get(Project project);
}

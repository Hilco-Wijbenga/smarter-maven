package org.cavebeetle.maven2;

import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.Project;
import com.google.common.base.Optional;

public interface ProjectFinder
        extends
            Iterable<Project>
{
    Optional<Project> getProject(Gav gav);
}

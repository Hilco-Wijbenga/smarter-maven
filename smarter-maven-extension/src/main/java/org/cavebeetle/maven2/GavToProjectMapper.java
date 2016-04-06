package org.cavebeetle.maven2;

import java.util.List;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.Project;
import com.google.common.base.Optional;

public interface GavToProjectMapper
{
    Optional<Project> map(Gav gav);

    List<Project> projects();
}

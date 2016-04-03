package org.cavebeetle.maven2.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

public final class Project
        implements
            Comparable<Project>
{
    private static final Interner<Project> INTERNER = Interners.newWeakInterner();

    public static final Project make(final Gav gav, final Packaging type, final PomFile pomFile)
    {
        return INTERNER.intern(new Project(gav, type, pomFile));
    }

    private final Gav gav;
    private final Packaging packaging;
    private final PomFile pomFile;

    public Project(final Gav gav, final Packaging packaging, final PomFile pomFile)
    {
        Preconditions.checkNotNull(gav, "Missing 'gav'.");
        Preconditions.checkNotNull(packaging, "Missing 'packaging'.");
        Preconditions.checkNotNull(pomFile, "Missing 'pomFile'.");
        this.gav = gav;
        this.packaging = packaging;
        this.pomFile = pomFile;
    }

    public Gav gav()
    {
        return gav;
    }

    public Packaging packaging()
    {
        return packaging;
    }

    public PomFile pomFile()
    {
        return pomFile;
    }

    @Override
    public String toString()
    {
        return String.format("[Project gav=%s packaging=%s pomFile=%s]", gav, packaging, pomFile);
    }

    @Override
    public int compareTo(final Project other)
    {
        return ComparisonChain.start()
                .compare(gav, other.gav())
                .compare(packaging, other.packaging())
                .compare(pomFile, other.pomFile())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + gav.hashCode();
        result = prime * result + packaging.hashCode();
        result = prime * result + pomFile.hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final Project other = (Project) object;
        return compareTo(other) == 0;
    }
}

package org.cavebeetle.maven2.data;

import java.io.File;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

public final class PomFile
        implements
            Comparable<PomFile>
{
    private final File value;

    public PomFile(final File value)
    {
        Preconditions.checkArgument(value != null && value.isFile(), "Missing 'value'.");
        this.value = value;
    }

    public File value()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return String.format("[PomFile value='%s']", value.getPath());
    }

    @Override
    public int compareTo(final PomFile other)
    {
        return ComparisonChain
                .start()
                .compare(value, other.value())
                .result();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + value.hashCode();
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
        final PomFile other = (PomFile) object;
        return compareTo(other) == 0;
    }
}

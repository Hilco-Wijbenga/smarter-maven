package org.cavebeetle.maven2.data;

import java.io.File;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

public final class Module
        implements
            Comparable<Module>
{
    private static final Interner<Module> INTERNER = Interners.newWeakInterner();

    public static final Module make(final PomFile pomFile, final String module)
    {
        final File moduleFile = new File(pomFile.value().getParentFile(), module);
        final PomFile modulePomFile = moduleFile.isFile()
            ? PomFile.make(moduleFile)
            : PomFile.make(new File(moduleFile, "pom.xml"));
        return INTERNER.intern(new Module(modulePomFile));
    }

    private final PomFile value;

    public Module(final PomFile value)
    {
        Preconditions.checkArgument(value != null, "Missing 'value'.");
        this.value = value;
    }

    public PomFile value()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return String.format("[Module value='%s']", value);
    }

    @Override
    public int compareTo(final Module other)
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
        final Module other = (Module) object;
        return compareTo(other) == 0;
    }
}

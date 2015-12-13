package org.cavebeetle.maven;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

public final class Path
{
    public static final Path ROOT = new Path();
    private final Path parent;
    private final Key key;

    private Path(final Path parent, final Key key)
    {
        checkNotNull(parent, "Missing 'parent'.");
        checkNotNull(key, "Missing 'key'.");
        this.parent = parent;
        this.key = key;
    }

    private Path()
    {
        parent = null;
        key = null;
    }

    @Override
    public int hashCode()
    {
        final int prime = 349;
        int result = 1;
        result = prime * result + parent.hashCode();
        result = prime * result + key.hashCode();
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
        final Path other = (Path) object;
        return parent.equals(other.parent) && key.equals(other.key);
    }

    @Override
    public String toString()
    {
        return toStringHelper(getClass())
                .add("parent", parent)
                .add("key", key)
                .toString();
    }

    public Path extend(final Key key)
    {
        return new Path(this, key);
    }

    public String toText()
    {
        if (this == ROOT)
        {
            return "/";
        }
        else
        {
            final StringBuilder sb = new StringBuilder();
            if (parent != ROOT)
            {
                sb.append(parent.toText());
            }
            sb.append('/').append(key.key());
            return sb.toString();
        }
    }
}

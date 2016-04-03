package org.cavebeetle.maven;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;

public final class Path
{
    public static final Path ROOT = new Path();
    private final Key head;
    private final Path tail;

    private Path(final Path parent, final Key key)
    {
        checkNotNull(parent, "Missing 'parent'.");
        checkNotNull(key, "Missing 'key'.");
        head = parent.head();
        tail = new Path(parent.tail(), key);
    }

    private Path()
    {
        head = null;
        tail = null;
    }

    public Key head()
    {
        return head;
    }

    public Path tail()
    {
        return tail;
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
            sb.append('/').append(head);
            if (tail != ROOT)
            {
                sb.append(tail.toText());
            }
            return sb.toString();
        }
    }

    @Override
    public int hashCode()
    {
        final int prime = 349;
        if (this == ROOT)
        {
            return prime;
        }
        int result = 1;
        result = prime * result + head.hashCode();
        result = prime * result + tail.hashCode();
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
        if (this == ROOT || other == ROOT)
        {
            return this == other;
        }
        return head.equals(other.head) && tail.equals(other.tail);
    }

    @Override
    public String toString()
    {
        return toStringHelper(getClass())
                .add("head", head)
                .add("tail", tail)
                .toString();
    }
}

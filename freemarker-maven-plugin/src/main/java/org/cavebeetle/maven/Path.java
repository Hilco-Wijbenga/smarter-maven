package org.cavebeetle.maven;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.ImmutableList;

public final class Path
        implements
            Iterable<Key>
{
    public static final Path ROOT = new Path();
    private final List<Key> keys;

    public Path(final Path path, final Key key)
    {
        checkNotNull(path, "Missing 'path'.");
        checkNotNull(key, "Missing 'key'.");
        keys = ImmutableList.<Key> builder()
                .addAll(path.keys)
                .add(key)
                .build();
    }

    public Path(final Key key)
    {
        checkNotNull(key, "Missing 'key'.");
        keys = ImmutableList.of(key);
    }

    private Path()
    {
        keys = ImmutableList.<Key> builder().build();
    }

    @Override
    public int hashCode()
    {
        final int prime = 349;
        int result = 1;
        result = prime * result + keys.hashCode();
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
        return keys.equals(other.keys);
    }

    @Override
    public String toString()
    {
        return toStringHelper(getClass()).add("keys", keys).toString();
    }

    @Override
    public Iterator<Key> iterator()
    {
        return keys.iterator();
    }

    public String toText()
    {
        final StringBuilder sb = new StringBuilder();
        for (final Key key : keys)
        {
            sb.append('/').append(key.value());
        }
        return sb.toString();
    }
}

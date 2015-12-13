package org.cavebeetle.maven;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Interners.newWeakInterner;
import com.google.common.collect.Interner;

public final class Key
{
    private static final Interner<Key> INTERNER = newWeakInterner();

    public static final Key newKey(final String key)
    {
        return INTERNER.intern(new Key(key));
    }

    private final String key;

    private Key(final String key)
    {
        checkArgument(key != null && !key.isEmpty(), "Missing 'key'.");
        this.key = key;
    }

    @Override
    public int hashCode()
    {
        final int prime = 271;
        int result = 1;
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
        final Key other = (Key) object;
        return key.equals(other.key);
    }

    @Override
    public String toString()
    {
        return toStringHelper(getClass())
                .addValue(key)
                .toString();
    }

    public String key()
    {
        return key;
    }
}

package org.cavebeetle.maven;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;

public final class Key
{
    private final String value;

    public Key(final String value)
    {
        checkArgument(value != null && !value.isEmpty(), "Missing 'value'.");
        this.value = value;
    }

    @Override
    public int hashCode()
    {
        final int prime = 271;
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
        final Key other = (Key) object;
        return value.equals(other.value);
    }

    @Override
    public String toString()
    {
        return toStringHelper(getClass()).addValue(value).toString();
    }

    public String value()
    {
        return value;
    }
}

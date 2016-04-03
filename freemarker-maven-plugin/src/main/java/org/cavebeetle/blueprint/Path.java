package org.cavebeetle.blueprint;

public final class Path
{
    private final Key parent;
    private final Key key;

    private Path()
    {
        parent = null;
        key = null;
    }

    public Path(final Key key)
    {
        parent = null;
        this.key = key;
    }

    public Path(final Key parent, final Key key)
    {
        this.parent = parent;
        this.key = key;
    }
}

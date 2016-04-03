package org.cavebeetle.blueprint;

public final class Leaf
        implements
            Component
{
    private final String name;
    private String value;

    public Leaf(final String name, final String value)
    {
        this.name = name;
        this.value = value;
    }

    public String name()
    {
        return name;
    }

    public String value()
    {
        return value;
    }

    public void setValue(final String value)
    {
        this.value = value;
    }
}

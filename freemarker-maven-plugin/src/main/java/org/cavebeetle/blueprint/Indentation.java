package org.cavebeetle.blueprint;

public final class Indentation
{
    public static final Indentation NO_INDENTATION = new Indentation("");
    private final String indentation;

    private Indentation(final String indentation)
    {
        this.indentation = indentation;
    }

    public Indentation indent()
    {
        return new Indentation(indentation + "    ");
    }

    public String text()
    {
        return indentation;
    }
}

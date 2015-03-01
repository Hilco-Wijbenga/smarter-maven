package org.cavebeetle.maven.plugins;

public final class DefaultPomPartVerbatim
        implements
            PomPart.Verbatim
{
    private final String text;

    public DefaultPomPartVerbatim(
            final String text)
    {
        this.text = text;
    }
}

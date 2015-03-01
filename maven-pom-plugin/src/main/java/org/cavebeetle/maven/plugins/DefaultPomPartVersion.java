package org.cavebeetle.maven.plugins;

public final class DefaultPomPartVersion
        implements
            PomPart.Version
{
    private final String originalText;

    public DefaultPomPartVersion(
            final String originalText)
    {
        this.originalText = originalText;
    }
}

package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkArgument;
import javax.inject.Singleton;
import org.cavebeetle.maven.Version;

/**
 * The implementation of {@code Version}.
 */
public final class DefaultVersion
        implements
            Version
{
    /**
     * The implementation of {@code Version.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        @Override
        public Version newVersion(
                final String version)
        {
            return new DefaultVersion(version);
        }
    }

    private final String version;

    /**
     * Creates a new {@code DefaultVersion}.
     *
     * @param version
     *            the text representing the version.
     */
    public DefaultVersion(
            final String version)
    {
        checkArgument(version != null && !version.isEmpty(), "Missing 'version'.");
        this.version = version;
    }

    @Override
    public int hashCode()
    {
        final int prime = 37;
        final int result = prime + version.hashCode();
        return result;
    }

    @Override
    public boolean equals(
            final Object object)
    {
        if (this == object)
        {
            return true;
        }
        if (object == null || getClass() != object.getClass())
        {
            return false;
        }
        final DefaultVersion other = (DefaultVersion) object;
        return version.equals(other.version);
    }

    @Override
    public String toString()
    {
        return version;
    }
}

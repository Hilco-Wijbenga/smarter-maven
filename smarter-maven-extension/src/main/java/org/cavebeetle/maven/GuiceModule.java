package org.cavebeetle.maven;

import org.cavebeetle.io.impl.IoGuiceModule;
import org.cavebeetle.maven.impl.MavenGuiceModule;
import com.google.inject.AbstractModule;

/**
 * The Guice module describing the required bindings.
 */
public final class GuiceModule
        extends
            AbstractModule
{
    @Override
    public void configure()
    {
        install(new MavenGuiceModule());
        install(new IoGuiceModule());
    }
}

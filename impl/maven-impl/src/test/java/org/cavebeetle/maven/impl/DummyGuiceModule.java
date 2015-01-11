package org.cavebeetle.maven.impl;

import org.cavebeetle.io.IoApi;
import com.google.inject.AbstractModule;

/**
 * A dummy Guice module for testing.
 */
public final class DummyGuiceModule
        extends
            AbstractModule
{
    @Override
    public void configure()
    {
        bind(IoApi.class).to(DummyIoApi.class);
    }
}

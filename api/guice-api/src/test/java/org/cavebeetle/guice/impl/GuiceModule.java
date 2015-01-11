package org.cavebeetle.guice.impl;

import org.cavebeetle.guice.Thing;
import com.google.inject.AbstractModule;

/**
 * The dummy Guice module for testing.
 */
public final class GuiceModule
        extends
            AbstractModule
{
    @Override
    public void configure()
    {
        bind(Thing.class).to(DefaultThing.class);
    }
}

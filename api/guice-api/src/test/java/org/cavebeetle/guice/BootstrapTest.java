package org.cavebeetle.guice;

import static org.cavebeetle.guice.Bootstrap.GUICE;
import static org.cavebeetle.guice.Bootstrap.getGuiceModuleProperties;
import static org.cavebeetle.guice.Bootstrap.loadGuiceModule;
import static org.cavebeetle.guice.Bootstrap.loadPropertiesFromUrl;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.cavebeetle.guice.impl.DefaultThing;
import org.junit.Test;

/**
 * The unit tests for {@code Bootstrap}.
 */
public final class BootstrapTest
{
    /**
     * Tests that requesting a Thing instance works correctly.
     */
    @Test
    public final void requesting_a_Thing_instance_works_correctly()
    {
        final Thing thing = GUICE.getInstance(Thing.class);
        assertNotNull(thing);
        assertTrue(thing instanceof DefaultThing);
    }

    /**
     * Tests that a failure to load Guice module properties is handled appropriately.
     */
    @Test
    public final void a_failure_to_load_Guice_module_properties_is_handled_appropriately()
    {
        try
        {
            getGuiceModuleProperties(null, null);
            fail("Expected a NullPointerException.");
        }
        catch (final IllegalStateException e)
        {
            assertTrue(e.getCause() instanceof NullPointerException);
        }
    }

    /**
     * Tests that a failure to load properties from a URL is handled appropriately.
     */
    @Test
    public final void a_failure_to_load_properties_from_a_URL_is_handled_appropriately()
    {
        try
        {
            loadPropertiesFromUrl(null);
            fail("Expected a NullPointerException.");
        }
        catch (final IllegalStateException e)
        {
            assertTrue(e.getCause() instanceof NullPointerException);
        }
    }

    /**
     * Tests that a failure to load a Guice module is handled appropriately.
     */
    @Test
    public final void a_failure_to_load_a_Guice_module_is_handled_appropriately()
    {
        try
        {
            loadGuiceModule(null, null);
            fail("Expected a NullPointerException.");
        }
        catch (final IllegalStateException e)
        {
            assertTrue(e.getCause() instanceof NullPointerException);
        }
    }
}

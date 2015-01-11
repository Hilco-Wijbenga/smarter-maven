package org.cavebeetle.io.impl;

import static com.google.inject.Guice.createInjector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.cavebeetle.io.FileWriter;
import org.cavebeetle.io.InputStream;
import org.cavebeetle.io.InternalApi;
import org.cavebeetle.io.IoApi;
import org.cavebeetle.io.SourceFiles;
import org.cavebeetle.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import com.google.inject.Injector;

/**
 * The unit tests for Guice.
 */
public final class GuiceModuleTest
{
    private Injector injector;

    /**
     * Sets up each unit test case.
     */
    @Before
    public void setUp()
    {
        final GuiceModule guiceModule = new GuiceModule();
        injector = createInjector(guiceModule);
    }

    /**
     * Checks that no Guice bindings were missed.
     */
    @Test
    public final void check_that_no_Guice_bindings_were_missed()
    {
        assertEquals(6, injector.getBindings().size() - 3);
    }

    /**
     * Checks that Guice creates a singleton {@code IoApi}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_IoApi()
    {
        final Class<IoApi> instanceType = IoApi.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code InternalApi}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_InternalApi()
    {
        final Class<InternalApi> instanceType = InternalApi.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates the same instance for both {@code IoApi} and {@code InternalApi}.
     */
    @Test
    public final void check_that_Guice_creates_the_same_instance_for_both_IoApi_and_InternalApi()
    {
        assertSame(injector.getInstance(IoApi.class), injector.getInstance(InternalApi.class));
    }

    /**
     * Checks that Guice creates a singleton {@code InputStream.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_InputStream_Builder()
    {
        final Class<InputStream.Builder> instanceType = InputStream.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code SourceFiles.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_SourceFiles_Builder()
    {
        final Class<SourceFiles.Builder> instanceType = SourceFiles.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code StringWriter.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_StringWriter_Builder()
    {
        final Class<StringWriter.Builder> instanceType = StringWriter.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }

    /**
     * Checks that Guice creates a singleton {@code FileWriter.Builder}.
     */
    @Test
    public final void check_that_Guice_creates_a_singleton_FileWriter_Builder()
    {
        final Class<FileWriter.Builder> instanceType = FileWriter.Builder.class;
        assertNotNull(injector.getInstance(instanceType));
        assertSame(injector.getInstance(instanceType), injector.getInstance(instanceType));
    }
}

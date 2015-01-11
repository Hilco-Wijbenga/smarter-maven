package org.cavebeetle.guice;

import static com.google.inject.Guice.createInjector;
import static java.lang.Thread.currentThread;
import static java.util.Collections.list;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;

/**
 * {@code Bootstrap} facilitates bootstrapping dependency injection.
 */
public enum Bootstrap
{
    /**
     * The global singleton providing access to Guice.
     */
    GUICE;
    private final Injector injector;

    /**
     * Gets a {@code List<URL>} of all files with the given name on the class path.
     *
     * @param classLoader
     *            the {@code ClassLoader} to use.
     * @param propertiesFileName
     *            the name of the file to look for.
     * @return a {@code List<URL>} of all files with the given name on the class path.
     */
    public static final List<URL> getGuiceModuleProperties(
            final ClassLoader classLoader,
            final String propertiesFileName)
    {
        try
        {
            return list(classLoader.getResources(propertiesFileName));
        }
        catch (final Exception e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Loads the properties available in the file identified by the given {@code URL}.
     *
     * @param url
     *            the {@code URL} to load.
     * @return a {@code Properties} instance with the properties provided by the given {@code URL}.
     */
    public static final Properties loadPropertiesFromUrl(
            final URL url)
    {
        try
        {
            final Properties properties = new Properties();
            properties.load(url.openStream());
            return properties;
        }
        catch (final Exception e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Loads the Guice module identified by the given class name.
     *
     * @param classLoader
     *            the {@code ClassLoader} to use.
     * @param guiceModuleClassName
     *            the name of the Guice module to load.
     * @return the Guice module identified by the given class name.
     */
    public static final AbstractModule loadGuiceModule(
            final ClassLoader classLoader,
            final String guiceModuleClassName)
    {
        try
        {
            final Class<?> type = classLoader.loadClass(guiceModuleClassName);
            @SuppressWarnings("unchecked")
            final Class<? extends AbstractModule> guiceModuleClass = (Class<? extends AbstractModule>) type;
            return guiceModuleClass.newInstance();
        }
        catch (final Exception e)
        {
            throw new IllegalStateException(e);
        }
    }

    private Bootstrap()
    {
        final List<AbstractModule> guiceModules = new ArrayList<AbstractModule>();
        final ClassLoader classLoader = currentThread().getContextClassLoader();
        for (final URL url : getGuiceModuleProperties(classLoader, "guice-module.properties"))
        {
            final Properties properties = loadPropertiesFromUrl(url);
            final String guiceModuleClassName = properties.getProperty("guice-module");
            final AbstractModule guiceModule = loadGuiceModule(classLoader, guiceModuleClassName);
            guiceModules.add(guiceModule);
        }
        injector = createInjector(guiceModules);
    }

    /**
     * Gets an instance of the given type.
     *
     * @param requestedType
     *            the type of the requested instance.
     * @return an instance of the given type.
     */
    public <T> T getInstance(
            final Class<T> requestedType)
    {
        return injector.getInstance(requestedType);
    }
}

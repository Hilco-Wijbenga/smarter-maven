package org.cavebeetle.io.impl;

import static com.google.inject.Guice.createInjector;
import static java.io.File.createTempFile;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.io.IOException;
import org.cavebeetle.io.FileWriter;
import org.cavebeetle.io.InputStream;
import org.cavebeetle.io.SourceFiles;
import org.cavebeetle.io.StringWriter;
import org.cavebeetle.io.TextFile;
import org.cavebeetle.io.TextFileReader;
import org.junit.Before;
import org.junit.Test;
import com.google.inject.Injector;

/**
 * The unit tests for {@code DefaultInternalApi}.
 */
public final class DefaultInternalApiTest
{
    private DefaultInternalApi internalApi;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        final Injector injector = createInjector(new IoGuiceModule());
        internalApi = new DefaultInternalApi(injector);
    }

    /**
     * Tests that a {@code DefaultInternalApi} instance successfully creates an {@code InputStream} from a {@code File}.
     */
    @Test
    public final void a_DefaultInternalApi_instance_successfully_creates_an_InputStream_from_a_File()
    {
        final File file = new File("pom.xml");
        final InputStream inputStream = internalApi.newInputStream(file);
        assertNotNull(inputStream);
    }

    /**
     * Tests that a {@code DefaultInternalApi} instance successfully creates an {@code InputStream} from text.
     */
    @Test
    public final void a_DefaultInternalApi_instance_successfully_creates_an_InputStream_from_text()
    {
        final InputStream inputStream = internalApi.newInputStream("Hello world!");
        assertNotNull(inputStream);
    }

    /**
     * Tests that a {@code DefaultInternalApi} instance successfully creates a {@code SourceFiles}.
     */
    @Test
    public final void a_DefaultInternalApi_instance_successfully_creates_a_SourceFiles()
    {
        final File baseDir = new File(".");
        final SourceFiles sourceFiles = internalApi.newSourceFiles(baseDir);
        assertNotNull(sourceFiles);
    }

    /**
     * Tests that a {@code DefaultInternalApi} instance successfully creates a {@code StringWriter}.
     */
    @Test
    public final void a_DefaultInternalApi_instance_successfully_creates_a_StringWriter()
    {
        final StringWriter stringWriter = internalApi.newWriter();
        assertNotNull(stringWriter);
    }

    /**
     * Tests that a {@code DefaultInternalApi} instance successfully creates a {@code FileWriter}.
     *
     * @throws IOException
     *             if an IO problem occurs.
     */
    @Test
    public final void a_DefaultInternalApi_instance_successfully_creates_a_FileWriter()
            throws IOException
    {
        final File file = createTempFile("io-impl", ".txt");
        final FileWriter fileWriter = internalApi.newWriter(file);
        assertNotNull(fileWriter);
    }

    /**
     * Tests that a {@code DefaultInternalApi} instance successfully creates a {@code TextFileReader} from a
     * {@code File}.
     */
    @Test
    public final void a_DefaultInternalApi_instance_successfully_creates_a_TextFileReader_from_a_File()
    {
        final File file = new File("pom.xml");
        final TextFileReader textFileReader = internalApi.newTextFileReader(file);
        assertNotNull(textFileReader);
    }

    /**
     * Tests that a {@code DefaultInternalApi} instance successfully creates a {@code TextFileReader} from text.
     */
    @Test
    public final void a_DefaultInternalApi_instance_successfully_creates_a_TextFileReader_from_text()
    {
        final TextFileReader textFileReader = internalApi.newTextFileReader("Hello world!");
        assertNotNull(textFileReader);
    }

    /**
     * Tests that a {@code DefaultInternalApi} instance successfully creates a {@code TextFile} from a {@code File}.
     */
    @Test
    public final void a_DefaultInternalApi_instance_successfully_creates_a_TextFile_from_a_File()
    {
        final File file = new File("pom.xml");
        final TextFile textFile = internalApi.newTextFile(file);
        assertNotNull(textFile);
    }
}

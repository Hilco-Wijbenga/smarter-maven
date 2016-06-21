package org.cavebeetle.io.impl;

import static java.io.File.createTempFile;
import static java.lang.Character.MIN_HIGH_SURROGATE;
import static java.lang.Character.MIN_LOW_SURROGATE;
import static java.lang.Character.MIN_SUPPLEMENTARY_CODE_POINT;
import static java.lang.String.copyValueOf;
import static org.cavebeetle.io.IoApi.END_OF_LINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.cavebeetle.io.IoException;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code DefaultFileWriter}.
 */
public final class DefaultFileWriterTest
{
    private java.io.FileWriter mockDelegate;
    private DefaultFileWriter fileWriter;
    private String text;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        mockDelegate = mock(java.io.FileWriter.class);
        fileWriter = new DefaultFileWriter(mockDelegate);
        text = "Hello World!";
    }

    /**
     * Tests that a missing file is handled correctly.
     *
     * @throws IOException
     *             if something goes wrong.
     */
    @Test
    public final void a_missing_file_is_handled_correctly()
            throws IOException
    {
        try
        {
            new DefaultFileWriter((File) null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'file'.", e.getMessage());
        }
    }

    /**
     * Tests that an invalid file is handled correctly.
     *
     * @throws IOException
     *             if something goes wrong.
     */
    @Test
    public final void an_invalid_file_is_handled_correctly()
            throws IOException
    {
        final File file = new File(".");
        try
        {
            new DefaultFileWriter(file);
            fail("Expected an IoException.");
        }
        catch (final IoException e)
        {
            assertTrue(e.getCause() instanceof IOException);
        }
    }

    /**
     * Tests that a complete write and close scenario works.
     *
     * @throws IOException
     *             if something goes wrong.
     */
    @Test
    public final void a_complete_write_and_close_scenario_works()
            throws IOException
    {
        final File file = createTempFile("dummy", "ignore");
        final DefaultFileWriter otherFileWriter = new DefaultFileWriter(file);
        otherFileWriter.write(text);
        otherFileWriter.close();
        final FileReader fileReader = new FileReader(file);
        final char[] buffer = new char[100];
        final int charCount = fileReader.read(buffer);
        assertEquals(text.length(), charCount);
        assertEquals(text, copyValueOf(buffer, 0, charCount));
        fileReader.close();
    }

    /**
     * Tests that closing a {@code FileWriter} closes its delegate.
     *
     * @throws IOException
     *             if something goes wrong.
     */
    @Test
    public final void closing_a_FileWriter_closes_its_delegate()
            throws IOException
    {
        fileWriter.close();
        verify(mockDelegate).close();
    }

    /**
     * Tests that an exception thrown while closing a {@code FileWriter} is handled correctly.
     *
     * @throws IOException
     *             if something goes wrong.
     */
    @Test
    public final void an_exception_thrown_while_closing_a_FileWriter_is_handled_correctly()
            throws IOException
    {
        final Throwable oops = new IOException("Oops");
        doThrow(oops).when(mockDelegate).close();
        try
        {
            fileWriter.close();
            fail("Expected an IoException.");
        }
        catch (final IoException e)
        {
            assertSame(oops, e.getCause());
        }
    }

    /**
     * Tests that a missing text is handled correctly.
     *
     * @throws IOException
     *             if something goes wrong.
     */
    @Test
    public final void a_missing_text_is_handled_correctly()
            throws IOException
    {
        try
        {
            fileWriter.write(null);
            fail("Expected an NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'text'.", e.getMessage());
        }
    }

    /**
     * Tests that writing to a {@code FileWriter} writes to its delegate.
     *
     * @throws IOException
     *             if something goes wrong.
     */
    @Test
    public final void writing_to_a_FileWriter_writes_to_its_delegate()
            throws IOException
    {
        fileWriter.write(text);
        verify(mockDelegate).write(text);
    }

    /**
     * Tests that writing a line to a {@code FileWriter} writes to its delegate.
     *
     * @throws IOException
     *             if something goes wrong.
     */
    @Test
    public final void writing_a_line_to_a_FileWriter_writes_to_its_delegate()
            throws IOException
    {
        fileWriter.writeLine(text);
        verify(mockDelegate).write(eq(text + END_OF_LINE));
    }

    /**
     * Tests that an exception thrown while writing to a {@code FileWriter} is handled correctly.
     *
     * @throws IOException
     *             if something goes wrong.
     */
    @Test
    public final void an_exception_thrown_while_writing_to_a_FileWriter_is_handled_correctly()
            throws IOException
    {
        final Throwable oops = new IOException("Oops");
        doThrow(oops).when(mockDelegate).write(text);
        try
        {
            fileWriter.write(text);
            fail("Expected an IoException.");
        }
        catch (final IoException e)
        {
            assertSame(oops, e.getCause());
        }
    }

    /**
     * Tests that writing a single character code point to a {@code FileWriter} writes to its delegate.
     *
     * @throws IOException
     *             if something goes wrong.
     */
    @Test
    public final void writing_a_single_character_code_point_to_a_FileWriter_writes_to_its_delegate()
            throws IOException
    {
        fileWriter.write('a');
        verify(mockDelegate).write('a');
    }

    /**
     * Tests that writing a double character code point to a {@code FileWriter} writes to its delegate.
     *
     * @throws IOException
     *             if something goes wrong.
     */
    @Test
    public final void writing_a_double_character_code_point_to_a_FileWriter_writes_to_its_delegate()
            throws IOException
    {
        final int codePoint = 0x10000;
        fileWriter.write(codePoint);
        final char highSurrogate =
                (char) ((codePoint >>> 10) + MIN_HIGH_SURROGATE - (MIN_SUPPLEMENTARY_CODE_POINT >>> 10));
        final char lowSurrogate = (char) ((codePoint & 0x3ff) + MIN_LOW_SURROGATE);
        verify(mockDelegate).write(highSurrogate);
        verify(mockDelegate).write(lowSurrogate);
    }

    /**
     * Tests that an exception thrown while writing a code point to a {@code FileWriter} is handled correctly.
     *
     * @throws IOException
     *             if something goes wrong.
     */
    @Test
    public final void an_exception_thrown_while_writing_a_code_point_to_a_FileWriter_is_handled_correctly()
            throws IOException
    {
        final Throwable oops = new IOException("Oops");
        doThrow(oops).when(mockDelegate).write('a');
        try
        {
            fileWriter.write('a');
            fail("Expected an IoException.");
        }
        catch (final IoException e)
        {
            assertSame(oops, e.getCause());
        }
    }
}

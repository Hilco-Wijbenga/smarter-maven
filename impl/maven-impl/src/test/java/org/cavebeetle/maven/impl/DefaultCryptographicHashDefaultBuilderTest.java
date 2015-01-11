package org.cavebeetle.maven.impl;

import static org.cavebeetle.maven.CryptographicHashAlgorithm.SHA1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import org.cavebeetle.maven.CryptographicHash;
import org.cavebeetle.maven.InternalApi;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code DefaultCryptographicHash.DefaultBuilder}.
 */
public final class DefaultCryptographicHashDefaultBuilderTest
{
    private InternalApi mockInternalApi;
    private DefaultCryptographicHash.DefaultBuilder cryptographicHashBuilder;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        mockInternalApi = mock(InternalApi.class);
        cryptographicHashBuilder = new DefaultCryptographicHash.DefaultBuilder(mockInternalApi);
    }

    /**
     * Tests that a missing {@code InternalApi} is handled correctly.
     */
    @Test
    public final void a_missing_InternalApi_is_handled_correctly()
    {
        try
        {
            new DefaultCryptographicHash.DefaultBuilder(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'internalApi'.", e.getMessage());
        }
    }

    /**
     * Tests that a missing {@code CryptographicHashAlgorithm} in {CryptographicHash.Builder#newCryptographicHash} is
     * handled correctly.
     */
    @Test
    public final void a_missing_CryptographicHashAlgorithm_in_CryptographicHash_Builder_newCryptographicHash_is_handled_correctly()
    {
        try
        {
            cryptographicHashBuilder.newCryptographicHash(null);
            fail("Expected a NullPointerException.");
        }
        catch (final NullPointerException e)
        {
            assertEquals("Missing 'cryptographicHashAlgorithm'.", e.getMessage());
        }
    }

    /**
     * Tests that creating a new {@code CryptographicHash} returns a {@code CryptographicHash} instance.
     */
    @Test
    public final void creating_a_new_CryptographicHash_returns_a_CryptographicHash_instance()
    {
        final CryptographicHash cryptographicHash = cryptographicHashBuilder.newCryptographicHash(SHA1);
        assertNotNull(cryptographicHash);
    }
}

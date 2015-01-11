package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Character.forDigit;
import static java.lang.Character.toUpperCase;
import javax.inject.Singleton;
import org.cavebeetle.maven.Digest;

/**
 * The implementation of {@code Digest}.
 */
public final class DefaultDigest
        implements
            Digest
{
    /**
     * The implementation of {@code Digest.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        @Override
        public Digest newDigest(
                final byte[] bytes)
        {
            return new DefaultDigest(bytes);
        }
    }

    private static final String[] BYTE_AS_HEX;
    static
    {
        BYTE_AS_HEX = new String[256];
        final char[] hexDigit = new char[16];
        for (int i = 0; i < 16; i++)
        {
            hexDigit[i] = toUpperCase(forDigit(i, 16));
        }
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                final StringBuilder stringBuilder = new StringBuilder(2);
                stringBuilder.append(hexDigit[i]).append(hexDigit[j]);
                BYTE_AS_HEX[i * 16 + j] = stringBuilder.toString();
            }
        }
    }
    private final String digest;

    /**
     * Creates a new {@code DefaultDigest}.
     *
     * @param bytes
     *            the byte array that represents the digest.
     */
    public DefaultDigest(
            final byte[] bytes)
    {
        checkNotNull(bytes, "Missing 'bytes'.");
        final StringBuilder stringBuilder = new StringBuilder();
        for (final byte b : bytes)
        {
            stringBuilder.append(b < 0 ? BYTE_AS_HEX[b + 256] : BYTE_AS_HEX[b]);
        }
        digest = stringBuilder.toString();
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        final int result = prime + digest.hashCode();
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
        final DefaultDigest other = (DefaultDigest) object;
        return digest.equals(other.digest);
    }

    @Override
    public String toString()
    {
        return digest;
    }
}

package org.cavebeetle.maven.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.cavebeetle.io.IoApi.END_OF_LINE;
import java.security.MessageDigest;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cavebeetle.io.InputStream;
import org.cavebeetle.maven.CryptographicHash;
import org.cavebeetle.maven.CryptographicHashAlgorithm;
import org.cavebeetle.maven.Digest;
import org.cavebeetle.maven.InternalApi;

/**
 * The implementation of {@code CryptographicHash}.
 */
public final class DefaultCryptographicHash
        implements
            CryptographicHash
{
    /**
     * The implementation of {@code CryptographicHash.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        private final InternalApi internalApi;

        /**
         * Creates a new {@code DefaultBuilder}.
         *
         * @param internalApi
         *            the {@code InternalApi} instance.
         */
        @Inject
        public DefaultBuilder(final InternalApi internalApi)
        {
            checkNotNull(internalApi, "Missing 'internalApi'.");
            this.internalApi = internalApi;
        }

        @Override
        public CryptographicHash newCryptographicHash(final CryptographicHashAlgorithm cryptographicHashAlgorithm)
        {
            return new DefaultCryptographicHash(internalApi, cryptographicHashAlgorithm);
        }
    }

    private static final int KB = 1024;
    private static final int BUFFER_SIZE = 16 * KB;
    private final InternalApi internalApi;
    private final CryptographicHashAlgorithm cryptographicHashAlgorithm;

    /**
     * Creates a new {@code DefaultCryptographicHash}.
     *
     * @param internalApi
     *            the {@code InternalApi} instance.
     * @param cryptographicHashAlgorithm
     *            the cryptographic hash algorithm to use.
     */
    public DefaultCryptographicHash(
            final InternalApi internalApi,
            final CryptographicHashAlgorithm cryptographicHashAlgorithm)
    {
        checkNotNull(internalApi, "Missing 'internalApi'.");
        checkNotNull(cryptographicHashAlgorithm, "Missing 'cryptographicHashAlgorithm'.");
        this.internalApi = internalApi;
        this.cryptographicHashAlgorithm = cryptographicHashAlgorithm;
    }

    @Override
    public Digest generateDigest(final InputStream inputStream)
    {
        checkNotNull(inputStream, "Missing 'inputStream'.");
        final MessageDigest messageDigest = cryptographicHashAlgorithm.newMessageDigest();
        final byte[] buffer = new byte[BUFFER_SIZE];
        while (true)
        {
            final int byteCount = inputStream.read(buffer);
            if (byteCount == -1)
            {
                break;
            }
            messageDigest.update(buffer, 0, byteCount);
        }
        return internalApi.newDigest(messageDigest.digest());
    }

    @Override
    public Digest generateDigest(final Iterable<String> lines)
    {
        checkNotNull(lines, "Missing 'lines'.");
        final MessageDigest messageDigest = cryptographicHashAlgorithm.newMessageDigest();
        for (final String line : lines)
        {
            final String line_ = line + END_OF_LINE;
            messageDigest.update(line_.getBytes());
        }
        return internalApi.newDigest(messageDigest.digest());
    }
}

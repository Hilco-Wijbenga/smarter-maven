package org.cavebeetle.maven.impl;

import static org.cavebeetle.maven.CryptographicHashAlgorithm.MD5;
import static org.cavebeetle.maven.Digest.MISSING_FILE_DIGEST;
import java.io.File;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.cavebeetle.io.InputStream;
import org.cavebeetle.io.IoApi;
import org.cavebeetle.maven.Digest;
import org.cavebeetle.maven.FileHashGenerator;
import org.cavebeetle.maven.InternalApi;

/**
 * The implementation of {@code FileHashGenerator}.
 */
@Singleton
public final class DefaultFileHashGenerator
        implements
            FileHashGenerator
{
    private final IoApi ioApi;
    private final InternalApi internalApi;

    /**
     * Creates a new {@code DefaultFileHashGenerator}.
     *
     * @param ioApi
     *            the {@code IoApi} instance.
     * @param internalApi
     *            the {@code InternalApi} instance.
     */
    @Inject
    public DefaultFileHashGenerator(
            final IoApi ioApi,
            final InternalApi internalApi)
    {
        this.ioApi = ioApi;
        this.internalApi = internalApi;
    }

    @Override
    public Digest generate(
            final File file)
    {
        if (file.exists())
        {
            final InputStream inputStream = ioApi.newInputStream(file);
            try
            {
                return generate(inputStream);
            }
            finally
            {
                inputStream.close();
            }
        }
        else
        {
            return MISSING_FILE_DIGEST;
        }
    }

    @Override
    public Digest generate(
            final InputStream inputStream)
    {
        final Digest digest = internalApi.newCryptographicHash(MD5).generateDigest(inputStream);
        return digest;
    }
}

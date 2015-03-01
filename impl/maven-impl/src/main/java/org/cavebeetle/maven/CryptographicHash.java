package org.cavebeetle.maven;

import org.cavebeetle.io.InputStream;

/**
 * A {@code CryptographicHash} represents a cryptographic hash implementation.
 */
public interface CryptographicHash
{
    /**
     * A factory for {@code CryptographicHash} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code CryptographicHash}.
         *
         * @param cryptographicHashAlgorithm
         *            the cryptographic hash algorithm to use.
         * @return a new {@code CryptographicHash}.
         */
        CryptographicHash newCryptographicHash(
                CryptographicHashAlgorithm cryptographicHashAlgorithm);
    }

    /**
     * Generates a digest for the given {@code InputStream}.
     *
     * @param inputStream
     *            the {@code InputStream} to hash.
     * @return a digest for the given {@code InputStream}.
     */
    Digest generateDigest(
            InputStream inputStream);

    /**
     * Generates a digest for the given lines. Each line gets an OS dependent end-of-line appended prior to processing.
     *
     * @param lines
     *            the lines to hash.
     * @return a digest for the given lines.
     */
    Digest generateDigest(
            Iterable<String> lines);
}

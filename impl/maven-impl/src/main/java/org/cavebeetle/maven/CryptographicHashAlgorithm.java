package org.cavebeetle.maven;

import static java.security.MessageDigest.getInstance;
import java.security.MessageDigest;

/**
 * The list of available cryptographic hash algorithms.
 */
public enum CryptographicHashAlgorithm
{
    /** MD2 */
    MD2(
            "MD2"),
    /** MD5 */
    MD5(
            "MD5"),
    /** SHA1 */
    SHA1(
            "SHA-1"),
    /** SHA-256 */
    SHA256(
            "SHA-256"),
    /** SHA-384 */
    SHA384(
            "SHA-384"),
    /** SHA-512 */
    SHA512(
            "SHA-512");
    /**
     * Creates a new {@code MessageDigest}.
     *
     * @param algorithmName
     *            the algorithm to be used by the requested {@code MessageDigest}.
     * @return a new {@code MessageDigest}.
     */
    public static final MessageDigest newMessageDigest(
            final String algorithmName)
    {
        try
        {
            return getInstance(algorithmName);
        }
        catch (final Exception e)
        {
            throw new IllegalStateException(e);
        }
    }

    private final String algorithmName;

    private CryptographicHashAlgorithm(
            final String algorithmName)
    {
        this.algorithmName = algorithmName;
    }

    /**
     * Creates a new {@code MessageDigest}.
     *
     * @return a new {@code MessageDigest}.
     */
    public MessageDigest newMessageDigest()
    {
        return newMessageDigest(algorithmName);
    }

    @Override
    public String toString()
    {
        return algorithmName;
    }
}

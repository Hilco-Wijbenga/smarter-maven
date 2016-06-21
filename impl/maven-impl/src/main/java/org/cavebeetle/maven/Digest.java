package org.cavebeetle.maven;

/**
 * A {@code Digest} represents a hash.
 */
public interface Digest
{
    /**
     * A factory of {@code Digest} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code Digest}.
         *
         * @param bytes
         *            the bytes that represent the digest.
         * @return a new {@code Digest}.
         */
        Digest newDigest(byte[] bytes);
    }

    /**
     * Represents the digest of a missing (presumably deleted) file.
     */
    Digest MISSING_FILE_DIGEST = new Digest()
    {
        // Empty.
    };
}

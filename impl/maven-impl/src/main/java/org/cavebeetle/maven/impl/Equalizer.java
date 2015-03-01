package org.cavebeetle.maven.impl;

/**
 * The {@code Equalizer} facilitates type safe comparisons.
 */
public final class Equalizer
{
    /**
     * Returns whether the left and right instances are equal.
     *
     * @param <T>
     *            the type parameter.
     * @param left
     *            the left instance (may be {@code null}).
     * @param right
     *            the right instance (may be {@code null}).
     * @return {@code true} if and only if the left instance is equal to the right instance.
     */
    public static final <T> boolean isEqual(
            final T left,
            final T right)
    {
        return left == right || left != null && left.equals(right);
    }
}

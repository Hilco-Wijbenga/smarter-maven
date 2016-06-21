package org.cavebeetle.maven.impl;

import javax.inject.Singleton;
import org.cavebeetle.maven.DirtyReason;

/**
 * The implementation of {@code DirtyReason}.
 */
public final class DefaultDirtyReason
        implements
            DirtyReason
{
    /**
     * The implementation of {@code DirtyReason.Builder}.
     */
    @Singleton
    public static final class DefaultBuilder
            implements
                Builder
    {
        @Override
        public DirtyReason newDirtyReason(final boolean dirty, final String reason)
        {
            return new DefaultDirtyReason(dirty, reason);
        }
    }

    private final boolean dirty;
    private final String reason;

    /**
     * Creates a new {@code DefaultDirtyReason}.
     *
     * @param dirty
     *            whether this {@code DirtyReason} indicates the project is considered dirty.
     * @param reason
     *            the reason why this {@code DirtyReason} indicates the project is considered dirty.
     */
    public DefaultDirtyReason(final boolean dirty, final String reason)
    {
        this.dirty = dirty;
        this.reason = reason;
    }

    @Override
    public boolean isDirty()
    {
        return dirty;
    }

    @Override
    public String getReason()
    {
        return reason;
    }
}

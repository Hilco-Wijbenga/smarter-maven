package org.cavebeetle.maven;

/**
 * The available reasons why a project is considered dirty.
 */
public interface DirtyReason
{
    /**
     * A factory of {@code Gav} instances.
     */
    public interface Builder
    {
        /**
         * Creates a new {@code DirtyReason}.
         *
         * @param dirty
         *            whether this {@code DirtyReason} indicates the project is considered dirty.
         * @param reason
         *            the reason why this {@code DirtyReason} indicates the project is considered dirty.
         * @return a new {@code DirtyReason}.
         */
        DirtyReason newDirtyReason(
                boolean dirty,
                String reason);
    }

    /** Published. */
    DirtyReason PUBLISHED = new DirtyReason()
    {
        @Override
        public boolean isDirty()
        {
            return false;
        }

        @Override
        public String getReason()
        {
            return "Published";
        }
    };
    /** No build directory. */
    DirtyReason NO_BUILD_DIRECTORY = new DirtyReason()
    {
        @Override
        public boolean isDirty()
        {
            return true;
        }

        @Override
        public String getReason()
        {
            return "No build directory";
        }
    };
    /** Not in the local repo. */
    DirtyReason NOT_IN_LOCAL_REPO = new DirtyReason()
    {
        @Override
        public boolean isDirty()
        {
            return true;
        }

        @Override
        public String getReason()
        {
            return "Not in the local repo";
        }
    };
    /** No hash file. */
    DirtyReason NO_HASH_FILE = new DirtyReason()
    {
        @Override
        public boolean isDirty()
        {
            return true;
        }

        @Override
        public String getReason()
        {
            return "No source file digest available";
        }
    };
    /** Changes detected. */
    DirtyReason CHANGES_DETECTED = new DirtyReason()
    {
        @Override
        public boolean isDirty()
        {
            return true;
        }

        @Override
        public String getReason()
        {
            return "Changes detected";
        }
    };
    /** Not dirty. */
    DirtyReason NOT_DIRTY = new DirtyReason()
    {
        @Override
        public boolean isDirty()
        {
            return false;
        }

        @Override
        public String getReason()
        {
            return "";
        }
    };

    /**
     * Returns whether this {@code DirtyReason} indicates the project is considered dirty.
     *
     * @return whether this {@code DirtyReason} indicates the project is considered dirty.
     */
    boolean isDirty();

    /**
     * Gets the reason why this {@code DirtyReason} indicates the project is considered dirty.
     *
     * @return the reason why this {@code DirtyReason} indicates the project is considered dirty.
     */
    String getReason();
}

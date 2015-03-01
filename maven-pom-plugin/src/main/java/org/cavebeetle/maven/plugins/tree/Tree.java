package org.cavebeetle.maven.plugins.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface Tree
{
    boolean isLeaf();

    boolean isBranch();

    Leaf asLeaf();

    Branch asBranch();

    public static enum LeafType
    {
        START_PARENT,
        END_PARENT,
        START_DEPENDENCY,
        END_DEPENDENCY,
        START_ARTIFACT_ID,
        END_ARTIFACT_ID,
        START_GROUP_ID,
        END_GROUP_ID,
        START_VERSION,
        END_VERSION,
        START,
        TEXT,
        END,
        //
        ;
    }

    public static final class Leaf
            implements
                Tree
    {
        private final LeafType leafType;
        private final String text;

        public Leaf(
                final LeafType leafType,
                final String text)
        {
            this.leafType = leafType;
            this.text = text;
        }

        public LeafType getLeafType()
        {
            return leafType;
        }

        public boolean isText()
        {
            return leafType == LeafType.TEXT;
        }

        @Override
        public boolean isLeaf()
        {
            return true;
        }

        @Override
        public boolean isBranch()
        {
            return false;
        }

        @Override
        public Leaf asLeaf()
        {
            return this;
        }

        @Override
        public Branch asBranch()
        {
            throw new IllegalStateException("This is not a Tree.Branch.");
        }

        @Override
        public String toString()
        {
            return "{" + leafType + "[" + text + "]}";
        }
    }

    public static final class Branch
            implements
                Tree,
                Iterable<Tree>
    {
        private final List<Tree> leafsAndBranches = new ArrayList<Tree>();

        public void addTree(
                final Tree leafOrBranch)
        {
            leafsAndBranches.add(leafOrBranch);
        }

        @Override
        public boolean isLeaf()
        {
            return false;
        }

        @Override
        public boolean isBranch()
        {
            return true;
        }

        @Override
        public Leaf asLeaf()
        {
            throw new IllegalStateException("This is not a Tree.Leaf.");
        }

        @Override
        public Branch asBranch()
        {
            return this;
        }

        @Override
        public Iterator<Tree> iterator()
        {
            return leafsAndBranches.iterator();
        }

        @Override
        public String toString()
        {
            final StringBuilder text = new StringBuilder();
            for (final Tree leafOrBranch : this)
            {
                text.append(leafOrBranch.toString());
            }
            return text.toString();
        }
    }
}

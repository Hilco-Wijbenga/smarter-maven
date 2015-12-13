package org.cavebeetle.maven;

public interface TypeClassList
{
    public static enum Type
    {
        NIL,
        LIST;
    }

    public static interface List
    {
        Type type();
    }

    public static enum Nil implements
            List
    {
        INSTANCE
        //
        ;
        @Override
        public Type type()
        {
            return Type.NIL;
        }
    }

    public static final class List_<E>
            implements
                List
    {
        private final E head;
        private final List tail;

        public List_(final E head, final List tail)
        {
            this.tail = tail;
            this.head = head;
        }

        @Override
        public Type type()
        {
            return Type.LIST;
        }

        public E head()
        {
            return head;
        }

        public List tail()
        {
            return tail;
        }
    }

    public static interface Functions
    {
        public static interface Length
        {
            int length(List list);
        }

        public static interface Head
        {
            <E> E head(List list);
        }

        public static interface Tail
        {
            List tail(List list);
        }

        public static interface Get
        {
            <E> E get(List list, int index);
        }

        Length LENGTH = new Length()
        {
            @Override
            public int length(final List list)
            {
                switch (list.type())
                {
                    case NIL:
                    {
                        return 0;
                    }
                    default:
                    {
                        final List_ list_ = (List_) list;
                        return 1 + length(list_.tail());
                    }
                }
            }
        };
        Head HEAD = new Head()
        {
            @Override
            public <E> E head(final List list)
            {
                switch (list.type())
                {
                    case NIL:
                        throw new IllegalStateException("Empty list.");
                    default:
                    {
                        final List_ list_ = (List_) list;
                        return (E) list_.head();
                    }
                }
            }
        };
        Tail TAIL = new Tail()
        {
            @Override
            public List tail(final List list)
            {
                switch (list.type())
                {
                    case NIL:
                    {
                        return list;
                    }
                    default:
                    {
                        final List_ list_ = (List_) list;
                        return list_.tail();
                    }
                }
            }
        };
        Get GET = new Get()
        {
            @Override
            public <E> E get(final List list, final int index)
            {
                if (index < 0)
                {
                    throw new IllegalStateException("Invalid list index: " + index + ".");
                }
                switch (list.type())
                {
                    case NIL:
                    {
                        throw new IllegalStateException("Empty list.");
                    }
                    default:
                    {
                        final List_ list_ = (List_) list;
                        return index == 0 ? (E) list_.head() : (E) get(list_.tail(), index - 1);
                    }
                }
            }
        };
    }
}

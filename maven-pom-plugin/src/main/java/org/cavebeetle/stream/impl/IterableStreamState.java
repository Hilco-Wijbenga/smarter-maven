package org.cavebeetle.stream.impl;

import java.util.Iterator;

public final class IterableStreamState<T>
        implements
            StreamState<T>
{
    private final Iterator<T> iterator;
    private final boolean empty;
    private final T head;

    public IterableStreamState(
            final Iterable<T> iterable)
    {
        this(iterable.iterator());
    }

    private IterableStreamState(
            final Iterator<T> iterator)
    {
        this.iterator = iterator;
        empty = !iterator.hasNext();
        head = empty ? null : iterator.next();
    }

    @Override
    public T head()
    {
        return head;
    }

    @Override
    public StreamState<T> tail()
    {
        return new IterableStreamState<T>(iterator);
    }

    @Override
    public boolean isEmpty()
    {
        return !empty;
    }
}

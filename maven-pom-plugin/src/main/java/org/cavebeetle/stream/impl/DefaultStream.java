package org.cavebeetle.stream.impl;

import java.util.Iterator;
import org.cavebeetle.stream.EmptyStreamException;
import org.cavebeetle.stream.Stream;

public final class DefaultStream<T>
        implements
            Stream<T>
{
    private final StreamState<T> state;
    private volatile Stream<T> tail;

    public DefaultStream(
            final StreamState<T> state)
    {
        this.state = state;
    }

    @Override
    public T head()
    {
        if (isEmpty())
        {
            throw new EmptyStreamException();
        }
        return state.head();
    }

    @Override
    public Stream<T> tail()
    {
        if (isEmpty())
        {
            throw new EmptyStreamException();
        }
        if (tail == null)
        {
            synchronized (this)
            {
                if (tail == null)
                {
                    tail = new DefaultStream<T>(state.tail());
                }
            }
        }
        return tail;
    }

    @Override
    public boolean isEmpty()
    {
        return state.isEmpty();
    }

    @Override
    public Stream<T> concatenate(
            final Stream<T> stream)
    {
        return new DefaultStream<T>(new ConcatenatedStreamState<T>(this, stream));
    }

    @Override
    public Stream<T> insert(
            final Stream<T> stream)
    {
        return new DefaultStream<T>(new ConcatenatedStreamState<T>(stream, this));
    }

    @Override
    public Iterator<T> iterator()
    {
        return new StreamIterator<T>(this);
    }

    public StreamState<T> getStreamState()
    {
        return state;
    }
}

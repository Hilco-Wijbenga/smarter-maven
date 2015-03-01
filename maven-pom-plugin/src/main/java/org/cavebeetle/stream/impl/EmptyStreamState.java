package org.cavebeetle.stream.impl;

import org.cavebeetle.stream.EmptyStreamException;

public final class EmptyStreamState<T>
        implements
            StreamState<T>
{
    @Override
    public T head()
    {
        throw new EmptyStreamException();
    }

    @Override
    public StreamState<T> tail()
    {
        throw new EmptyStreamException();
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }
}

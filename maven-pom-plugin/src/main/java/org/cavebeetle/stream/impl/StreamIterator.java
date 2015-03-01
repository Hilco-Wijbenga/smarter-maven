package org.cavebeetle.stream.impl;

import java.util.Iterator;
import org.cavebeetle.stream.Stream;

public final class StreamIterator<T>
        implements
            Iterator<T>
{
    private Stream<T> stream;

    public StreamIterator(
            final Stream<T> stream)
    {
        this.stream = stream;
    }

    @Override
    public boolean hasNext()
    {
        return !stream.isEmpty();
    }

    @Override
    public T next()
    {
        try
        {
            return stream.head();
        }
        finally
        {
            stream = stream.tail();
        }
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException("A StreamIterator does not support remove.");
    }
}

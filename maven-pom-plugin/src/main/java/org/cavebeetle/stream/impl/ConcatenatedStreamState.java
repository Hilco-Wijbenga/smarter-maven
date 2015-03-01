package org.cavebeetle.stream.impl;

import org.cavebeetle.stream.Stream;

public final class ConcatenatedStreamState<T>
        implements
            StreamState<T>
{
    private final Stream<T> firstStream;
    private final Stream<T> secondStream;

    public ConcatenatedStreamState(
            final Stream<T> firstStream,
            final Stream<T> secondStream)
    {
        this.firstStream = firstStream;
        this.secondStream = secondStream;
    }

    @Override
    public T head()
    {
        return firstStream.head();
    }

    @Override
    public StreamState<T> tail()
    {
        if (!firstStream.isEmpty())
        {
            return new ConcatenatedStreamState<T>(firstStream.tail(), secondStream);
        }
        else
        {
            final DefaultStream<T> secondStreamAsDefaultStream = (DefaultStream<T>) secondStream;
            return secondStreamAsDefaultStream.getStreamState();
        }
    }

    @Override
    public boolean isEmpty()
    {
        return firstStream.isEmpty() && secondStream.isEmpty();
    }
}

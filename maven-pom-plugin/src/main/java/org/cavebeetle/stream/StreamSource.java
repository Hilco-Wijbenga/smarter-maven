package org.cavebeetle.stream;

public interface StreamSource<T>
{
    boolean isEmpty();

    T getNext();
}

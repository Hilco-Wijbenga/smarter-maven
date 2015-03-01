package org.cavebeetle.stream.impl;

public interface StreamState<T>
{
    T head();

    StreamState<T> tail();

    boolean isEmpty();
}

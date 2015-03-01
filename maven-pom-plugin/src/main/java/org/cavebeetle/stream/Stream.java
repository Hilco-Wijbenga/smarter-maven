package org.cavebeetle.stream;

public interface Stream<T>
        extends
            Iterable<T>
{
    boolean isEmpty();

    T head();

    Stream<T> tail();

    Stream<T> concatenate(
            Stream<T> stream);

    Stream<T> insert(
            Stream<T> stream);
}

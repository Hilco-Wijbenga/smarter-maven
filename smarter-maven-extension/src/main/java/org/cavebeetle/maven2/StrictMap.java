package org.cavebeetle.maven2;

import java.util.Iterator;
import java.util.Map;
import com.google.common.collect.Maps;

public interface StrictMap<KEY, VALUE>
        extends
            Iterable<KEY>
{
    VALUE get(KEY key);

    int size();

    public interface Mutable<KEY, VALUE>
            extends
                StrictMap<KEY, VALUE>
    {
        void put(KEY key, VALUE value);
    }

    public static final class Builder
    {
        public static final <K, V> StrictMap.Mutable<K, V> make()
        {
            return new DefaultStrictMap<K, V>();
        }
    }

    public static final class DefaultStrictMap<KEY, VALUE>
            implements
                StrictMap.Mutable<KEY, VALUE>
    {
        private final Map<KEY, VALUE> map;

        public DefaultStrictMap()
        {
            map = Maps.newConcurrentMap();
        }

        @Override
        public VALUE get(final KEY key)
        {
            return map.get(key);
        }

        @Override
        public void put(final KEY key, final VALUE value)
        {
            map.put(key, value);
        }

        @Override
        public int size()
        {
            return map.size();
        }

        @Override
        public Iterator<KEY> iterator()
        {
            return map.keySet().iterator();
        }
    }
}

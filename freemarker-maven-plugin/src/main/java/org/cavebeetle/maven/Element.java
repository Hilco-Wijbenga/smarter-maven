package org.cavebeetle.maven;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.ImmutableList;

public interface Element
{
    public static enum Type
    {
        TAG,
        GROUP,
        KEY_VALUE;
    }

    public static final class Tag
            implements
                Element
    {
        private final Key key;

        public Tag(final Key key)
        {
            checkNotNull(key, "Missing 'key'.");
            this.key = key;
        }

        @Override
        public int hashCode()
        {
            final int prime = 353;
            int result = 1;
            result = prime * result + key.hashCode();
            return result;
        }

        @Override
        public boolean equals(final Object object)
        {
            if (this == object)
            {
                return true;
            }
            if (object == null || getClass() != object.getClass())
            {
                return false;
            }
            final Tag other = (Tag) object;
            return key.equals(other.key);
        }

        @Override
        public String toString()
        {
            return toStringHelper(getClass())
                    .add("key", key)
                    .toString();
        }

        @Override
        public Type type()
        {
            return Type.TAG;
        }

        @Override
        public Key key()
        {
            return key;
        }

        @Override
        public void toText(final Path path, final List<String> lines)
        {
            System.out.println(type() + "#toText");
            lines.add(path.toText() + "/" + key.value());
        }
    }

    public static final class Group
            implements
                Element,
                Iterable<Element>
    {
        private final Key key;
        private final List<Element> elements;

        public Group(final Key key, final List<Element> elements)
        {
            checkNotNull(key, "Missing 'key'.");
            checkNotNull(elements, "Missing 'elements'.");
            this.key = key;
            this.elements = ImmutableList.copyOf(elements);
        }

        @Override
        public int hashCode()
        {
            final int prime = 359;
            int result = 1;
            result = prime * result + elements.hashCode();
            result = prime * result + key.hashCode();
            return result;
        }

        @Override
        public boolean equals(final Object object)
        {
            if (this == object)
            {
                return true;
            }
            if (object == null || getClass() != object.getClass())
            {
                return false;
            }
            final Group other = (Group) object;
            return key.equals(other.key) && elements.equals(other.elements);
        }

        @Override
        public String toString()
        {
            return toStringHelper(getClass())
                    .add("key", key)
                    .add("elements", elements)
                    .toString();
        }

        @Override
        public Type type()
        {
            return Type.GROUP;
        }

        @Override
        public Key key()
        {
            return key;
        }

        @Override
        public Iterator<Element> iterator()
        {
            return elements.iterator();
        }

        @Override
        public void toText(final Path path, final List<String> lines)
        {
            System.out.println(type() + "#toText");
            final Path path_ = new Path(path, key);
            for (final Element element : elements)
            {
                element.toText(path_, lines);
            }
        }
    }

    public static final class KeyValue
            implements
                Element
    {
        private final Key key;
        private final String value;

        public KeyValue(final Key key, final String value)
        {
            checkNotNull(key, "Missing 'key'.");
            checkArgument(value != null && !value.isEmpty(), "Missing 'value'.");
            this.key = key;
            this.value = value;
        }

        @Override
        public int hashCode()
        {
            final int prime = 367;
            int result = 1;
            result = prime * result + key.hashCode();
            result = prime * result + value.hashCode();
            return result;
        }

        @Override
        public boolean equals(final Object object)
        {
            if (this == object)
            {
                return true;
            }
            if (object == null || getClass() != object.getClass())
            {
                return false;
            }
            final KeyValue other = (KeyValue) object;
            return key.equals(other.key) && value.equals(other.value);
        }

        @Override
        public String toString()
        {
            return toStringHelper(getClass())
                    .add("key", key)
                    .add("value", value)
                    .toString();
        }

        @Override
        public Type type()
        {
            return Type.KEY_VALUE;
        }

        @Override
        public Key key()
        {
            return key;
        }

        @Override
        public void toText(final Path path, final List<String> lines)
        {
            System.out.println(type() + "#toText");
            lines.add(path.toText() + "/" + key.value() + " = '" + value + "'");
        }

        public String value()
        {
            return value;
        }
    }

    Type type();

    Key key();

    void toText(Path path, List<String> lines);
}

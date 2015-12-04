package org.cavebeetle.maven;

import static com.google.common.collect.Lists.newArrayList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.ImmutableList;

public final class Model_
{
    public static interface Element
    {
        public static final class Group
                implements
                    Element,
                    Iterable<Element>
        {
            public static class Builder
            {
                private final String tag;
                private final List<Element> elements;

                public Builder(
                        final String tag)
                {
                    this.tag = tag;
                    elements = new ArrayList<Element>();
                }

                public Builder add(
                        final Element element)
                {
                    elements.add(element);
                    return this;
                }

                public Element.Group.Builder newGroup(
                        final String tag)
                {
                    return new Element.Group.Builder(tag);
                }

                public Element.KeyValue.Builder newKeyValue(
                        final String tag)
                {
                    return new Element.KeyValue.Builder(tag);
                }

                public Element.Group build()
                {
                    return new Element.Group(this);
                }
            }

            public static final Element.Group.Builder newGroup(
                    final String tag)
            {
                return new Element.Group.Builder(tag);
            }

            private final String tag;
            private final List<Element> elements;

            public Group(
                    final Element.Group.Builder builder)
            {
                tag = builder.tag;
                elements = ImmutableList.copyOf(builder.elements);
            }

            @Override
            public String tag()
            {
                return tag;
            }

            public List<Element> elements()
            {
                return elements;
            }

            @Override
            public Iterator<Element> iterator()
            {
                return elements.iterator();
            }

            @Override
            public String toString()
            {
                final StringBuilder sb = new StringBuilder();
                for (final Element element : elements)
                {
                    sb.append('/').append(tag).append('/').append(element);
                }
                return sb.toString();
            }

            @Override
            public void toText(
                    final List<String> lines,
                    final String path)
            {
                for (final Element element : elements)
                {
                    element.toText(lines, path + "/" + tag);
                }
            }
        }

        public static final class KeyValue
                implements
                    Element
        {
            public static class Builder
            {
                private final String key;
                private String value;

                public Builder(
                        final String key)
                {
                    this.key = key;
                }

                public Builder setValue(
                        final String value)
                {
                    this.value = value;
                    return this;
                }

                public KeyValue build()
                {
                    return new KeyValue(this);
                }
            }

            public static final Element.KeyValue.Builder newKeyValue(
                    final String tag)
            {
                return new Element.KeyValue.Builder(tag);
            }

            private final String key;
            private final String value;

            public KeyValue(
                    final Builder builder)
            {
                key = builder.key;
                value = builder.value;
            }

            @Override
            public String tag()
            {
                return key;
            }

            public String value()
            {
                return value;
            }

            @Override
            public void toText(
                    final List<String> lines,
                    final String path)
            {
                final StringBuilder sb = new StringBuilder();
                sb.append(path).append('/').append(key).append(" = '").append(value).append('\'');
                lines.add(sb.toString());
            }
        }

        String tag();

        void toText(
                List<String> lines,
                String path);
    }

    public static final Element.Group.Builder newModel(
            final String root)
    {
        return new Element.Group.Builder(root);
    }

    private final Element.Group root;

    public Model_(
            final Element.Group.Builder builder)
    {
        root = builder.build();
    }

    @Override
    public String toString()
    {
        final List<String> lines = newArrayList();
        for (final Element element : root)
        {
            element.toText(lines, "/" + root.tag());
        }
        final StringBuilder sb = new StringBuilder();
        for (final String line : lines)
        {
            sb.append(line).append('\n');
        }
        return sb.toString();
    }
}

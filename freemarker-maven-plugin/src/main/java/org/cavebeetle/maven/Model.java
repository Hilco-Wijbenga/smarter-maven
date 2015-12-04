package org.cavebeetle.maven;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.ImmutableList;

public final class Model
        implements
            Iterable<Element>
{
    private final List<Element> elements;

    public Model(final List<Element> elements)
    {
        checkNotNull(elements, "Missing 'elements'.");
        this.elements = ImmutableList.copyOf(elements);
        for (final Element element : elements)
        {
            System.out.println(element);
        }
    }

    @Override
    public int hashCode()
    {
        final int prime = 373;
        int result = 1;
        result = prime * result + elements.hashCode();
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
        final Model other = (Model) object;
        return elements.equals(other.elements);
    }

    @Override
    public String toString()
    {
        return toStringHelper(getClass())
                .add("elements", elements)
                .toString();
    }

    @Override
    public Iterator<Element> iterator()
    {
        return elements.iterator();
    }

    public void toText(final List<String> lines)
    {
        for (final Element element : elements)
        {
            element.toText(Path.ROOT, lines);
        }
    }
}

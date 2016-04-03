package org.cavebeetle.blueprint;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.ArrayList;
import java.util.List;

public final class Composite
        implements
            Component
{
    private final String name;
    private final List<Component> components;

    public Composite(final String name)
    {
        checkNotNull(name, "Missing 'name'.");
        this.name = name;
        components = new ArrayList<Component>();
    }

    public String name()
    {
        return name;
    }

    public Iterable<Component> components()
    {
        return components;
    }

    public void createLeaf(final Leaf leaf)
    {
        components.add(leaf);
    }

    public void addComposite(final Composite composite)
    {
        components.add(composite);
    }

    public Composite composite(final String... path)
    {
        return getComposite(0, path);
    }

    public Composite getComposite(final int index, final String[] path)
    {
        if (path.length == 0 || index >= path.length || !name.equals(path[index]))
        {
            return null;
        }
        if (index + 1 == path.length)
        {
            return this;
        }
        for (final Component component : components())
        {
            if (component instanceof Composite)
            {
                final Composite composite = (Composite) component;
                final Composite result = composite.getComposite(index + 1, path);
                if (result != null)
                {
                    return result;
                }
            }
        }
        return null;
    }
}

package org.cavebeetle.blueprint;

import static com.google.common.base.Preconditions.checkNotNull;

public final class Main
{
    public static void main(final String[] args)
    {
        final Composite root = new Composite("project");
        root.createLeaf(new Leaf("modelVersion", "4"));
        root.addComposite(new Composite("properties"));
        root.addComposite(new Composite("dependencies"));
        checkNotNull(root.composite("project", "properties"));
        root.composite("project", "dependencies").addComposite(new Composite("dependency"));
        root.composite("project", "dependencies", "dependency").createLeaf(new Leaf("groupId", "THE GROUP_ID"));
        dump(Indentation.NO_INDENTATION, root);
        System.out.println("Okay");
    }

    public static final void dump(final Indentation indentation, final Component component)
    {
        if (component instanceof Composite)
        {
            dumpComposite(indentation, (Composite) component);
        }
        else
        {
            dumpLeaf(indentation, (Leaf) component);
        }
    }

    public static final void dumpComposite(final Indentation indentation, final Composite composite)
    {
        System.out.println(String.format("%s<%s>", indentation.text(), composite.name()));
        for (final Component component : composite.components())
        {
            dump(indentation.indent(), component);
        }
        System.out.println(String.format("%s</%s>", indentation.text(), composite.name()));
    }

    public static final void dumpLeaf(final Indentation indentation, final Leaf leaf)
    {
        System.out.println(String.format("%s<%s>%s</%s>", indentation.text(), leaf.name(), leaf.value(), leaf.name()));
    }
}
/*

<plugin-definition gId aId>

</plugin-definition>

<blueprint id="...">
    <extends id="blueprint-id-1"/>
    :
    <extends id="blueprint-id-n"/>
    <property name="..." value="${blueprint.the.property.id}"/>
    <manage-dependency gId aId after="gId:aId">
        <version>${blueprint.version}</version>
        <exclude-dependency gId aId/>
        <exclude-dependency gId aId/>
    </manage-dependency>
    <dependency gId aId after="gId:aId">
        <version>${blueprint.version}</version>
        <exclude-dependency gId aId/>
        <exclude-dependency gId aId/>
    </dependency>
    <manage-plugin gId aId phase>
        <version>${blueprint.version}</version>
    </manage-plugin>
    <plugin gId aId phase>
        <version>${blueprint.version}</version>
    </plugin>
</blueprint>

 */

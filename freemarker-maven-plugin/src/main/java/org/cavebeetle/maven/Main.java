package org.cavebeetle.maven;

import static com.google.common.collect.Lists.newArrayList;
import java.util.List;
import com.google.common.collect.Lists;

public final class Main
{
    public static void main(final String[] args)
    {
        //final Element tag = new Element.Tag(new Key("tag"));
        //final List<Element> projectElements = Lists.<Element> newArrayList(tag);
        //final Element project = new Element.Group(new Key("project"), projectElements);
        //final Model model = new Model(Lists.newArrayList(new Element.Group(new Key("project"), projectElements)));
        final Model model = new Model(Lists.<Element>newArrayList(
                                new Element.Group(new Key("parent"), Lists.<Element> newArrayList(
                                        new Element.KeyValue(new Key("groupId"), "GROUP-ID"),
                                        new Element.KeyValue(new Key("artifactId"), "ARTIFACT-ID"),
                                        new Element.KeyValue(new Key("version"), "1.2.3")))));
        final List<String> lines = newArrayList();
        model.toText(lines);
        for (final String line : lines)
        {
            System.out.println(line);
        }
    }
}
/*

data List a  = Nil
             | List a : Nil

data Key     = Key String

data Path    = Path (List Key)

data Element = Tag      Key
             | Group    Key (List Element)
             | KeyValue Key String

data Model   = Model (List Element)

*/

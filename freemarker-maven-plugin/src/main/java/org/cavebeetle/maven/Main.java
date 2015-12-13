package org.cavebeetle.maven;

import static org.cavebeetle.maven.Key.newKey;

public final class Main
{
    public static void main(final String[] args)
    {
        //        final Model model = new Model(ImmutableList.<Element> of(
        //                new Element.Tag(new Key("tag")),
        //                new Element.Group(new Key("parent"), ImmutableList.<Element> of(
        //                        new Element.KeyValue(new Key("groupId"), "GROUP-ID"),
        //                        new Element.KeyValue(new Key("artifactId"), "ARTIFACT-ID"),
        //                        new Element.KeyValue(new Key("version"), "1.2.3")))));
        //        final List<String> lines = newArrayList();
        //        model.toText(lines);
        //        for (final String line : lines)
        //        {
        //            System.out.println(line);
        //        }
        System.out.println(Path.ROOT);
        System.out.println(Path.ROOT.toText());
        final Path project = Path.ROOT.extend(newKey("project"));
        System.out.println(project);
        System.out.println(project.toText());
        final Path parent = project.extend(newKey("parent"));
        System.out.println(parent);
        System.out.println(parent.toText());
        final Path groupId = parent.extend(newKey("groupId"));
        System.out.println(groupId);
        System.out.println(groupId.toText());
    }
}

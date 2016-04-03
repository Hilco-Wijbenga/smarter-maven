package org.cavebeetle.maven;

import static org.cavebeetle.maven.Key.newKey;
import java.util.HashMap;
import java.util.Map;

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
        // model.getValue(path); model.setValue(path, value);
        // model.getModel(path); model.addModel(path, key);
        final Map<Object, Object> model = new HashMap<Object, Object>();
        model.put("project", null);
        /*

         /project/modelVersion = 4.0.0
         /project/dependencies/dependency[0]/groupId = xyz
         /project/dependencies/dependency[0]/artifactId = xyz
         /project/dependencies/dependency[1]/groupId = xyz
         /project/dependencies/dependency[1]/artifactId = xyz

         */
        final Path project = Path.ROOT.extend(newKey("project"));
        final Path dependencies = project.extend(newKey("dependencies"));
        final Path firstDependency = dependencies.extend(newKey("dependency"));
        final Path firstDependencyGroupId = firstDependency.extend(newKey("groupId"));
        final Path secondDependency = dependencies.extend(newKey("dependency"));
        final Path secondDependencyGroupId = secondDependency.extend(newKey("groupId"));
    }
}

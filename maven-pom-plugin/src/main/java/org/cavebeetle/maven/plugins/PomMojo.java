package org.cavebeetle.maven.plugins;

import static javax.xml.stream.XMLStreamConstants.CHARACTERS;
import static javax.xml.stream.XMLStreamConstants.END_DOCUMENT;
import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.SPACE;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Deque;
import java.util.LinkedList;
import javax.xml.stream.XMLInputFactory;
import org.cavebeetle.maven.plugins.tree.Tree;
import org.codehaus.stax2.LocationInfo;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

public final class PomMojo
{
    public static final void main(
            final String[] argv)
            throws Exception
    {
        final File pomFile = new File("pom.xml");
        final FileInputStream pomFileInputStream = new FileInputStream(pomFile);
        final byte[] buffer = new byte[(int) pomFile.length()];
        pomFileInputStream.read(buffer);
        pomFileInputStream.close();
        final String pomContent = new String(buffer);
        final XMLInputFactory2 factory = (XMLInputFactory2) XMLInputFactory.newInstance();
        factory.configureForRoundTripping();
        final XMLStreamReader2 xmlStreamReader = (XMLStreamReader2) factory.createXMLStreamReader(new StringReader(pomContent));
        Tree.Branch tree = new Tree.Branch();
        final LocationInfo firstLocation = xmlStreamReader.getLocationInfo();
        final Tree.Leaf leaf = new Tree.Leaf(Tree.LeafType.TEXT, pomContent.substring((int) firstLocation.getStartingCharOffset(), (int) firstLocation.getEndingCharOffset()));
        tree.addTree(leaf);
        final Deque<Tree.Branch> stack = new LinkedList<Tree.Branch>();
        while (xmlStreamReader.hasNext())
        {
            final int eventType = xmlStreamReader.next();
            final LocationInfo currentLocation = xmlStreamReader.getLocationInfo();
            final String text = pomContent.substring((int) currentLocation.getStartingCharOffset(), (int) currentLocation.getEndingCharOffset());
            switch (eventType)
            {
                case START_ELEMENT:
                {
                    final Tree.LeafType leafType;
                    if ("parent".equals(xmlStreamReader.getLocalName()))
                    {
                        leafType = Tree.LeafType.START_PARENT;
                    }
                    else if ("dependency".equals(xmlStreamReader.getLocalName()))
                    {
                        leafType = Tree.LeafType.START_DEPENDENCY;
                    }
                    else if ("artifactId".equals(xmlStreamReader.getLocalName()))
                    {
                        leafType = Tree.LeafType.START_ARTIFACT_ID;
                    }
                    else if ("groupId".equals(xmlStreamReader.getLocalName()))
                    {
                        leafType = Tree.LeafType.START_GROUP_ID;
                    }
                    else if ("version".equals(xmlStreamReader.getLocalName()))
                    {
                        leafType = Tree.LeafType.START_VERSION;
                    }
                    else
                    {
                        leafType = Tree.LeafType.START;
                    }
                    tree.addTree(new Tree.Leaf(leafType, text));
                    final Tree.Branch subTree = new Tree.Branch();
                    tree.addTree(subTree);
                    stack.push(tree);
                    tree = subTree;
                    break;
                }
                case END_ELEMENT:
                {
                    final Tree.LeafType leafType;
                    if ("parent".equals(xmlStreamReader.getLocalName()))
                    {
                        leafType = Tree.LeafType.END_PARENT;
                    }
                    else if ("dependency".equals(xmlStreamReader.getLocalName()))
                    {
                        leafType = Tree.LeafType.END_DEPENDENCY;
                    }
                    else if ("artifactId".equals(xmlStreamReader.getLocalName()))
                    {
                        leafType = Tree.LeafType.END_ARTIFACT_ID;
                    }
                    else if ("groupId".equals(xmlStreamReader.getLocalName()))
                    {
                        leafType = Tree.LeafType.END_GROUP_ID;
                    }
                    else if ("version".equals(xmlStreamReader.getLocalName()))
                    {
                        leafType = Tree.LeafType.END_VERSION;
                    }
                    else
                    {
                        leafType = Tree.LeafType.END;
                    }
                    tree = stack.pop();
                    tree.addTree(new Tree.Leaf(leafType, text));
                    break;
                }
                case CHARACTERS:
                case SPACE:
                {
                    tree.addTree(new Tree.Leaf(Tree.LeafType.TEXT, pomContent.substring((int) currentLocation.getStartingCharOffset(), (int) currentLocation.getEndingCharOffset())));
                    break;
                }
                case END_DOCUMENT:
                {
                    tree.addTree(new Tree.Leaf(Tree.LeafType.TEXT, pomContent.substring((int) currentLocation.getStartingCharOffset(), (int) currentLocation.getEndingCharOffset())));
                    break;
                }
                default:
                {
                    tree.addTree(new Tree.Leaf(Tree.LeafType.TEXT, pomContent.substring((int) currentLocation.getStartingCharOffset(), (int) currentLocation.getEndingCharOffset())));
                }
            }
        }
        final Tree mergedTree = merge(tree);
        System.out.println(mergedTree);
    }

    public static final Tree merge(
            final Tree.Branch tree)
    {
        final Tree.Branch mergedTree = new Tree.Branch();
        Tree.Leaf previousLeaf = null;
        for (final Tree leafOrBranch : tree)
        {
            if (leafOrBranch.isBranch())
            {
                if (previousLeaf != null)
                {
                    mergedTree.addTree(previousLeaf);
                    previousLeaf = null;
                }
                mergedTree.addTree(merge(leafOrBranch.asBranch()));
            }
            else
            {
                if (previousLeaf != null)
                {
                    if (previousLeaf.isText() && leafOrBranch.asLeaf().getLeafType() == Tree.LeafType.START_ARTIFACT_ID)
                    {
                        mergedTree.addTree(new Tree.Leaf(Tree.LeafType.START_ARTIFACT_ID, previousLeaf.toString() + leafOrBranch.toString()));
                    }
                    else
                    {
                        mergedTree.addTree(previousLeaf);
                    }
                    previousLeaf = null;
                }
                else
                {
                    previousLeaf = leafOrBranch.isLeaf() ? leafOrBranch.asLeaf() : null;
                }
            }
        }
        return mergedTree;
    }
}

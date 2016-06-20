package org.cavebeetle.maven2;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public final class DirectoryTreeDigester
{
    public static final void main(final String[] args) throws Exception
    {
        System.out.println("Running...");
        new DirectoryTreeDigester(new File("/home/hilco/workspaces/smarter-maven"));
        System.out.println("Done.");
    }

    public DirectoryTreeDigester(final File rootDir) throws Exception
    {
        final Path path = FileSystems.getDefault().getPath(rootDir.getCanonicalPath());
        final List<Path> files = Lists.newArrayList();
        Files.walkFileTree(path, Sets.<FileVisitOption> newHashSet(), Integer.MAX_VALUE, new FileVisitor<Path>()
        {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException
            {
                if (dir.getFileName().toString().equals(".git"))
                {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                if (dir.getFileName().toString().equals(".metadata"))
                {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                if (dir.getFileName().toString().equals(".recommenders"))
                {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                if (dir.getFileName().toString().equals("target"))
                {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                System.out.println(String.format("%s", dir));
                files.add(dir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException
            {
                System.out.println(String.format("%s", file));
                files.add(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(final Path file, final IOException exc) throws IOException
            {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException
            {
                return FileVisitResult.CONTINUE;
            }
        });
    }
}

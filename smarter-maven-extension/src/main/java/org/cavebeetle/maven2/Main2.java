package org.cavebeetle.maven2;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;

public final class Main2
{
    public static void main(final String[] args) throws Exception
    {
        final String[] inclusions = new String[] {
            "pom.xml",
            "src",
            "target"
        };
        final File rootDir = new File("/home/hilco/workspaces/smarter-maven");
        final List<File> rootFiles = findAllFiles(rootDir, inclusions);
        final int N = 13;
        System.out.println("Running...");
        runWithCrc32(rootFiles, N);
        runWithAdler32(rootFiles, N);
        runWithMd5(rootFiles, N);
        runWithSha1(rootFiles, N);
        runWithSha256(rootFiles, N);
        //final String hex = BaseEncoding.base16().encode(digest);
        //System.out.println(hex);
    }

    private static void runWithCrc32(final List<File> rootFiles, final int N)
    {
        final long startInMillis = System.currentTimeMillis();
        for (int i = 0; i < N; i++)
        {
            for (final File file : rootFiles)
            {
                calculateDigestCrc32(file);
            }
        }
        printDelta("CRC32", N, startInMillis, System.currentTimeMillis());
    }

    private static void runWithAdler32(final List<File> rootFiles, final int N)
    {
        final long startInMillis = System.currentTimeMillis();
        for (int i = 0; i < N; i++)
        {
            for (final File file : rootFiles)
            {
                calculateDigestAdler32(file);
            }
        }
        printDelta("Adler32", N, startInMillis, System.currentTimeMillis());
    }

    private static void runWithMd5(final List<File> rootFiles, final int N)
    {
        final long startInMillis = System.currentTimeMillis();
        for (int i = 0; i < N; i++)
        {
            for (final File file : rootFiles)
            {
                calculateDigestMd5(file);
            }
        }
        printDelta("MD5", N, startInMillis, System.currentTimeMillis());
    }

    private static void runWithSha1(final List<File> rootFiles, final int N)
    {
        final long startInMillis = System.currentTimeMillis();
        for (int i = 0; i < N; i++)
        {
            for (final File file : rootFiles)
            {
                calculateDigestSha1(file);
            }
        }
        printDelta("SHA-1", N, startInMillis, System.currentTimeMillis());
    }

    private static void runWithSha256(final List<File> rootFiles, final int N)
    {
        final long startInMillis = System.currentTimeMillis();
        for (int i = 0; i < N; i++)
        {
            for (final File file : rootFiles)
            {
                calculateDigestSha256(file);
            }
        }
        printDelta("SHA-256", N, startInMillis, System.currentTimeMillis());
    }

    private static void printDelta(final String algorithm, final int N, final long startInMillis, final long finishInMillis)
    {
        final long deltaInMillis = finishInMillis + 1L - startInMillis;
        final double deltaInMillis_ = deltaInMillis / N;
        final int deltaInMinutes = (int) (deltaInMillis_ / 1000D / 60D);
        final int deltaInSeconds = (int) ((deltaInMillis_ - deltaInMinutes * 1000D * 60D) / 1000D);
        final int deltaInMillis__ = (int) (deltaInMillis_ - deltaInMinutes * 1000D * 60D - deltaInSeconds * 1000D);
        System.out.println(String.format("%7s   %02d:%02d.%03d", algorithm, deltaInMinutes, deltaInSeconds, deltaInMillis__));
    }

    public static byte[] calculateDigestCrc32(final File filename)
    {
        final int SIZE = 16 * 1024;
        try
        {
            final FileInputStream in = new FileInputStream(filename);
            try
            {
                final FileChannel channel = in.getChannel();
                final CRC32 crc = new CRC32();
                final int length = (int) channel.size();
                final MappedByteBuffer mb = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
                final byte[] buffer = new byte[SIZE];
                while (mb.hasRemaining())
                {
                    final int nGet = Math.min(mb.remaining(), SIZE);
                    mb.get(buffer, 0, nGet);
                    crc.update(buffer, 0, nGet);
                }
                return Longs.toByteArray(crc.getValue());
            }
            finally
            {
                in.close();
            }
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static byte[] calculateDigestAdler32(final File filename)
    {
        final int SIZE = 16 * 1024;
        try
        {
            final FileInputStream in = new FileInputStream(filename);
            try
            {
                final FileChannel channel = in.getChannel();
                final Adler32 crc = new Adler32();
                final int length = (int) channel.size();
                final MappedByteBuffer mb = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
                final byte[] bytes = new byte[SIZE];
                int nGet;
                while (mb.hasRemaining())
                {
                    nGet = Math.min(mb.remaining(), SIZE);
                    mb.get(bytes, 0, nGet);
                    crc.update(bytes, 0, nGet);
                }
                return Longs.toByteArray(crc.getValue());
            }
            finally
            {
                in.close();
            }
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static byte[] calculateDigestMd5(final File filename)
    {
        final int SIZE = 16 * 1024;
        try
        {
            final FileInputStream in = new FileInputStream(filename);
            try
            {
                final FileChannel channel = in.getChannel();
                final MessageDigest crc = MessageDigest.getInstance("MD5");
                final int length = (int) channel.size();
                final MappedByteBuffer mb = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
                final byte[] bytes = new byte[SIZE];
                int nGet;
                while (mb.hasRemaining())
                {
                    nGet = Math.min(mb.remaining(), SIZE);
                    mb.get(bytes, 0, nGet);
                    crc.update(bytes, 0, nGet);
                }
                return crc.digest();
            }
            finally
            {
                in.close();
            }
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static byte[] calculateDigestSha1(final File filename)
    {
        final int SIZE = 16 * 1024;
        try
        {
            final FileInputStream in = new FileInputStream(filename);
            try
            {
                final FileChannel channel = in.getChannel();
                final MessageDigest crc = MessageDigest.getInstance("SHA-1");
                final int length = (int) channel.size();
                final MappedByteBuffer mb = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
                final byte[] bytes = new byte[SIZE];
                int nGet;
                while (mb.hasRemaining())
                {
                    nGet = Math.min(mb.remaining(), SIZE);
                    mb.get(bytes, 0, nGet);
                    crc.update(bytes, 0, nGet);
                }
                return crc.digest();
            }
            finally
            {
                in.close();
            }
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static byte[] calculateDigestSha256(final File filename)
    {
        final int SIZE = 16 * 1024;
        try
        {
            final FileInputStream in = new FileInputStream(filename);
            try
            {
                final FileChannel channel = in.getChannel();
                final MessageDigest crc = MessageDigest.getInstance("SHA-256");
                final int length = (int) channel.size();
                final MappedByteBuffer mb = channel.map(FileChannel.MapMode.READ_ONLY, 0, length);
                final byte[] bytes = new byte[SIZE];
                int nGet;
                while (mb.hasRemaining())
                {
                    nGet = Math.min(mb.remaining(), SIZE);
                    mb.get(bytes, 0, nGet);
                    crc.update(bytes, 0, nGet);
                }
                return crc.digest();
            }
            finally
            {
                in.close();
            }
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static List<File> findAllFiles(final File rootDir, final String[] inclusions)
    {
        final List<File> files = Lists.newArrayList();
        final File[] rootFiles = rootDir.listFiles();
        Arrays.sort(rootFiles, new Comparator<File>()
        {
            @Override
            public int compare(final File left, final File right)
            {
                return ComparisonChain
                        .start()
                        .compareFalseFirst(left.isDirectory(), right.isDirectory())
                        .compare(left.getPath(), right.getPath())
                        .result();
            }
        });
        for (final File file : rootDir.listFiles())
        {
            final String fileName = file.getName();
            if (fileName.equals(".git"))
            {
                continue;
            }
            handleFile(files, file);
        }
        return files;
    }

    private static void handleFile(final List<File> files, final File file)
    {
        if (file.isDirectory())
        {
            findFiles(file, files);
        }
        else
        {
            files.add(file);
        }
    }

    private static void findFiles(final File rootDir, final List<File> files)
    {
        for (final File file : rootDir.listFiles())
        {
            handleFile(files, file);
        }
    }
    //    private static List<File> findAllFiles(final File rootDir, final String[] inclusions)
    //    {
    //        final List<File> files = Lists.newArrayList();
    //        final File[] rootFiles = rootDir.listFiles();
    //        Arrays.sort(rootFiles, new Comparator<File>()
    //        {
    //            @Override
    //            public int compare(final File left, final File right)
    //            {
    //                return ComparisonChain
    //                        .start()
    //                        .compareFalseFirst(left.isDirectory(), right.isDirectory())
    //                        .compare(left.getPath(), right.getPath())
    //                        .result();
    //            }
    //        });
    //        for (final File file : rootDir.listFiles())
    //        {
    //            final String fileName = file.getName();
    //            for (final String inclusion : inclusions)
    //            {
    //                if (inclusion.equals(fileName))
    //                {
    //                    handleFile(files, file);
    //                    break;
    //                }
    //            }
    //        }
    //        return files;
    //    }
    //
    //    private static void handleFile(final List<File> files, final File file)
    //    {
    //        if (file.isDirectory())
    //        {
    //            findFiles(file, files);
    //        }
    //        else
    //        {
    //            files.add(file);
    //        }
    //    }
    //
    //    private static void findFiles(final File rootDir, final List<File> files)
    //    {
    //        for (final File file : rootDir.listFiles())
    //        {
    //            handleFile(files, file);
    //        }
    //    }
}

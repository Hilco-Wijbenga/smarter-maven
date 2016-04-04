package org.cavebeetle.maven2;

import java.io.File;
import java.io.IOException;

public final class SmarterMavenRuntime
{
    public static final File toCanonicalFile(final File file)
    {
        try
        {
            return file.getCanonicalFile();
        }
        catch (final IOException e)
        {
            throw new SmarterMavenException("Unable to get a canonical file.", e);
        }
    }
}

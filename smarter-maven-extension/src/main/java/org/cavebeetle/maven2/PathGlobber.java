package org.cavebeetle.maven2;

public final class PathGlobber
{
    public static void main(final String[] args)
    {
        new PathGlobber("~");
        new PathGlobber("src/**/*.java");
    }

    private static enum PathGlob {
        HOME,
        SLASH,
        DIRECTORIES,
        CHARACTERS,
        ANY_CHAR
    }
    
    /**
     * '/'
     * '**'
     * '*'
     * '?'
     * '~'
     * text
     * @param pattern
     */
    public PathGlobber(final String pattern)
    {
        int index;
        if (pattern.charAt(0) == '~')
        {
            System.out.println("HOME");
            index = 1;
        }
        else
        {
            index = 0;
        }
        for (int i = index; i < pattern.length(); i++)
        {
            if (pattern.charAt(i) == '/')
            {
                System.out.println("SLASH");
            }
            else if (pattern.charAt(i) == '*')
            {
                if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '*')
                {
                    System.out.println("DOUBLE STAR");
                    i++;
                }
                else
                {
                    System.out.println("STAR");
                }
            }
            else if (pattern.charAt(i) == '?')
            {
                System.out.println("QUESTION MARK");
            }
            else
            {
                System.out.println(pattern.charAt(i));
            }
        }
    }
}

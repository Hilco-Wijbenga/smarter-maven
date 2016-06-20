package org.cavebeetle.maven2;

import java.util.Collections;
import java.util.List;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public final class Globber
{
    public static void main(final String[] args)
    {
        System.out.println(new Globber("*"));
        System.out.println(new Globber("?"));
        System.out.println(new Globber("??"));
        System.out.println(new Globber("?*"));
        System.out.println(new Globber("*??"));
        System.out.println(new Globber("*??*"));
        System.out.println(new Globber("*?x?*"));
        System.out.println(new Globber("\\*\\?x\\?\\*"));
        System.out.println(new Globber("*?x**?***hello***??**"));
    }

    public static interface Glob
    {
    }

    public static final class ZeroOrMoreCharactersGlob
            implements
                Glob
    {
        public List<GlobSearch> match(final List<Glob> search, final String text)
        {
            if (search.isEmpty() && text.isEmpty())
            {
                return SUCCESS.search();
            }
            else
            {
                final List<GlobSearch> newSearch = Lists.newArrayList();
                newSearch.add(new DefaultGlobSearch(search, text));
                newSearch.add(new DefaultGlobSearch(search, text.substring(1)));
                return newSearch;
            }
        }

        @Override
        public String toString()
        {
            return "(*)";
        }
    }

    public static final class AnyOneCharacterGlob
            implements
                Glob
    {
        public List<GlobSearch> match(final List<Glob> search, final String text)
        {
            if (text.isEmpty())
            {
                return Collections.emptyList();
            }
            else
            {
                return Lists.<GlobSearch> newArrayList(new DefaultGlobSearch(search, text.substring(1)));
            }
        }

        @Override
        public String toString()
        {
            return "(?)";
        }
    }

    public static final class TextGlob
            implements
                Glob
    {
        private final String pattern;

        public TextGlob(final String pattern)
        {
            this.pattern = pattern;
        }

        public List<GlobSearch> match(final List<Glob> search, final String text)
        {
            if (text.startsWith(pattern))
            {
                return Lists.<GlobSearch> newArrayList(new DefaultGlobSearch(search, text.substring(pattern.length())));
            }
            else
            {
                return Collections.emptyList();
            }
        }

        @Override
        public String toString()
        {
            return String.format("'%s'", pattern);
        }
    }

    public static interface GlobSearch
    {
        List<GlobSearch> search();
    }

    public static final GlobSearch SUCCESS = new GlobSearch()
    {
        private final List<GlobSearch> RESULT = Lists.newArrayList(SUCCESS);

        @Override
        public List<GlobSearch> search()
        {
            return RESULT;
        }
    };

    public static final class DefaultGlobSearch
            implements
                GlobSearch
    {
        private final List<Glob> search;
        private final String text;

        public DefaultGlobSearch(final List<Glob> search, final String text)
        {
            this.search = search;
            this.text = text;
        }

        @Override
        public List<GlobSearch> search()
        {
            if (search.isEmpty() && text.isEmpty())
            {
                return Collections.<GlobSearch> emptyList();
            }
            else if (search.isEmpty() && !text.isEmpty() || !search.isEmpty() && text.isEmpty())
            {
                return null;
            }
            else
            {
                return null;
            }
        }
    }

    private final List<Glob> globs;

    public Globber(final String pattern)
    {
        final List<Glob> globs_ = Lists.newArrayList();
        StringBuilder stringBuilder = new StringBuilder();
        boolean previousWasStar = false;
        for (int i = 0; i < pattern.length(); i++)
        {
            if (pattern.charAt(i) == '\\')
            {
                if (i + 1 >= pattern.length())
                {
                    throw new IllegalStateException("Found '\\' without an escaped character.");
                }
                if (pattern.charAt(i + 1) == '\\')
                {
                    stringBuilder.append('\\');
                }
                else if (pattern.charAt(i + 1) == '*')
                {
                    stringBuilder.append('*');
                }
                else if (pattern.charAt(i + 1) == '?')
                {
                    stringBuilder.append('?');
                }
                else
                {
                    throw new IllegalStateException("Found an invalid escaped character.");
                }
                previousWasStar = false;
                i++;
            }
            else if (pattern.charAt(i) == '*')
            {
                if (previousWasStar)
                {
                    continue;
                }
                if (stringBuilder.length() > 0)
                {
                    globs_.add(new TextGlob(stringBuilder.toString()));
                    stringBuilder = new StringBuilder();
                }
                globs_.add(new ZeroOrMoreCharactersGlob());
                previousWasStar = true;
            }
            else if (pattern.charAt(i) == '?')
            {
                previousWasStar = false;
                if (stringBuilder.length() > 0)
                {
                    globs_.add(new TextGlob(stringBuilder.toString()));
                    stringBuilder = new StringBuilder();
                }
                globs_.add(new AnyOneCharacterGlob());
            }
            else
            {
                previousWasStar = false;
                stringBuilder.append(pattern.charAt(i));
            }
        }
        if (stringBuilder.length() > 0)
        {
            globs_.add(new TextGlob(stringBuilder.toString()));
        }
        globs = ImmutableList.copyOf(globs_);
    }

    public void match(final String text)
    {
        final List<List<Glob>> search = Lists.newArrayList();
        search.add(globs);
        match_(search, text);
    }

    private void match_(final List<List<Glob>> search, final String text)
    {
        if (search.isEmpty() && text.isEmpty())
        {
            System.out.println("SUCCESS");
        }
        else if (search.isEmpty() && !text.isEmpty())
        {
            System.out.println("FAILURE");
        }
        else if (!search.isEmpty() && text.isEmpty())
        {
            System.out.println("FAILURE");
        }
        else
        {
            final List<List<Glob>> newSearch = Lists.newArrayList();
            for (final List<Glob> subsearch : search)
            {
            }
        }
    }

    @Override
    public String toString()
    {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Glob glob : globs)
        {
            if (stringBuilder.length() > 0)
            {
                stringBuilder.append(' ');
            }
            stringBuilder.append(glob);
        }
        return stringBuilder.toString();
    }
}

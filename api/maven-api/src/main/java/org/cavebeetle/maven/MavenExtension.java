package org.cavebeetle.maven;

/**
 * A {@code MavenExtension} represents an extension to Maven.
 */
public interface MavenExtension
        extends
            AfterSessionStart,
            AfterProjectsRead
{
    // Empty.
}

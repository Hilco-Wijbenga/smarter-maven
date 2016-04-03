package org.cavebeetle.maven2;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.cavebeetle.maven2.data.ArtifactId;
import org.cavebeetle.maven2.data.Gav;
import org.cavebeetle.maven2.data.GroupId;
import org.cavebeetle.maven2.data.Version;

public interface GavMapper<SOURCE>
{
    Gav map(SOURCE source);

    GavMapper<MavenProject> MAVEN_PROJECT_TO_GAV = new GavMapper<MavenProject>()
    {
        @Override
        public Gav map(final MavenProject source)
        {
            return Gav.make(
                    GroupId.make(source.getGroupId()),
                    ArtifactId.make(source.getArtifactId()),
                    Version.make(source.getVersion()));
        }
    };
    GavMapper<Parent> PARENT_TO_GAV = new GavMapper<Parent>()
    {
        @Override
        public Gav map(final Parent source)
        {
            return Gav.make(
                    GroupId.make(source.getGroupId()),
                    ArtifactId.make(source.getArtifactId()),
                    Version.make(source.getVersion()));
        }
    };
    GavMapper<Dependency> DEPENDENCY_TO_GAV = new GavMapper<Dependency>()
    {
        @Override
        public Gav map(final Dependency source)
        {
            return Gav.make(
                    GroupId.make(source.getGroupId()),
                    ArtifactId.make(source.getArtifactId()),
                    Version.make(source.getVersion()));
        }
    };
    GavMapper<Plugin> PLUGIN_TO_GAV = new GavMapper<Plugin>()
    {
        @Override
        public Gav map(final Plugin source)
        {
            return Gav.make(
                    GroupId.make(source.getGroupId()),
                    ArtifactId.make(source.getArtifactId()),
                    Version.make(source.getVersion()));
        }
    };
    GavMapper<Extension> EXTENSION_TO_GAV = new GavMapper<Extension>()
    {
        @Override
        public Gav map(final Extension source)
        {
            return Gav.make(
                    GroupId.make(source.getGroupId()),
                    ArtifactId.make(source.getArtifactId()),
                    Version.make(source.getVersion()));
        }
    };
}

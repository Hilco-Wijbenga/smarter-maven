package org.cavebeetle.maven.plugins;

import static java.lang.String.format;

public final class Parent
{
    private GroupId groupId;
    private ArtifactId artifactId;
    private Version version;
    private RelativePath relativePath = RelativePath.DEFAULT;

    public GroupId getGroupId()
    {
        return groupId;
    }

    public void setGroupId(
            final GroupId groupId)
    {
        this.groupId = groupId;
    }

    public ArtifactId getArtifactId()
    {
        return artifactId;
    }

    public void setArtifactId(
            final ArtifactId artifactId)
    {
        this.artifactId = artifactId;
    }

    public Version getVersion()
    {
        return version;
    }

    public void setVersion(
            final Version version)
    {
        this.version = version;
    }

    public RelativePath getRelativePath()
    {
        return relativePath;
    }

    public void setRelativePath(
            final RelativePath relativePath)
    {
        this.relativePath = relativePath;
    }

    @Override
    public String toString()
    {
        return format(
                "[Parent groupId='%s', artifactId='%s', version='%s', relativePath='%s']",
                groupId,
                artifactId,
                version,
                relativePath);
    }
}

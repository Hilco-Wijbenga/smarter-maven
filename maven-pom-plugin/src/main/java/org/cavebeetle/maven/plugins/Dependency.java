package org.cavebeetle.maven.plugins;

import static java.lang.String.format;

public final class Dependency
{
    private GroupId groupId;
    private ArtifactId artifactId;
    private Version version;
    private Classifier classifier = Classifier.DEFAULT;
    private Type type = Type.DEFAULT;

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

    public Classifier getClassifier()
    {
        return classifier;
    }

    public void setClassifier(
            final Classifier classifier)
    {
        this.classifier = classifier;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(
            final Type type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return format(
                "[Dependency groupId='%s', artifactId='%s', version='%s', classifier='%s', type='%s']",
                groupId,
                artifactId,
                version,
                classifier,
                type);
    }
}

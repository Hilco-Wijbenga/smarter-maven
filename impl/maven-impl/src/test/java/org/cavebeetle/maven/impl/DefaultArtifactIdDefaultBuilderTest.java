package org.cavebeetle.maven.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import org.cavebeetle.maven.ArtifactId;
import org.junit.Before;
import org.junit.Test;

/**
 * The unit tests for {@code DefaultArtifactId.DefaultBuilder}.
 */
public final class DefaultArtifactIdDefaultBuilderTest
{
    private DefaultArtifactId.DefaultBuilder artifactIdBuilder;

    /**
     * Sets up each unit test.
     */
    @Before
    public void setUp()
    {
        artifactIdBuilder = new DefaultArtifactId.DefaultBuilder();
    }

    /**
     * Tests that a missing artifact id is not valid.
     */
    @Test
    public final void a_missing_artifact_id_is_not_valid()
    {
        try
        {
            artifactIdBuilder.newArtifactId(null);
            fail("Expected an IllegalArgumentException.");
        }
        catch (final IllegalArgumentException e)
        {
            assertEquals("Missing 'artifactId'.", e.getMessage());
        }
    }

    /**
     * Tests that an empty artifact id is not valid.
     */
    @Test
    public final void an_empty_artifact_id_is_not_valid()
    {
        try
        {
            artifactIdBuilder.newArtifactId("");
            fail("Expected an IllegalArgumentException.");
        }
        catch (final IllegalArgumentException e)
        {
            assertEquals("Missing 'artifactId'.", e.getMessage());
        }
    }

    /**
     * Tests that the provided artifact id is used.
     */
    @Test
    public final void the_provided_artifact_id_is_used()
    {
        final String artifactIdAsText = "artifact-id";
        final ArtifactId artifactId = artifactIdBuilder.newArtifactId(artifactIdAsText);
        assertNotNull(artifactId);
        assertSame(artifactIdAsText, artifactId.toString());
    }
}

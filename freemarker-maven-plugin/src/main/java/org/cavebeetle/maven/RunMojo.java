package org.cavebeetle.maven;

import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "run", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class RunMojo
        extends
            AbstractMojo
{
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/freemarker", property = "outputDir", required = true)
    private File outputDirectory;
    @Parameter(required = true)
    private Model model;

    @Override
    public void execute() throws MojoExecutionException
    {
        getLog().info(String.format("outputDir = '%s'", outputDirectory));
    }
}

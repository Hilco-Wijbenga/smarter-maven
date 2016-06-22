package org.cavebeetle.maven;

import static com.google.inject.Guice.createInjector;
import static org.cavebeetle.maven.SmartMavenExtensionVersion.VERSION;
import java.util.Properties;
import org.apache.maven.AbstractMavenLifecycleParticipant;
import org.apache.maven.MavenExecutionException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.rtinfo.RuntimeInformation;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;

/**
 * The Smarter Maven extension determines which modules of a reactor build must be rebuilt.
 */
@Component(
        role = AbstractMavenLifecycleParticipant.class,
        hint = "smarter-maven-extension")
public final class SmarterMavenExtension
        extends
            AbstractMavenLifecycleParticipant
{
    private final MavenExtension mavenExtension;
    @Requirement
    private Logger logger;
    @Requirement
    private RuntimeInformation runtime;
    @Requirement
    private ProjectBuilder projectBuilder;

    /**
     * Creates a new {@code SmarterMavenExtension}.
     */
    public SmarterMavenExtension()
    {
        final AbstractModule guiceModule = new GuiceModule();
        final Injector injector = createInjector(guiceModule);
        mavenExtension = injector.getInstance(MavenApi.class).getMavenExtension();
    }

    @Override
    public void afterProjectsRead(final MavenSession session) throws MavenExecutionException
    {
        super.afterProjectsRead(session);
        mavenExtension.afterProjectsRead(logger, runtime, session, projectBuilder);
    }

    @Override
    public void afterSessionStart(final MavenSession session) throws MavenExecutionException
    {
        super.afterSessionStart(session);
        final Properties userProperties = session.getUserProperties();
        for (final String goal : session.getGoals())
        {
            if ("install".equals(goal))
            {
                userProperties.setProperty(ActiveDetector.SHOW_PROJECT_HIERARCHY_WARNINGS_PROPERTY, "TRUE");
                userProperties.setProperty(ActiveDetector.SMARTER_MAVEN_ACTIVE_PROPERTY, "TRUE");
            }
            else if (ActiveDetector.SHOW_PROJECT_HIERARCHY_WARNINGS_PROPERTY.equals(goal))
            {
                userProperties.setProperty(ActiveDetector.SHOW_PROJECT_HIERARCHY_WARNINGS_PROPERTY, "TRUE");
            }
        }
        session.getGoals().remove(ActiveDetector.SHOW_PROJECT_HIERARCHY_WARNINGS_PROPERTY);
        if (session.getGoals().isEmpty())
        {
            session.getGoals().add("validate");
        }
        mavenExtension.afterSessionStart(VERSION, logger, runtime, session);
    }
}

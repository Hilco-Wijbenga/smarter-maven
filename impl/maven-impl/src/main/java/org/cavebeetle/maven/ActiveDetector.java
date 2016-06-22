package org.cavebeetle.maven;

import org.apache.maven.execution.MavenSession;

/**
 * An {@code ActiveDetector} detects whether the Smart Maven extension is active.
 */
public interface ActiveDetector
{
    /**
     * The name of the user property indicating that the project hierarchy warnings should be shown.
     */
    String SHOW_PROJECT_HIERARCHY_WARNINGS_PROPERTY = "show-project-hierarchy-warnings";
    /**
     * The name of the user property indicating that the project hierarchy warnings should be shown.
     */
    String SMARTER_MAVEN_ACTIVE_PROPERTY = "smarter-maven-active";

    /**
     * Returns whether the Smart Maven extension is active. This is the case when the {@code install} phase is invoked.
     *
     * @param session
     *            the {@code MavenSession} instance.
     * @return {@code true} if and only if the Smart Maven extension is active.
     */
    boolean isSmarterMavenActive(MavenSession session);

    /**
     * Returns whether to show the project hierarchy warnings. This is the case when the {@code validate} phase is
     * invoked.
     *
     * @param session
     *            the {@code MavenSession} instance.
     * @return {@code true} if and only if the project hierarchy warnings must be shown.
     */
    boolean showProjectHierarchyWarnings(MavenSession session);

    /**
     * Returns whether to show the banner when Maven starts. This is the case when either
     * {@code ActiveDetector#isActive_(MavenSession)} or
     * {@code ActiveDetector#showProjectHierarchyWarnings(MavenSession)} returns {@code true}.
     *
     * @param session
     *            the {@code MavenSession} instance.
     * @return {@code true} if and only if the banner must be shown when Maven starts.
     */
    boolean showBanner(MavenSession session);
}

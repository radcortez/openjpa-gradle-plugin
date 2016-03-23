package com.radcortez.gradle.plugin.openjpa

import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class OpenJpaExtension {
    Project project

    @Input
    @Optional
    boolean addDefaultConstructor = true
    @Input
    @Optional
    boolean enforcePropertyRestrictions = false
    @Input
    @Optional
    String includes = "*.class"
    @Input
    @Optional
    String excludes = ""
    @Input
    @Optional
    String persistenceXml = "META-INF/persistence.xml"
    @Input
    @Optional
    boolean metamodel = false
    @Input
    @Optional
    String metamodelOutputFolder = project.buildDir.absolutePath + "/generated"
    @Input
    @Optional
    String metamodelDependency = "org.apache.openjpa:openjpa:2.4.0"

    OpenJpaExtension(Project project) {
        this.project = project
    }
}

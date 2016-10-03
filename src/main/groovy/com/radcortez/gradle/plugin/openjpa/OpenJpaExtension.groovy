package com.radcortez.gradle.plugin.openjpa

import org.gradle.api.InvalidUserDataException
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
    def classes

    @Input
    @Optional
    String persistenceXml = "META-INF/persistence.xml"
    @Input
    @Optional
    String includes
    @Input
    @Optional
    String excludes

    OpenJpaExtension(final Project project) {
        this.project = project
    }

    def getClasses() {
        if (classes == null) {
            classes = project.fileTree(project.sourceSets.main.output.classesDir).matching {
                // Need to use this. because there is a method with the same name as the instance variable
                if (this.includes != null)
                    include this.includes

                // Need to use this. because there is a method with the same name as the instance variable
                if (this.excludes != null)
                    exclude this.excludes
            }.files
        }

        return classes
    }

    File getPersistenceXmlFile() {
        def persistenceXml = project.fileTree(project.sourceSets.main.output.resourcesDir).matching {
            include persistenceXml
        }

        if (persistenceXml.isEmpty() || persistenceXml.files.size() > 1) {
            throw new InvalidUserDataException(
                    "Could not find valid persistence.xml in path " + this.persistenceXml)
        }

        persistenceXml.singleFile
    }

    URL[] getClasspath() {
        def classes = project.sourceSets.main.output.classesDir.toURI().toURL()

        def compileJars = project.configurations["compile"].files.collect { jar ->
            jar.toURI().toURL()
        }

        def providedJars = project.configurations["providedCompile"].files.collect { jar ->
            jar.toURI().toURL()
        }

        def resources = project.sourceSets.main.resources.srcDirs.collect { resource ->
            resource.toURI().toURL()
        }

        return ([classes] + compileJars + providedJars + resources) as URL[]
    }
}

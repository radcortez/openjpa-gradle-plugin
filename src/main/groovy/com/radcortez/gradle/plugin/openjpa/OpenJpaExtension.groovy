package com.radcortez.gradle.plugin.openjpa

import com.radcortez.gradle.plugin.openjpa.metamodel.MetamodelExtension
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.internal.ClosureBackedAction
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class OpenJpaExtension {
    Project project
    Set<File> classes

    @Input
    @Optional
    String persistenceXml = "META-INF/persistence.xml"
    @Input
    @Optional
    Set<String> includes
    @Input
    @Optional
    Set<String> excludes

    boolean addMetamodel = false

    OpenJpaExtension(final Project project) {
        this.project = project
    }

    void metamodel(Closure closure) {
        addMetamodel = true
        project.extensions.findByType(OpenJpaExtension).extensions
                .configure(MetamodelExtension.class, new ClosureBackedAction(closure))
    }

    def getClasses() {
        if (classes == null) {
            classes = project.sourceSets.main.output.classesDirs.collectMany { classesDir ->
                project.fileTree(classesDir).matching {
                    // Need to use this. because there is a method with the same name as the instance variable
                    if (this.includes != null)
                        this.includes.forEach { include it }

                    // Need to use this. because there is a method with the same name as the instance variable
                    if (this.excludes != null)
                        this.excludes.forEach { exclude it }
                }.files
            }
        }

        return classes
    }

    File getPersistenceXmlFile() {
        def persistenceXmlFile
        // Check if persistence.xml is in the resource dirs.
        project.sourceSets.main.resources.srcDirs.collect { resourceDir ->
            def persistenceXmlFileCandidate = project.fileTree(resourceDir).matching {
                include persistenceXml
            }

            if (!persistenceXmlFileCandidate.isEmpty()) {
                if (persistenceXmlFile == null) {
                    persistenceXmlFile = persistenceXmlFileCandidate.singleFile
                } else {
                    throw new InvalidUserDataException("Multiple persistence.xml files found in path: " +
                            persistenceXmlFile + ", " + persistenceXmlFileCandidate)
                }
            }
        }

        // Nothing found. Fallback to plain file.
        if (persistenceXmlFile == null) {
            persistenceXmlFile = project.file(persistenceXml)
            if (!persistenceXmlFile.exists()) {
                throw new InvalidUserDataException(
                        "Could not find valid persistence.xml in path " + this.persistenceXml)
            }
        }

        persistenceXml = persistenceXmlFile
    }

    URL[] getClasspath() {
        def classes = project.sourceSets.main.output.classesDirs.collect { it.toURI().toURL() }

        def compileJars = project.configurations["compile"].files.collect { jar ->
            jar.toURI().toURL()
        }

        // This scope is only available with the war plugin.
        def providedJars
        if (project.configurations.hasProperty("providedCompile")) {
            providedJars = project.configurations["providedCompile"].files.collect { jar ->
                jar.toURI().toURL()
            }
        } else {
            providedJars = []
        }

        def resources = project.sourceSets.main.resources.srcDirs.collect { resource ->
            resource.toURI().toURL()
        }

        return (classes + compileJars + providedJars + resources) as URL[]
    }
}

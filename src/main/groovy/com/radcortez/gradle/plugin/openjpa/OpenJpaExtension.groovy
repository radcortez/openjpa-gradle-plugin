package com.radcortez.gradle.plugin.openjpa

import com.radcortez.gradle.plugin.openjpa.sql.SqlExtension
import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.SourceSet
import org.gradle.util.ClosureBackedAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class OpenJpaExtension {
    private static final Logger LOG = LoggerFactory.getLogger(OpenJpaExtension.class)

    Project project
    Set<File> classes
    Set<File> testClasses

    SourceSet sourceSet = project.sourceSets.main

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
    boolean generateSql = false

    OpenJpaExtension(final Project project) {
        this.project = project
    }

    void metamodel(Closure closure) {
        addMetamodel = true
        project.extensions.findByType(OpenJpaExtension).extensions
                .configure(MetamodelExtension.class, new ClosureBackedAction(closure))
    }

    void sql(Closure closure) {
        generateSql = true
        project.extensions.findByType(OpenJpaExtension).extensions
                .configure(SqlExtension.class, new ClosureBackedAction(closure))
    }

    def getClasses() {
        if (sourceSet == project.sourceSets.main) {
            if (classes == null) {
                classes = sourceSet.output.classesDirs.collectMany { classesDir ->
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
        } else {
            if (testClasses == null) {
                testClasses = sourceSet.output.classesDirs.collectMany { classesDir ->
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
            return testClasses
        }
    }

    File getPersistenceXmlFile() {
        def persistenceXmlFile
        // Check if persistence.xml is in the resource dirs.
        sourceSet.resources.srcDirs.collect { resourceDir ->
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
        def compileJars = project.configurations.compileClasspath.files.collect { jar ->
            jar.toURI().toURL()
        }
        def resources = sourceSet.resources.srcDirs.collect { resource ->
            resource.toURI().toURL()
        }

        if (sourceSet == project.sourceSets.test) {
            classes = classes + project.sourceSets.test.output.classesDirs.collect { it.toURI().toURL() }
            compileJars = compileJars + project.configurations.testCompileClasspath.files.collect { jar ->
                jar.toURI().toURL()
            }
        }

        LOG.info("Compile Jars")
        compileJars.each { LOG.info(it.toString())}
        LOG.info("Resources ABCDEF")
        resources.each {LOG.info(it.toString())}

        return (classes + compileJars + resources) as URL[]
    }
}

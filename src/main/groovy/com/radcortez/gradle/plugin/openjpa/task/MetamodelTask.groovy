package com.radcortez.gradle.plugin.openjpa.task

import com.radcortez.gradle.plugin.openjpa.OpenJpaExtension
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.tasks.compile.JavaCompile

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class MetamodelTask extends JavaCompile {
    MetamodelTask() {
        project.afterEvaluate {
            def configuration = project.extensions.findByType(OpenJpaExtension)

            def SourceDirectorySet mainJava = project.sourceSets.main.java
            source(mainJava.srcDirs)

            project.dependencies { DependencyHandler d -> d.add("compile", configuration.metamodelDependency) }
            setClasspath(project.configurations.compile)

            destinationDir = project.file(configuration.metamodelOutputFolder)

            options.compilerArgs += [
                    "-Aopenjpa.source=8",
                    "-Aopenjpa.metamodel=true",
                    "-proc:only",
                    "-processor", "org.apache.openjpa.persistence.meta.AnnotationProcessor6"
            ]

            mainJava.srcDir(destinationDir)
        }
    }
}

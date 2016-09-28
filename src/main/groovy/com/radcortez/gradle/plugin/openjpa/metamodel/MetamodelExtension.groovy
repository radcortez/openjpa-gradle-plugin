package com.radcortez.gradle.plugin.openjpa.metamodel

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

import java.nio.file.Paths

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class MetamodelExtension {
    Project project

    @Input
    @Optional
    String metamodelOutputFolder = project.buildDir.absolutePath + "/generated"
    @Input
    @Optional
    String metamodelDependency = "org.apache.openjpa:openjpa:2.4.0"

    MetamodelExtension(final Project project) {
        this.project = project

        project.afterEvaluate() {
            // Clean the generated metamodel folder.
            project.tasks.clean.doLast {
                cleanMetamodelOutputFolder()
            }

            // Generate metamodel source files before Java compile.
            project.convention.getPlugin(JavaPluginConvention).sourceSets.all {
                project.tasks.compileJava.dependsOn project.tasks.metamodel
            }
        }
    }

    void cleanMetamodelOutputFolder() {
        def metamodelOutputFolder = project.file(metamodelOutputFolder)
        def path = Paths.get(metamodelOutputFolder.path)
        metamodelOutputFolder.deleteDir()

        while (path.parent != Paths.get(project.buildDir.path)) {
            path = path.parent
            if (path.toFile().exists() && path.toFile().listFiles().length == 0) {
                println(path)
                path.deleteDir()
            } else {
                break;
            }
        }
    }
}

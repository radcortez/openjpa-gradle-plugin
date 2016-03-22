package com.radcortez.gradle.plugin.openjpa

import com.radcortez.gradle.plugin.openjpa.task.EnhanceTask
import com.radcortez.gradle.plugin.openjpa.task.MetamodelTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention

import java.nio.file.Paths

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class OpenJpaPlugin implements Plugin<Project> {
    @Override
    void apply(final Project project) {
        project.extensions.create("openjpa", OpenJpaExtension, project)

        project.task(
                type: EnhanceTask,
                group: "OpenJPA",
                description: "Enhances entity classes with the OpenJPA Enhancer tool.",
                dependsOn: "classes",
                "enhance")

        project.task(
                type: MetamodelTask,
                group: "OpenJPA",
                description: "Generates metamodel classes.",
                "metamodel")

        project.afterEvaluate() {
            def OpenJpaExtension configuration = project.extensions.openjpa

            project.tasks.clean.doLast { cleanMetamodelOutputFolder(project) }

            project.tasks.classes.doLast { project.tasks.enhance.execute() }

            if (configuration.metamodel) {
                project.convention.getPlugin(JavaPluginConvention).sourceSets.all {
                    project.tasks.compileJava.dependsOn project.tasks.metamodel
                }
            }
        }
    }

    private static void cleanMetamodelOutputFolder(Project project) {
        def OpenJpaExtension configuration = project.extensions.openjpa

        def metamodelOutputFolder = project.file(configuration.metamodelOutputFolder)
        def path = Paths.get(metamodelOutputFolder.path)
        metamodelOutputFolder.deleteDir()

        while (path.parent != Paths.get(project.buildDir.path)) {
            path = path.parent
            if (path.toFile().exists() && path.toFile().listFiles().length == 0) {
                path.deleteDir()
            } else {
                break;
            }
        }
    }
}

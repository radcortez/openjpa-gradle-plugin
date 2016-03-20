package com.radcortez.gradle.plugin.openjpa

import com.radcortez.gradle.plugin.openjpa.task.EnhanceTask
import com.radcortez.gradle.plugin.openjpa.task.MetamodelTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention

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
            if (project.extensions.findByType(OpenJpaExtension).metamodel) {
                project.convention.getPlugin(JavaPluginConvention).sourceSets.all {
                    project.tasks.compileJava.dependsOn project.tasks.metamodel
                }
            }
        }
    }
}

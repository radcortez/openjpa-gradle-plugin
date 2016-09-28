package com.radcortez.gradle.plugin.openjpa

import com.radcortez.gradle.plugin.openjpa.metamodel.MetamodelExtension
import com.radcortez.gradle.plugin.openjpa.task.EnhanceTask
import com.radcortez.gradle.plugin.openjpa.metamodel.MetamodelTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class OpenJpaPlugin implements Plugin<Project> {
    @Override
    void apply(final Project project) {
        project.extensions.create("openjpa", OpenJpaExtension)
        project.openjpa.extensions.create("metamodel", MetamodelExtension, project)

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
            project.tasks.classes.doLast { project.tasks.enhance.execute() }
        }
    }
}

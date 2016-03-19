package com.radcortez.gradle.plugin.openjpa

import com.radcortez.gradle.plugin.openjpa.task.EnhanceTask
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
        project.task(
                type: EnhanceTask,
                group: "OpenJPA",
                description: "Enhances entity classes with the OpenJPA Enhancer tool",
                dependsOn: "classes",
                "enhance")
    }
}

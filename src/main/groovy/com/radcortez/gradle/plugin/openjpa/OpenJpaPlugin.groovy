package com.radcortez.gradle.plugin.openjpa

import com.radcortez.gradle.plugin.openjpa.enhance.EnhanceExtension
import com.radcortez.gradle.plugin.openjpa.enhance.EnhanceTask
import com.radcortez.gradle.plugin.openjpa.metamodel.MetamodelExtension
import com.radcortez.gradle.plugin.openjpa.metamodel.MetamodelTask
import com.radcortez.gradle.plugin.openjpa.sql.SqlExtension
import com.radcortez.gradle.plugin.openjpa.sql.SqlTask
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
        project.extensions.create("openjpa", OpenJpaExtension, project)
        project.openjpa.extensions.create("enhance", EnhanceExtension, project)
        project.openjpa.extensions.create("metamodel", MetamodelExtension, project)
        project.openjpa.extensions.create("sql", SqlExtension, project)

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

        project.task(
                type: SqlTask,
                group: "OpenJPA",
                description: "Generates database schema SQL.",
                dependsOn: "classes",
                "sql")
    }
}

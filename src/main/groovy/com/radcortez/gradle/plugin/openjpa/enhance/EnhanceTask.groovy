package com.radcortez.gradle.plugin.openjpa.enhance

import com.radcortez.gradle.plugin.openjpa.OpenJpa
import com.radcortez.gradle.plugin.openjpa.OpenJpaExtension
import org.apache.openjpa.lib.util.Options
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.TaskAction

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class EnhanceTask extends DefaultTask {
    @TaskAction
    void enhance() {
        project.pluginManager.apply(JavaPlugin)

        OpenJpaExtension openJpaConfiguration = project.extensions.findByType(OpenJpaExtension)
        EnhanceExtension configuration = openJpaConfiguration.extensions.findByType(EnhanceExtension)

        def classes = project.sourceSets.main.output.classesDir

        def entities = project.fileTree(classes).matching {
            include configuration.includes
            exclude configuration.excludes
        }

        Thread.currentThread().contextClassLoader.addURL(classes.toURI().toURL())

        OpenJpa.openJpa(new Options([
                "addDefaultConstructor"      : Boolean.toString(configuration.addDefaultConstructor),
                "enforcePropertyRestrictions": Boolean.toString(configuration.enforcePropertyRestrictions),
                "propertiesFile"             : openJpaConfiguration.persistenceXmlFile
        ])).enhance(entities.files as String[])
    }
}

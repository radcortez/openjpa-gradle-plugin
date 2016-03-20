package com.radcortez.gradle.plugin.openjpa.task

import com.radcortez.gradle.plugin.openjpa.OpenJpaExtension
import org.apache.openjpa.enhance.PCEnhancer
import org.apache.openjpa.lib.util.Options
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.TaskAction

import static org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class EnhanceTask extends DefaultTask {
    @TaskAction
    void enhance() {
        project.pluginManager.apply(JavaPlugin)
        def configuration = project.extensions.findByType(OpenJpaExtension)

        def classes = project.convention.getPlugin(JavaPluginConvention)
                .sourceSets.getByName(MAIN_SOURCE_SET_NAME).output.classesDir

        def entities = project.fileTree(classes).matching {
            include configuration.includes
            exclude configuration.excludes
        }

        def options = new Options()
        options.put("addDefaultConstructor", Boolean.toString(configuration.addDefaultConstructor))
        options.put("enforcePropertyRestrictions", Boolean.toString(configuration.enforcePropertyRestrictions))
        options.put("propertiesFile", findPersistenceXml())

        Thread.currentThread().contextClassLoader.addURL(classes.toURI().toURL())

        PCEnhancer.run(entities.files as String[], options)
    }

    File findPersistenceXml() {
        def resources = project.convention.getPlugin(JavaPluginConvention)
                .sourceSets.getByName(MAIN_SOURCE_SET_NAME).getOutput().getResourcesDir()

        project.fileTree(resources).matching { include 'META-INF/persistence.xml' }.singleFile
    }
}

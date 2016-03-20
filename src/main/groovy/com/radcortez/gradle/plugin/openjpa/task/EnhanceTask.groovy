package com.radcortez.gradle.plugin.openjpa.task

import com.radcortez.gradle.plugin.openjpa.OpenJpaExtension
import org.apache.openjpa.enhance.PCEnhancer
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
        def configuration = project.extensions.findByType(OpenJpaExtension)

        def classes = project.sourceSets.main.output.classesDir

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
        project.fileTree(project.sourceSets.main.output.resourcesDir).matching {
            include 'META-INF/persistence.xml'
        }.singleFile
    }
}

package com.radcortez.gradle.plugin.openjpa.task

import com.radcortez.gradle.plugin.openjpa.OpenJpaExtension
import org.apache.openjpa.enhance.PCEnhancer
import org.apache.openjpa.lib.util.Options
import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.TaskAction

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class EnhanceTask extends DefaultTask {
    def OpenJpaExtension configuration

    @TaskAction
    void enhance() {
        project.pluginManager.apply(JavaPlugin)
        configuration = project.extensions.findByType(OpenJpaExtension)

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
        def persistenceXml = project.fileTree(project.sourceSets.main.output.resourcesDir).matching {
            include configuration.persistenceXml
        }

        if (persistenceXml.isEmpty() || persistenceXml.files.size() > 1) {
            throw new InvalidUserDataException(
                    "Could not find valid persistence.xml in path " + configuration.persistenceXml)
        }

        persistenceXml.singleFile
    }
}

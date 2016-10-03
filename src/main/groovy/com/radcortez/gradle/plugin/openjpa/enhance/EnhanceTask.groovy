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

        def openJpa = OpenJpa.openJpa(openJpaConfiguration.classpath, new Options([
                "addDefaultConstructor"      : configuration.addDefaultConstructor.toBoolean(),
                "enforcePropertyRestrictions": configuration.enforcePropertyRestrictions.toBoolean(),
                "tmpClassLoader"             : configuration.tmpClassLoader.toBoolean(),
                "propertiesFile"             : openJpaConfiguration.persistenceXmlFile
        ]))

        openJpa.enhance(openJpaConfiguration.classes as String[])
        openJpa.dispose()
    }
}

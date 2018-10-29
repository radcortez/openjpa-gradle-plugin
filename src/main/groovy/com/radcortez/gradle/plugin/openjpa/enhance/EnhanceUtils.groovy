package com.radcortez.gradle.plugin.openjpa.enhance

import com.radcortez.gradle.plugin.openjpa.OpenJpa
import com.radcortez.gradle.plugin.openjpa.OpenJpaExtension
import org.apache.openjpa.lib.util.Options
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class EnhanceUtils {
    static void enhance(Project project) {
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

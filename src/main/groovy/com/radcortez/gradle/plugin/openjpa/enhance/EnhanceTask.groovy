package com.radcortez.gradle.plugin.openjpa.enhance

import com.radcortez.gradle.plugin.openjpa.OpenJpa
import com.radcortez.gradle.plugin.openjpa.OpenJpaExtension
import org.apache.openjpa.lib.util.Options
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class EnhanceTask extends DefaultTask {
    static final Logger LOG = LoggerFactory.getLogger(EnhanceTask.class)

    @TaskAction
    void enhance() {
        project.pluginManager.apply(JavaPlugin)

        OpenJpaExtension openJpaConfiguration = project.extensions.findByType(OpenJpaExtension)
        EnhanceExtension configuration = openJpaConfiguration.extensions.findByType(EnhanceExtension)

        LOG.debug("persistenceXml = {}", openJpaConfiguration.persistenceXml)
        LOG.debug("addDefaultConstructor = {}", configuration.addDefaultConstructor)
        LOG.debug("enforcePropertyRestrictions = {}", configuration.enforcePropertyRestrictions)
        LOG.debug("tmpClassLoader = {}", configuration.tmpClassLoader)

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

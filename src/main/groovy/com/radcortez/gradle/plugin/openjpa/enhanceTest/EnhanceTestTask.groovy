package com.radcortez.gradle.plugin.openjpa.enhanceTest

import com.radcortez.gradle.plugin.openjpa.OpenJpa
import com.radcortez.gradle.plugin.openjpa.OpenJpaExtension
import com.radcortez.gradle.plugin.openjpa.enhance.EnhanceExtension
import org.apache.openjpa.lib.util.Options
import org.gradle.api.DefaultTask
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Task to enhance classes under test/java for unit testing
 */
class EnhanceTestTask extends DefaultTask {
    static final Logger LOG = LoggerFactory.getLogger(EnhanceTestTask.class)

    @TaskAction
    void enhanceTest() {
        SourceSet sourceSet = project.sourceSets.test
        project.pluginManager.apply(JavaPlugin)

        OpenJpaExtension openJpaConfiguration = project.extensions.findByType(OpenJpaExtension)
        openJpaConfiguration.sourceSet = project.sourceSets.test
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

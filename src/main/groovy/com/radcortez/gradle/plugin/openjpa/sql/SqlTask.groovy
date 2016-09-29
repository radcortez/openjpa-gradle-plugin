package com.radcortez.gradle.plugin.openjpa.sql

import com.radcortez.gradle.plugin.openjpa.OpenJpa
import com.radcortez.gradle.plugin.openjpa.OpenJpaExtension
import org.apache.openjpa.lib.util.Options
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import javax.persistence.Entity

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class SqlTask extends DefaultTask {

    @TaskAction
    void sql() {
        OpenJpaExtension openJpaConfiguration = project.extensions.findByType(OpenJpaExtension)
        SqlExtension configuration = openJpaConfiguration.extensions.findByType(SqlExtension)

        def classes = project.sourceSets.main.output.classesDir
        Thread.currentThread().contextClassLoader.addURL(classes.toURI().toURL())
        project.configurations["compile"].files.collect { jar ->
            Thread.currentThread().contextClassLoader.addURL(jar.toURI().toURL())
        }
        project.sourceSets.main.resources.srcDirs.collect { resource ->
            Thread.currentThread().contextClassLoader.addURL(resource.toURI().toURL())
        }

        def openJpa = OpenJpa.openJpa(new Options([
                "propertiesFile"      : openJpaConfiguration.persistenceXmlFile,
                "ConnectionDriverName": configuration.connectionDriverName,
                "schemaAction"        : configuration.schemaAction,
                "sqlFile"             : configuration.sqlFile
        ]))

        def entities = []
        project.fileTree(classes).each { classFile ->
            openJpa.parseTypes(classFile.absolutePath).each { klass ->
                if (klass.isAnnotationPresent(Entity.class)) {
                    entities.add(classFile.absolutePath)
                }
            }
        }

        openJpa.mappingTool(entities as String[])
    }
}

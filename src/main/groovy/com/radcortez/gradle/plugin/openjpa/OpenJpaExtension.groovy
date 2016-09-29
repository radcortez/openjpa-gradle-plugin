package com.radcortez.gradle.plugin.openjpa

import org.gradle.api.InvalidUserDataException
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class OpenJpaExtension {
    Project project

    @Input
    @Optional
    String persistenceXml = "META-INF/persistence.xml"

    OpenJpaExtension(final Project project) {
        this.project = project
    }

    File getPersistenceXmlFile() {
        def persistenceXml = project.fileTree(project.sourceSets.main.output.resourcesDir).matching {
            include persistenceXml
        }

        if (persistenceXml.isEmpty() || persistenceXml.files.size() > 1) {
            throw new InvalidUserDataException(
                    "Could not find valid persistence.xml in path " + this.persistenceXml)
        }

        persistenceXml.singleFile
    }
}

package com.radcortez.gradle.plugin.openjpa

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class OpenJpaExtension {
    @Input
    @Optional
    String persistenceXml = "META-INF/persistence.xml"
}

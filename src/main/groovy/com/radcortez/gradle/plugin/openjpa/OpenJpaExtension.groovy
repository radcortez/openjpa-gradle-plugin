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
    boolean addDefaultConstructor = true
    @Input
    @Optional
    boolean enforcePropertyRestrictions = false
    @Input
    @Optional
    def includes = "*.class"
    @Input
    @Optional
    def excludes = ""
}

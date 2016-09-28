package com.radcortez.gradle.plugin.openjpa.enhance

import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class EnhanceExtension {
    Project project

    @Input
    @Optional
    boolean addDefaultConstructor = true
    @Input
    @Optional
    boolean enforcePropertyRestrictions = false
    @Input
    @Optional
    String includes = "*.class"
    @Input
    @Optional
    String excludes = ""

    EnhanceExtension(final Project project) {
        this.project = project

        project.afterEvaluate() {
            project.tasks.classes.doLast { project.tasks.enhance.execute() }
        }
    }
}

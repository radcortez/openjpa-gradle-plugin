package com.radcortez.gradle.plugin.openjpa.enhanceTest

import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Extension for EnhanceTest task; to enhance classes under test/java for unit testing
 */
class EnhanceTestExtension {
    Project project

    @Input
    @Optional
    boolean addDefaultConstructor = true
    @Input
    @Optional
    boolean enforcePropertyRestrictions = false
    @Input
    @Optional
    boolean tmpClassLoader = false;

    EnhanceTestExtension(final Project project) {
        this.project = project

        project.afterEvaluate() {
            def runEnhance = false
            project.tasks.testClasses.doLast {
                runEnhance = true
            }
            project.tasks.enhanceTest.onlyIf {
                runEnhance
            }
            project.tasks.testClasses.finalizedBy(project.tasks.enhanceTest)
        }
    }
}

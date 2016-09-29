package com.radcortez.gradle.plugin.openjpa.sql

import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class SqlExtension {
    Project project

    @Input
    String connectionDriverName
    @Input
    @Optional
    String schemaAction = "build"
    @Input
    String sqlFile = project.buildDir.absolutePath + "/database.sql"

    SqlExtension(final Project project) {
        this.project = project
    }
}

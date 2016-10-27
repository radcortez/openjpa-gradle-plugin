package com.radcortez.gradle.plugin.openjpa.sql

import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

import java.nio.file.Paths

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
    @Optional
    String sqlFile = "build/database.sql"

    SqlExtension(final Project project) {
        this.project = project

        project.afterEvaluate() {
            project.tasks.clean.doLast {
                cleanSqlOutputFolder()
            }

            project.tasks.sql.dependsOn project.tasks.classes
        }
    }

    String getSqlFile() {
        return project.rootDir.path + "/" + sqlFile
    }

    void cleanSqlOutputFolder() {
        def sqlFile = project.file(getSqlFile()).parentFile
        def path = Paths.get(sqlFile.path)
        sqlFile.deleteDir()

        while (path.parent != Paths.get(project.rootDir.path)) {
            path = path.parent
            if (path.toFile().exists() && path.toFile().listFiles().length == 0) {
                path.deleteDir()
            } else {
                break;
            }
        }
    }
}

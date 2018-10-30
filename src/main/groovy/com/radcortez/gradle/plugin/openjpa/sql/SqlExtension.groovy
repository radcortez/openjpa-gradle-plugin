package com.radcortez.gradle.plugin.openjpa.sql

import com.radcortez.gradle.plugin.openjpa.OpenJpaExtension
import org.gradle.api.Project

import java.nio.file.Paths

/**
 * Description.
 *
 * @author Roberto Cortez
 */
class SqlExtension {
    Project project

    String connectionDriverName
    String schemaAction = "build"
    String sqlFile = "build/database.sql"

    SqlExtension(final Project project) {
        this.project = project

        project.afterEvaluate() {
            OpenJpaExtension configuration = project.extensions.findByType(OpenJpaExtension)

            project.tasks.clean.doLast {
                cleanSqlOutputFolder()
            }

            def runSql = false
            project.tasks.classes.doLast {
                runSql = true
            }

            project.tasks.sql.onlyIf {
                runSql && configuration.generateSql
            }
            project.tasks.sql.mustRunAfter("enhance")
            project.tasks.classes.finalizedBy(project.tasks.sql)
        }
    }

    String getSqlFile() {
        return project.rootDir.path + "/" + sqlFile
    }

    void cleanSqlOutputFolder() {
        def sqlFile = project.file(getSqlFile()).parentFile
        def path = Paths.get(sqlFile.path)

        if (sqlFile.isFile()) {
            sqlFile.deleteDir()
        }

        while (path.parent != Paths.get(project.rootDir.path)) {
            path = path.parent
            if (path.toFile().exists() && path.toFile().listFiles().length == 0) {
                path.deleteDir()
            } else {
                break
            }
        }
    }
}

# OpenJPA Gradle Plugin

## Usage
At the moment, the plugin is still in a earlier development stage. You need to checkout the project, build it and 
install it in your local Maven repository. You need to have Gradle installed.

 * Build with `gradle build`
 * Add it to the local Maven repository with `gradle publishToMavenLocal`
 
Then in a Gradle project where you want to use the OpenJPA Grade Plugin, add the following snippet:
 
```gradle
apply plugin: 'openjpa'

buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath 'com.radcortez.gradle:openjpa-gradle-plugin:0.2-SNAPSHOT'
    }
}
```

### Tasks
The OpenJPA Gradle Plugin has three taks:
* enhance
* metamodel
* sql

#### Enhance (Enhances entity classes with the OpenJPA Enhancer tool)
The `enhance` Task is performed automatically in your build after the `classes` Task. You can also call it separately
with `gradle enhance`.

You can pass additional configuration to the `enhance` task with:

```gradle
openjpa {
    enhance {
       
    }
}
```

#### Metamodel (Generates JPA Metamodel classes)
If you want to generate Metamodel classes for your entity classes, you have to explicitly activate it, by adding the 
following configuration:

```gradle
openjpa {
    metamodel {
       
    }
}
```

Metamodel sources will be generated into the directory `${buildDir}/generated`

#### SQL (Generates the database schema to a file)
To run the `sql` task properly, you need to setup the target database to generate the sql schema. You can do it with 
the following configuration:

```gradle
openjpa {
    sql {
        connectionDriverName 'oracle.jdbc.OracleDriver'
    }
}
```

You don't need the driver in the classpath, just the name of the driver that you use to connect to the target database.

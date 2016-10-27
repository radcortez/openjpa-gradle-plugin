# OpenJPA Gradle Plugin

## Usage
In a Gradle project where you want to use the OpenJPA Gradle Plugin, add the following snippet:
 
```gradle
apply plugin: 'openjpa'

buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath 'com.radcortez.gradle:openjpa-gradle-plugin:0.3-SNAPSHOT'
    }
}
```

### Tasks
The OpenJPA Gradle Plugin has three tasks:
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
        addDefaultConstructor true
        enforcePropertyRestrictions false
        tmpClassLoader false
    }
}
```

You can omit the configuration if you use the default values.

#### Metamodel (Generates JPA Metamodel classes)
If you want to generate Metamodel classes for your entity classes, you have to explicitly activate it, by adding the 
following configuration:

```gradle
openjpa {
    metamodel {
       
    }
}
```

You can pass additional configuration to the `metamodel` task with:
```gradle
openjpa {
    metamodel {
        metamodelOutputFolder 'build/generated'
        metamodelDependency 'org.apache.openjpa:openjpa:2.4.0'
    }
}
```

You can omit the configuration if you use the default values.

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

The `connectionDriverName` is mandatory. You don't need the driver in the classpath, just the name of the driver that 
you use to connect to the target database. OpenJPA will try to guess the correct database dictionary to use to generate
the correct SQL statements.

You can pass additional configuration to the `sql` task with:
```gradle
openjpa {
    sql {
        connectionDriverName 'oracle.jdbc.OracleDriver'
        sqlFile 'build/database.sql'
    }
}
```

### Configuration
The OpenJPA Gradle Plugin also allows you to configure global properties to all tasks:
```gradle
openjpa {
    includes ''
    excludes ''
    persistenceXml 'META-INF/persistence.xml'
}
```

### Configuration Details

**includes**
* _Description_: Include files from the classpath into the OpenJPA Tools.
* _Type:_ String
* _Default:_ none
* _Required:_ No
* _Scope:_ global

**excludes**
* _Description_: Exclude files from the classpath into the OpenJPA Tools.
* _Type:_ String
* _Default:_ none
* _Required:_ No
* _Scope:_ global

**persistenceXml**
* _Description_: Location of the persistence.xml file.
* _Type:_ String
* _Default:_ META-INF/persistence.xml
* _Required:_ No
* _Scope:_ global

**addDefaultConstructor**
* _Description_: Add a default constructors to the Entity Enhancement.
* _Type:_ boolean
* _Default:_ true
* _Required:_ No
* _Scope:_ enhance

**enforcePropertyRestrictions**
* _Description_: Throw an exception when an entity property access is not obeying the restrictions placed on property access.
* _Type:_ boolean
* _Default:_ false
* _Required:_ No
* _Scope:_ enhance

**tmpClassLoader**
* _Description_: Tell the PCEnhancer to use a temporary classloader for enhancement.
* _Type:_ boolean
* _Default:_ false
* _Required:_ No
* _Scope:_ enhance

**metamodelOutputFolder**
* _Description_: The output folder to use to generate the metamodel source files.
* _Type:_ String
* _Default:_ build/generated
* _Required:_ No
* _Scope:_ metamodel

**metamodelDependency**
* _Description_: The OpenJPA dependency to use to generate the metamodel source files.
* _Type:_ String
* _Default:_ org.apache.openjpa:openjpa:2.4.0
* _Required:_ No
* _Scope:_ metamodel

**connectionDriverName**
* _Description_: The Connection Driver Name to determine the databsase to generate the Database Schema.
* _Type:_ String
* _Default:_ none
* _Required:_ Yes
* _Scope_: sql

**sqlFile**
* _Description_: The SQL file name to generate the Database Schema.
* _Type:_ String
* _Default:_ build/database.sql
* _Required:_ No
* _Scope_: sql

### Full Example:
```gradle
openjpa {
    excludes '**/*.class'

    metamodel {
        metamodelOutputFolder 'target/generated-sources/metamodel'
    }

    sql {
        connectionDriverName 'oracle.jdbc.OracleDriver'
    }
}
```

This example will exclude all classes from the OpenJPA tools, so it will only use the entities referenced in the 
`persistence.xml`. The metamodel source files are generated at 'target/generated-sources/metamodel' and are 
automatically part of the build. An Oracle database SQL schema can be generated with `gradle sql` in the file 
`build/database.sql`.

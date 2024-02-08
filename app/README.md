###Realm database
1. build.gradle(project)
        buildScript{
            dependencies {
               classpath "io.realm:realm-gradle-plugin:10.11.1"
            }
        }
2. build.gradle(module)
   realm{
      syncEnabled = true
      }
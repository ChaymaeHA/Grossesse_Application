allprojects {
    repositories {
        google()
        mavenCentral()
    }

    subprojects {
        afterEvaluate {
            // 'this' est le Project évalué
            if (plugins.hasPlugin("com.android.application") || plugins.hasPlugin("com.android.library")) {
                // Récupère l'extension Android, si présente
                extensions.findByType(com.android.build.gradle.BaseExtension::class.java)?.let { androidExt ->
                    if (androidExt.namespace == null) {
                        androidExt.namespace = group.toString()
                    }
                }
            }
        }
    }
}

val newBuildDir: Directory = rootProject.layout.buildDirectory.dir("../../build").get()
rootProject.layout.buildDirectory.value(newBuildDir)

subprojects {
    val newSubprojectBuildDir: Directory = newBuildDir.dir(project.name)
    project.layout.buildDirectory.value(newSubprojectBuildDir)
}
subprojects {
    project.evaluationDependsOn(":app")
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

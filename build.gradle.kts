plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

group = "com.translated.lara"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
}

val generatedResourcesDir = layout.buildDirectory.dir("generated/resources").get()
sourceSets {
    main {
        resources {
            srcDir(generatedResourcesDir)
        }
    }
}

tasks.register("generateProjectProperties") {
    doLast {
        val propertiesFile = file(generatedResourcesDir.file("com/translated/lara/lara-java.properties"))
        propertiesFile.parentFile.mkdirs()
        propertiesFile.writeText("version=${project.version}\n")
    }
}

tasks.named("processResources") {
    dependsOn("generateProjectProperties")
}
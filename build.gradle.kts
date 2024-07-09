plugins {
    id("java-library")
    id("maven-publish")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

group = "com.translated.lara"
version = "1.1.1"

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

publishing {
    publications {
        create<MavenPublication>("maven") {
            pom {
                name = "lara-java"
                description = "Official Lara SDK for Java"
                url = "https://lara.translated.com/"
                licenses {
                    license {
                        name = "The MIT License"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }
                developers {
                    developer {
                        id = "translated"
                        name = "Translated"
                        url = "https://translated.com/"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/translated/lara-java.git"
                    developerConnection = "scm:git:ssh://github.com/translated/lara-java.git"
                    url = "https://github.com/translated/lara-java"
                }
            }

            from(components["java"])
        }
    }
    repositories {
        mavenLocal()
    }
}

tasks.register("install") {
    dependsOn("build", "publishToMavenLocal")
}
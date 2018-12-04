import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.0"
}

group = "munn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.register<JavaExec>("executeScript") {
    main = findProperty("mainClass") as? String ?: ""
    args = (findProperty("file") as? String)?.let { listOf(it) } ?: emptyList()
    classpath = sourceSets["main"].runtimeClasspath
}

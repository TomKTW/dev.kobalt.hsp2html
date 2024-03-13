plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.kobalt"
version = "0000.00.00.00.00.00.000"

repositories {
    mavenCentral()
}

fun ktor(module: String, version: String) = "io.ktor:ktor-$module:$version"
fun exposed(module: String, version: String) = "org.jetbrains.exposed:exposed-$module:$version"
fun general(module: String, version: String) = "$module:$version"
fun kotlinx(module: String, version: String) = "org.jetbrains.kotlinx:kotlinx-$module:$version"
fun kotlinw(module: String, version: String) = "org.jetbrains.kotlin-wrappers:kotlin-$module:$version"

fun DependencyHandler.csvParser() {
    implementation(general("com.github.doyaaaaaken:kotlin-csv-jvm", "0.7.3"))
}

fun DependencyHandler.commandLineInterface() {
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.6")
}

fun DependencyHandler.standardLibrary() {
    implementation(kotlin("stdlib", "1.9.22"))
}

fun DependencyHandler.serialization() {
    implementation(kotlinx("serialization-json", "1.6.3"))
}

fun DependencyHandler.logger() {
    implementation(general("org.slf4j:slf4j-simple", "2.0.11"))
}

fun DependencyHandler.htmlDsl() {
    implementation(kotlinx("html-jvm", "0.7.3"))
}

fun DependencyHandler.cssDsl() {
    implementation(kotlinw("css-jvm", "1.0.0-pre.242-kotlin-1.5.30"))
}

dependencies {
    commandLineInterface()
    standardLibrary()
    serialization()
    csvParser()
    logger()
    htmlDsl()
    cssDsl()
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveFileName.set("hsp2html.jar")
        mergeServiceFiles()
        manifest {
            attributes("Main-Class" to "dev.kobalt.hsp2html.jvm.MainKt")
        }
    }
}

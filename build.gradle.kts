plugins {
    kotlin("jvm") version "2.1.10"
}

group = "nl.evanv"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val letsPlotVersion = "4.5.2"
val letsPlotKotlinVersion = "4.9.3"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.6.0")

    implementation("org.jetbrains.lets-plot:lets-plot-kotlin-jvm:$letsPlotKotlinVersion")
    implementation("org.jetbrains.lets-plot:platf-awt-jvm:$letsPlotVersion")
    implementation("org.jetbrains.lets-plot:deprecated-in-v4-jvm:$letsPlotVersion")

    //    For PNG export demo
    implementation("org.jetbrains.lets-plot:lets-plot-image-export:$letsPlotVersion")

    implementation("org.slf4j:slf4j-simple:1.7.32")  // Enable logging to console

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

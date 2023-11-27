plugins {
    kotlin("jvm") version "1.7.21"
}

group = "nl.dirkgroot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.6")

    testImplementation(kotlin("test-junit5"))

    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-property:5.8.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.27.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testRuntimeOnly("org.junit.platform:junit-platform-console:1.9.0")
}

tasks {
    test {
        useJUnitPlatform()
    }
}

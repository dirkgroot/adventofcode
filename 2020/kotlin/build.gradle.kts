plugins {
    kotlin("jvm") version "2.2.20"
}

group = "nl.dirkgroot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.4.0")

    testImplementation(kotlin("test-junit5"))

    testImplementation("io.kotest:kotest-runner-junit5:6.0.0")
    testImplementation("io.kotest:kotest-property:6.0.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.14.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.14.0")
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.28.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.14.0")
    testRuntimeOnly("org.junit.platform:junit-platform-console:1.14.0")
}

tasks {
    test {
        useJUnitPlatform()
    }
}

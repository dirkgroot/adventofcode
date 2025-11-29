plugins {
    kotlin("jvm") version "2.2.21"
}

group = "nl.dirkgroot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs.addAll("-Xconsistent-data-class-copy-visibility")
    }
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("com.willowtreeapps.assertk:assertk-jvm:0.28.1")
}

tasks {
    test {
        useJUnitPlatform()
    }
}

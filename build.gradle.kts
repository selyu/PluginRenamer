import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.1.0"
}

group = "xyz.selyu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }

    maven {
        url = uri("https://github.com/selyuorg/maven-repo/raw/master")
    }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")

    implementation(kotlin("stdlib-jdk8"))
    implementation("xyz.selyu:larry:1.2-SNAPSHOT")
    implementation("me.wolflie:ConfigUtil:1.0-SNAPSHOT")
}

tasks {
    named<ShadowJar>("shadowJar") {
        mergeServiceFiles()
    }

    build {
        dependsOn(shadowJar)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
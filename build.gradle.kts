plugins {
    id("com.android.application") version "7.3.1" apply false
    id("com.android.library") version "7.3.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.20" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
    }
}


subprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            val requested = requested
            if (requested.group == "org.jetbrains.kotlin" &&
                (requested.name == "kotlin-stdlib-jdk8" ||
                    requested.name == "kotlin-stdlib-jdk7" ||
                    requested.name == "kotlin-reflect" ||
                    requested.name == "kotlin-stdlib" ||
                    requested.name == "kotlin-stdlib-common")
            ) {
                useVersion("1.7.20")
            }
        }
    }
}
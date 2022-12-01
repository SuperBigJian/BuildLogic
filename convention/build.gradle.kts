plugins {
    `kotlin-dsl`
    id("publish.maven")
}

group = "io.github.superbigjian.plugin.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "convention.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidCompose") {
            id = "convention.android.compose"
            implementationClass = "AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "convention.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidLibrary") {
            id = "convention.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("mavenPublish") {
            id = "convention.android.maven.publish"
            implementationClass = "AndroidMavenPublishConventionPlugin"
        }
        register("androidModule") {
            id = "convention.android.module"
            implementationClass = "AndroidModuleConventionPlugin"
        }
        register("androidPermissions") {
            id = "convention.android.permissions"
            implementationClass = "AndroidPermissionsConventionPlugin"
        }
        register("androidRoom") {
            id = "convention.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidTest") {
            id = "convention.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
    }
}

dependencies {
    implementation(libs.androidPlugin)
    implementation(libs.kotlinPlugin)
}

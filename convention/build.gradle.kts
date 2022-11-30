/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    `kotlin-dsl`
    id("version-catalog")
    id("maven-publish")
}

group = "com.cyaan.convention.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
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
        register("androidRoom") {
            id = "convention.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidPermissions") {
            id = "convention.android.permissions"
            implementationClass = "AndroidPermissionsConventionPlugin"
        }
        register("androidLibrary") {
            id = "convention.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidModule") {
            id = "convention.android.module"
            implementationClass = "AndroidModuleConventionPlugin"
        }
        register("androidTest") {
            id = "convention.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
    }
}

catalog {
    // declare the aliases, bundles and versions in this block
    versionCatalog {
        from(files("../libs.versions.toml"))
    }
}

publishing {
    publications {
        create<MavenPublication>("Catalog") {
            groupId = "io.github.superbigjian.buildlogic"
            artifactId = "catalog"
            version = "1.0.0"
            from(components["versionCatalog"])
        }

        create<MavenPublication>("Convention") {
            groupId = "io.github.superbigjian.buildlogic"
            artifactId = "convention"
            version = "1.0.0"
            from(components["kotlin"])
        }
    }
}



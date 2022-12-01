val POM_GROUP_ID = "io.github.superbigjian.plugin"
val POM_ARTIFACT_ID = "maven-publish-tools"
val POM_VERSION_NAME = "1.0.0"
val POM_NAME = "PublishTools"
val POM_DESCRIPTION = "Gradle Maven Publish Plugin"

val OSSRH_USERNAME = "Cyaan"
val OSSRH_PASSWORD = "Fatezero123!"

val GITHUB_ID = "SuperBigJian"
val GITHUB_NAME = "Cyann"
val GITHUB_EMAIL = "cyaanchen@gmail.com"
val Github_CODE_PATH = "SuperBigJian/BuildLogic"
val GITHUB_CODE_BRANCH = "master"

group = "io.github.superbigjian.plugin"

plugins {
    `kotlin-dsl`
    id("maven-publish")
    id("signing")
}

gradlePlugin {
    plugins {
        register("gradleMavenPublishPlugin") {
            id = "publish.maven"
            implementationClass = "GradleMavenPublishPlugin"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

publishing {
    publications {
        create<MavenPublication>(POM_NAME) {
            // The coordinates of the library, being set from variables that
            // we"ll set up in a moment
            groupId = POM_GROUP_ID
            artifactId = POM_ARTIFACT_ID
            version = POM_VERSION_NAME
            from(components["java"])

            // Self-explanatory metadata for the most part
            pom {
                name.set(POM_NAME)
                setDescription(POM_DESCRIPTION)

                // If your project has a dedicated site, use its URL here
                url.set("https://github.com/$Github_CODE_PATH")
                licenses {
                    license {
                        //协议类型，一般默认Apache License2.0的话不用改：
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set(GITHUB_ID)
                        name.set(GITHUB_NAME)
                        email.set(GITHUB_EMAIL)
                    }
                }
                // Version control info, if you"re using GitHub, follow the format as seen here
                scm {
                    //修改成你的Git地址：https://github.com/SuperBigJian/BuildLogic.git
                    connection.set("scm:git:github.com/${Github_CODE_PATH}.git")
                    developerConnection.set("scm:git:ssh://github.com/${Github_CODE_PATH}.git")
                    //分支地址：
                    url.set("https://github.com/${Github_CODE_PATH}/tree/$GITHUB_CODE_BRANCH")
                }
            }
        }
    }

    repositories {

        // The repository to publish to, Sonatype/MavenCentral
        maven {
            // This is an arbitrary name, you may also use "mavencentral" or
            // any other name that's descriptive for you
            name = POM_NAME

            val releasesRepoUrl = "https://s01.oss.sonatype.org/content/repositories/releases/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            // You only need this if you want to publish snapshots, otherwise just set the URL
            // to the release repo directly
            setUrl(if (POM_VERSION_NAME.endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)
            // The username and password we've fetched earlier
            credentials {
                username = OSSRH_USERNAME
                password = OSSRH_PASSWORD
            }
        }
    }
}

signing {
    sign(publishing.publications)
}
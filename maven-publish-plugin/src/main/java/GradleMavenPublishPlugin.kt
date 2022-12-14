/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import com.android.build.api.variant.Variant
import io.github.superbigjian.plugin.publish.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.create

class GradleMavenPublishPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val mVersion = findOptionalProperty(POM_VERSION_NAME) ?: ""
            version = mVersion
            val mGroupId = findOptionalProperty(POM_GROUP_ID)
            val mArtifactId = findOptionalProperty(POM_ARTIFACT_ID)
            val mName = findOptionalProperty(POM_NAME) ?: ""
            val mDescription = findOptionalProperty(POM_DESCRIPTION)
            val mComponent = findOptionalProperty(POM_COMPONENT) ?: "kotlin"

            val mUserName = findOptionalProperty(OSSRH_USERNAME)
            val mPassword = findOptionalProperty(OSSRH_PASSWORD)

            val mAccountID = findOptionalProperty(GITHUB_ID)
            val mAccountName = findOptionalProperty(GITHUB_NAME)
            val mAccountEmail = findOptionalProperty(GITHUB_EMAIL)

            val mCodePath = findOptionalProperty(Github_CODE_PATH)
            val mGitBranch = findOptionalProperty(GITHUB_CODE_BRANCH)

            with(pluginManager) {
                apply("maven-publish")
                apply("signing")
            }

            gradlePublishing.publications {

                create<MavenPublication>(mName) {
                    // The coordinates of the library, being set from variables that
                    // we"ll set up in a moment
                    groupId = mGroupId
                    artifactId = mArtifactId
                    version = mVersion
                    when (mComponent) {
                        "java", "kotlin" -> {
                            from(components.getByName(mComponent))
                        }
                        "android" -> {
                            androidComponents.onVariants {
                                if ("release" == it.buildType) {
                                    artifact(getReleaseOutput(target, it))
                                }
                            }
                        }
                    }
                    // Self-explanatory metadata for the most part
                    pom {
                        name.set(mName)
                        setDescription(mDescription)

                        // If your project has a dedicated site, use its URL here
                        url.set("https://github.com/$mCodePath")
                        licenses {
                            license {
                                //???????????????????????????Apache License2.0??????????????????
                                name.set("The Apache License, Version 2.0")
                                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            }
                        }
                        developers {
                            developer {
                                id.set(mAccountID)
                                name.set(mAccountName)
                                email.set(mAccountEmail)
                            }
                        }
                        // Version control info, if you"re using GitHub, follow the format as seen here
                        scm {
                            //???????????????Git?????????https://github.com/SuperBigJian/BuildLogic.git
                            connection.set("scm:git:github.com/${mCodePath}.git")
                            developerConnection.set("scm:git:ssh://github.com/${mCodePath}.git")
                            //???????????????
                            url.set("https://github.com/${mCodePath}/tree/$mGitBranch")
                        }
                    }
                }
            }

            gradlePublishing.repositories {
                // The repository to publish to, Sonatype/MavenCentral
                maven {
                    // This is an arbitrary name, you may also use "mavencentral" or
                    // any other name that's descriptive for you
                    name = mName

                    val releasesRepoUrl = "https://s01.oss.sonatype.org/content/repositories/releases/"
                    val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                    // You only need this if you want to publish snapshots, otherwise just set the URL
                    // to the release repo directly
                    setUrl(if (versionIsSnapshot) snapshotsRepoUrl else releasesRepoUrl)
                    // The username and password we've fetched earlier
                    credentials {
                        username = mUserName
                        password = mPassword
                    }
                }
            }

            gradleSigning.sign(gradlePublishing.publications)
        }
    }


    private fun getReleaseOutput(project: Project, variant: Variant?): String {
        return findBundle(project, variant)?.name
            ?: if (variant == null) {
                "${project.buildDir}/libs/${project.name}.jar"
            } else {
                if (variant.flavorName.isNullOrBlank()) {
                    "${project.buildDir}/outputs/aar/${project.name}-release.aar"
                } else {
                    "${project.buildDir}/outputs/aar/${project.name}-${variant.flavorName}-release.aar"
                }
            }
    }

    private fun findBundle(project: Project, variant: Variant?): Task? {
        return if (variant == null) {
            project.tasks.findByName("jar")
        } else {
            project.tasks.findByName("bundle${variant.flavorName?.capitalize()}Release")
                ?: project.tasks.findByName("bundle${variant.flavorName?.capitalize()}ReleaseAar")
        }
    }

    companion object {
        const val POM_GROUP_ID = "POM_GROUP_ID"
        const val POM_ARTIFACT_ID = "POM_ARTIFACT_ID"
        const val POM_VERSION_NAME = "POM_VERSION_NAME"
        const val POM_NAME = "POM_NAME"
        const val POM_DESCRIPTION = "POM_DESCRIPTION"
        const val POM_COMPONENT = "POM_COMPONENT"

        const val OSSRH_USERNAME = "OSSRH_USERNAME"
        const val OSSRH_PASSWORD = "OSSRH_PASSWORD"

        const val GITHUB_ID = "GITHUB_ID"
        const val GITHUB_NAME = "GITHUB_NAME"
        const val GITHUB_EMAIL = "GITHUB_EMAIL"
        const val Github_CODE_PATH = "Github_CODE_PATH"
        const val GITHUB_CODE_BRANCH = "GITHUB_CODE_BRANCH"
    }
}
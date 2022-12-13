package io.github.superbigjian.plugin.publish

import com.android.build.api.AndroidPluginVersion
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.plugins.signing.SigningExtension

internal fun Project.findOptionalProperty(propertyName: String) = findProperty(propertyName)?.toString()

internal val Project.versionIsSnapshot: Boolean
    get() = version.toString().endsWith("SNAPSHOT", true)

internal inline val Project.gradleSigning: SigningExtension
    get() = extensions.getByType(SigningExtension::class.java)

internal inline val Project.gradlePublishing: PublishingExtension
    get() = extensions.getByType(PublishingExtension::class.java)

internal inline val Project.androidComponents: AndroidComponentsExtension<*, *, *>
    get() = extensions.getByType(AndroidComponentsExtension::class.java)

internal fun Project.isAtLeastUsingAndroidGradleVersion(major: Int, minor: Int, patch: Int): Boolean {
    return try {
        androidComponents.pluginVersion >= AndroidPluginVersion(major, minor, patch)
    } catch (e: NoClassDefFoundError) {
        // was added in 7.0
        false
    }
}

internal fun Project.isAtLeastUsingAndroidGradleVersionBeta(major: Int, minor: Int, patch: Int, beta: Int): Boolean {
    return try {
        androidComponents.pluginVersion >= AndroidPluginVersion(major, minor, patch).beta(beta)
    } catch (e: NoClassDefFoundError) {
        // was added in 7.0
        false
    }
}

internal fun Project.isAtLeastUsingAndroidGradleVersionAlpha(major: Int, minor: Int, patch: Int, alpha: Int): Boolean {
    return try {
        androidComponents.pluginVersion >= AndroidPluginVersion(major, minor, patch).alpha(alpha)
    } catch (e: NoClassDefFoundError) {
        // was added in 7.0
        false
    }
}

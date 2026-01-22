import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.serialization)

}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting
        val androidMain by getting

        // ✅ Shared Apple (iOS) source set
        val appleMain by creating {
            dependsOn(commonMain)
        }

        // ✅ IMPORTANT: connect iOS source sets to appleMain
        val iosX64Main by getting { dependsOn(appleMain) }
        val iosArm64Main by getting { dependsOn(appleMain) }
        val iosSimulatorArm64Main by getting { dependsOn(appleMain) }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            // ✅ Android-only Ktor engine stays in androidMain
            implementation(libs.ktor.android.client)

            implementation(libs.koin.android)
            implementation(libs.splash.screen)
        }

        appleMain.dependencies {
            // ✅ iOS-only Ktor engine stays in appleMain
            implementation(libs.ktor.darwin.client)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Lifecycle ViewModel for CMP
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Navigation
            implementation(libs.compose.navigation)

            // ✅ Ktor common (multiplatform) deps only here
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // Serialization
            implementation(libs.kotlinx.serialization)

            // Coil3 (KMP)
            implementation(libs.coil3)
            implementation(libs.coil3.compose)
            implementation(libs.coil3.compose.core)
            implementation(libs.coil3.network.ktor)

            // KMP notifier
            implementation(libs.kmp.notifier)

            // Multiplatform settings
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.no.arg)
            implementation(libs.multiplatform.settings.make.observable)

            // Browser + Message Bar (KMP)
            implementation(libs.browser.kmp)
            implementation(libs.messagebar.kmp)


            // Koin (KMP)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Kotzilla SDK (KMP)
            implementation(libs.kotzilla.sdk.ktor3)


        }
    }
}

android {
    namespace = "ngo.friendship.mhealth.dc"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    // ✅ REQUIRED (fixes "non-flavored defaultConfigs must be provided")
    defaultConfig {
        applicationId = "ngo.friendship.mhealth.dc"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

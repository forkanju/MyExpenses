import com.codingfeline.buildkonfig.compiler.FieldSpec.Type

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room3)
}

kotlin {
    jvmToolchain(ProjectConfig.javaVersion)
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xcontext-parameters",
            "-Xexpect-actual-classes",
            "-Xcontext-sensitive-resolution",
            "-Xexplicit-backing-fields",
            "-XXLanguage:+ExplicitBackingFields",
            "-XXLanguage:+PropertyParamAnnotationDefaultTargetMode"
        )

        optIn.addAll(
            "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "androidx.compose.material3.ExperimentalMaterial3ExpressiveApi",
            "androidx.compose.foundation.ExperimentalFoundationApi",
            "androidx.compose.foundation.ExperimentalFoundationApi",
            "androidx.compose.ui.ExperimentalComposeUiApi",
            "kotlinx.serialization.ExperimentalSerializationApi",
            "kotlin.time.ExperimentalTime"
        )
    }
    androidLibrary {
        namespace = ProjectConfig.packageNameCommon
        compileSdk = ProjectConfig.compileSdk
        minSdk = ProjectConfig.minSdk
        androidResources {
            enable = true
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
    }
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.configure {
            kotlin.srcDir(layout.buildDirectory.dir("generated/ksp/metadata/commonMain/kotlin"))
        }
        commonMain.dependencies {
            implementation(libs.compose.components.resources)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.compose.ui.backhandler)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.savedstate.compose)

            implementation(libs.bundles.koin)
            implementation(libs.bundles.ktor)
            implementation(libs.bundles.coil)
            implementation(libs.bundles.settings)
            implementation(libs.bundles.nav)

//            // Room
//            implementation(libs.androidx.room.runtime)
//            implementation(libs.androidx.sqlite.bundled)

            implementation(libs.material.icons.core)
            implementation(libs.material.icons.extended)

            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.serialization.properties)
            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.connectivity.core)
            implementation(libs.connectivity.device)
            implementation(libs.connectivity.compose.device)

            api(libs.ktor.monitor.logging)
            implementation(libs.kotlinx.datetime)
            // Browser + Message Bar (KMP)
//            implementation(libs.browser.kmp)
            implementation(libs.messagebar.kmp)

            // Kotzilla SDK (KMP)
            implementation(libs.kotzilla.sdk.ktor3)

            implementation(libs.material.icons.extended)
            implementation(libs.animation)


            // Room
            implementation(libs.androidx.room3.runtime)
            implementation(libs.androidx.sqlite.bundled)

        }
    }
}

buildkonfig {
    packageName = ProjectConfig.packageName
    defaultConfigs {
        buildConfigField(Type.STRING, "packageName", ProjectConfig.packageName, const = true)
        buildConfigField(Type.STRING, "versionName", ProjectConfig.versionName, const = true)
        buildConfigField(Type.INT, "versionCode", ProjectConfig.versionCode.toString(), const = true)
    }
}

room3 {
    schemaDirectory("$projectDir/schemas")
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
    add("kspAndroid", libs.androidx.room3.compiler)
    add("kspIosArm64", libs.androidx.room3.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room3.compiler)
}

tasks.matching { it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata" }
    .configureEach {
        dependsOn("kspCommonMainKotlinMetadata")
    }

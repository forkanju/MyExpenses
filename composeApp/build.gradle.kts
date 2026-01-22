import com.codingfeline.buildkonfig.compiler.FieldSpec.Type

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.ksp)
}

kotlin {
    jvmToolchain(ProjectConfig.javaVersion)
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xcontext-parameters",
            "-Xwhen-guards",
            "-Xnon-local-break-continue",
            "-Xexpect-actual-classes",
            "-Xnested-type-aliases",
            "-Xcontext-sensitive-resolution",
            "-Xdata-flow-based-exhaustiveness",
            "-Xallow-holdsin-contract",
            "-Xallow-contracts-on-more-functions",
            "-Xallow-condition-implies-returns-contracts",
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
            "kotlin.time.ExperimentalTime",
            "org.koin.core.annotation.KoinExperimentalAPI",
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
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
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
            api(libs.koin.annotations)
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

            if (ProjectConfig.IS_DEBUG)
                implementation(libs.ktor.monitor.logging)
            else
                implementation(libs.ktor.monitor.logging.no.op)
            implementation(libs.kotlinx.datetime)
            // Browser + Message Bar (KMP)
//            implementation(libs.browser.kmp)
            implementation(libs.messagebar.kmp)

            // Kotzilla SDK (KMP)
            implementation(libs.kotzilla.sdk.ktor3)

        }
    }
}

buildkonfig {
    packageName = ProjectConfig.packageName
    defaultConfigs {
        buildConfigField(Type.STRING, "packageName", ProjectConfig.packageName, const = true)
        buildConfigField(Type.STRING, "versionName", ProjectConfig.versionName, const = true)
        buildConfigField(Type.INT, "versionCode", ProjectConfig.versionCode.toString(), const = true)
        buildConfigField(Type.BOOLEAN, "IS_DEBUG", ProjectConfig.IS_DEBUG.toString(), const = true)
        buildConfigField(
            Type.STRING, "BASE_URL",
            if (ProjectConfig.IS_DEBUG)
                ProjectConfig.BASE_URL_DEV
            else
                ProjectConfig.BASE_URL_LIVE,
            const = true
        )
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

dependencies {
    "androidRuntimeClasspath"(libs.compose.ui.tooling)
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)

    add("kspAndroid", libs.koin.ksp.compiler)
    add("kspIosArm64", libs.koin.ksp.compiler)
    add("kspIosSimulatorArm64", libs.koin.ksp.compiler)
}

tasks.matching { it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata" }
    .configureEach {
        dependsOn("kspCommonMainKotlinMetadata")
    }
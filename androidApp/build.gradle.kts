plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
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
    }
}

android {
    namespace = ProjectConfig.packageName
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.packageName
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
//        debug {
//            isDebuggable = false
//            isMinifyEnabled = true
//            isShrinkResources = true
//        }
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        buildFeatures {
            buildConfig = true
        }
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach {
            val output = it as com.android.build.api.variant.impl.VariantOutputImpl
            val projectName = rootProject.name.replace(" ", "_")
            val version = output.versionName.get()
            val buildType = variant.name
            output.outputFileName = "${projectName}_v$version-$buildType.apk"
        }
    }
}

dependencies {
    implementation(project(":composeApp"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.slf4j.simple)
}

//tasks.register("updatePlistVersion") {
//    doLast {
//        val plistFile = rootProject.file("iosApp/iosApp/Info.plist")
//        var content = plistFile.readText()
//
//        val updates = mapOf(
//            "CFBundleVersion" to ProjectConfig.versionCode,
//            "CFBundleShortVersionString" to ProjectConfig.versionName
//        )
//
//        updates.forEach { (key, value) ->
//            val regex = Regex(
//                """<key>$key</key>\s*<string>.*?</string>""",
//                RegexOption.DOT_MATCHES_ALL
//            )
//            content = if (regex.containsMatchIn(content)) {
//                regex.replace(content) {
//                    "<key>$key</key>\n\t<string>$value</string>"
//                }
//            } else {
//                content.replaceFirst(
//                    "<dict>",
//                    "<dict>\n\t<key>$key</key>\n\t<string>$value</string>"
//                )
//            }
//        }
//
//        plistFile.writeText(content)
//    }
//}

//tasks.named("preBuild") {
//    dependsOn("updatePlistVersion")
//}
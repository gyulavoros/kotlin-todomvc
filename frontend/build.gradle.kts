import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.npm.NpmExtension
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

group = "co.makery.todomvc.frontend"

plugins {
  id("kotlin2js") version kotlinVersion
  id("kotlin-dce-js") version kotlinVersion
  id("org.jetbrains.kotlin.frontend") version frontendPluginVersion
}

dependencies {
  compile("org.jetbrains.kotlin:kotlin-stdlib-js:$kotlinVersion")
  compile("org.jetbrains.kotlin:kotlin-test-js:$kotlinVersion")
  compile("org.jetbrains.kotlinx:kotlinx-html-js:$kotlinxHtmlVersion")
}

kotlinFrontend {
  sourceMaps = true

  npm {
    dependency("css-loader")
    dependency("style-loader")
    devDependency("karma")
  }

  bundle<WebPackExtension>("webpack", {
    (this as WebPackExtension).apply {
      port = 8080
      publicPath = "/frontend/"
      proxyUrl = "http://localhost:9000"
    }
  })

  define("PRODUCTION", true)
}

tasks {
  "compileKotlin2Js"(Kotlin2JsCompile::class) {
    kotlinOptions {
      metaInfo = true
      outputFile = "${project.buildDir.path}/js/${project.name}.js"
      sourceMap = true
      sourceMapEmbedSources = "always"
      moduleKind = "commonjs"
      main = "call"
    }
  }

  "compileTestKotlin2Js"(Kotlin2JsCompile::class) {
    kotlinOptions {
      metaInfo = true
      outputFile = "${project.buildDir.path}/js-tests/${project.name}-tests.js"
      sourceMap = true
      moduleKind = "commonjs"
      main = "call"
    }
  }

  "copyResources"(Copy::class) {
    val mainSrc = java.sourceSets["main"]
    from(mainSrc.resources.srcDirs)
    into(file(buildDir.path + "/js/min"))
  }
}

afterEvaluate {
  tasks["webpack-bundle"].dependsOn("copyResources")
  tasks["webpack-run"].dependsOn("copyResources")
  tasks["webpack-bundle"].dependsOn("runDceKotlinJs")
  tasks["webpack-run"].dependsOn("runDceKotlinJs")
}

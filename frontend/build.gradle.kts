import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.npm.NpmExtension
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

group = "co.makery.todomvc.frontend"

buildscript {
  repositories {
    jcenter()
    maven { setUrl("https://dl.bintray.com/kotlin/kotlin-eap") }
  }

  dependencies {
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
  }
}

plugins {
  java
}

apply {
  plugin("kotlin2js")
  plugin("org.jetbrains.kotlin.frontend")
}

repositories {
  maven { setUrl("https://dl.bintray.com/kotlin/kotlinx.html") }
}

dependencies {
  compile("org.jetbrains.kotlin:kotlin-stdlib-js:$kotlinVersion")
  compile("org.jetbrains.kotlin:kotlin-test-js:$kotlinVersion")
  compile("org.jetbrains.kotlinx:kotlinx-html-js:$htmlVersion")
}

configure<KotlinFrontendExtension> {
  sourceMaps = true

  configure<NpmExtension> {
    devDependency("css-loader")
    devDependency("style-loader")
    devDependency("karma")
  }

  bundle<WebPackExtension>("webpack", {
    (this as WebPackExtension).apply {
      port = 8080
      publicPath = "/frontend/"
      proxyUrl = "http://localhost:9000"
    }
  })

  define("PRODUCTION", false)
}

tasks.getByPath("compileKotlin2Js").configure(closureOf<Kotlin2JsCompile> {
  kotlinOptions {
    metaInfo = true
    outputFile = "${project.buildDir.path}/js/${project.name}.js"
    sourceMap = true
    moduleKind = "commonjs"
    main = "call"
  }
})

tasks.getByPath("compileTestKotlin2Js").configure(closureOf<Kotlin2JsCompile> {
  kotlinOptions {
    metaInfo = true
    outputFile = "${project.buildDir.path}/js-tests/${project.name}-tests.js"
    sourceMap = true
    moduleKind = "commonjs"
    main = "call"
  }
})

val mainSrc = java.sourceSets.getByName("main")

tasks {
  "copyResources"(Copy::class) {
    from(mainSrc.resources.srcDirs)
    into(file(buildDir.path + "/js"))
  }
}

afterEvaluate {
  tasks["webpack-bundle"].dependsOn("copyResources")
  tasks["webpack-run"].dependsOn("copyResources")
}

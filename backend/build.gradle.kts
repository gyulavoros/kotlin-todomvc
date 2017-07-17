import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "co.makery.todomvc.backend"

plugins {
  application
  kotlin("jvm", kotlinVersion)
}

application {
  mainClassName = "org.jetbrains.ktor.netty.DevelopmentHost"
}

kotlin {
  experimental.coroutines = Coroutines.ENABLE
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

repositories {
  maven { setUrl("https://dl.bintray.com/kotlin/ktor") }
  maven { setUrl("https://dl.bintray.com/kotlin/kotlinx.html") }
}

dependencies {
  compile("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
  compile("org.jetbrains.kotlinx:kotlinx-html-jvm:$htmlVersion")
  compile("org.jetbrains.ktor:ktor-netty:0.3.3")
  compile("org.jetbrains.ktor:ktor-html-builder:0.3.3")
  compile("azadev.kotlin:aza-kotlin-css:1.0")
}

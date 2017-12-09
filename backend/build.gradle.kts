import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "co.makery.todomvc.backend"

plugins {
  application
  kotlin("jvm") version kotlinVersion
}

application {
  mainClassName = "io.ktor.server.netty.DevelopmentEngine"
}

kotlin {
  experimental.coroutines = Coroutines.ENABLE
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  compile(kotlin("stdlib", kotlinVersion))
  compile("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")
  compile("io.ktor:ktor-server-netty:$ktorVersion")
  compile("io.ktor:ktor-html-builder:$ktorVersion")
  compile("ch.qos.logback:logback-classic:$logbackVersion")
  compile("azadev.kotlin:aza-kotlin-css:$cssVersion")
}

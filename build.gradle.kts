allprojects {
  version = "0.1.0"

  repositories {
    jcenter()
    maven { setUrl("https://dl.bintray.com/kotlin/ktor") }
    maven { setUrl("https://dl.bintray.com/kotlin/kotlinx.html") }
  }
}

plugins {
  base
}

dependencies {
  subprojects.forEach {
    archives(it)
  }
}

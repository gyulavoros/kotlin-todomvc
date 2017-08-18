# Kotlin full stack TodoMVC
This project is an example implementation of the [TodoMVC](http://todomvc.com/) app written in Kotlin. More specifically, it's the Kotlin port of the [Vanilla JS](https://github.com/tastejs/todomvc/tree/gh-pages/examples/vanillajs) version of TodoMVC (hence it doesn't use any JavaScript frameworks like Angular or React). The code intentionally follows closely the structure and conventions of the original, so you should be able to compare the two versions easily.

> Note: I haven't implemented local storage yet. All items are stored in-memory for now.

# Modules
The code is a full stack Kotlin example, so it consists of two modules: `backend` and `frontend`.

## Backend
It is a very simple HTTP server serving requests on a single route (`localhost:9000/`). The response is an index page with minimal HTML DOM: `html`, `head`, empty `body` and a `script` tag referencing the compiled JavaScript app. It uses [ktor](https://github.com/Kotlin/ktor), a web framework developed by JetBrains.

## Frontend
All of the application logic is implemented in Kotlin and compiled to JavaScript with a tool called [kotlinc-js](https://kotlinlang.org/docs/tutorials/javascript/getting-started-command-line/command-line-library-js.html). The app uses Gradle as a build tool and relies on the [kotlin-frontend-plugin](https://github.com/Kotlin/kotlin-frontend-plugin). This plugin takes care of a lot of things, so you don't have to interact with `kotlinc-js` directly. Once the page loaded and the `docuemnt` is ready, all HTML elements are rendered by JavaScript. For creating the DOM it uses [kotlinx.html](https://github.com/Kotlin/kotlinx.html), a cross-platform library (works both on the JVM and in the browser) that provides a DSL for building/appending HTML elements in a type-safe manner.

# Gradle [kotlin-dsl](https://github.com/gradle/kotlin-dsl)
Being a full stack Kotlin example project, the build scripts are also written in Kotlin (`build.gradle.kts`). They are mostly in line with the ones from the reference [kotlin-fullstack-sample](https://github.com/Kotlin/kotlin-fullstack-sample).

# Requirements
~~The `kotlin-frontend-plugin` is picky when it comes to version numbers and it's not really documented.~~

~~Make sure that you use the latest LTS version of [Node.js](https://nodejs.org/en/download/releases/) (`v6.11.2` as of writing) and the matching npm release (`3.10.10`).~~

As of `kotlin-frontend-plugin` version `0.0.20` you can use the latest `node` and `npm` versions.

# How to run
First, you have to start the backend:

```
./gradlew backend:run
```

> Note: ktor supports [automatic reloading](http://ktor.io/application/hosting.html#use-automatic-reloading), which is configured in the project. This means that you don't have to restart the running application when you make changes, it's enough to recompile the files that you are editing. You have to run compile by hand though (e.g. from IntelliJ), Gradle doesn't pick up changes automatically for now.

Then you can run the frontend with:

```
./gradlew -t frontend:run
```

The `kotlin-frontend-plugin` will compile/assemble all resources into a module and start a `webpack` server running on: [http://localhost:8080/](http://localhost:8080/).

> As `kotlin-frontend-plugin` uses `webpack` under the hood [Hot Module Replacement](https://webpack.js.org/concepts/hot-module-replacement/) is available, so as you are editing the Kotlin code Gradle will automatically recompile changes and the plugin will replace modules in the background.
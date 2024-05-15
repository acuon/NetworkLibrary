# Simple Network Library

A lightweight and easy-to-use Kotlin library for making network calls in Android applications.

## Features

- Simple API for making GET, POST, PUT, and PATCH requests.
- Easily customizable for specific use cases.
- Option to set thread priority for network calls.
- Provides listeners for handling responses as JSONArray, JSONObject, and String.


## Installation

### Step 1: Add the following dependency to your app level `build.gradle` file:

#### Groovy DSL:
```groovy
 implementation 'com.acuon.networklibrary:simplenetworklibrary:1.0.0'
```
#### Kotlin DSL:
```kotlin
implementation("com.acuon.networklibrary:simplenetworklibrary:1.0.0")
```

### Step 2: Adding Maven Repository to `settings.gradle` file:
#### Groovy DSL:

To include the Maven repository in your `settings.gradle` file using Groovy DSL:

```groovy
dependencyResolutionManagement {
    repositories {
        maven {
            url "https://maven.pkg.github.com/acuon/NetworkLibrary"
            credentials {
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

#### Kotlin DSL:

To include the Maven repository in your `settings.gradle.kts` file using Kotlin DSL:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/acuon/NetworkLibrary")
            credentials {
                username = System.getenv("GITHUB_USER")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
```

Ensure that you have environment variables `GITHUB_USER` and `GITHUB_TOKEN` set with your GitHub username and personal access token respectively in you System's environment variable to authenticate the Maven repository access.

This addition will enable Gradle to resolve dependencies from the specified Maven repository.

## Usage

### Making a Network Request
```kotlin
Http.Request()
    .requestType(RequestType.GET)
    .baseUrl("https://your-base-url.com/)
    .endpoint("/endpoint")
    .contentType(ContentType.JSON)
    .query(mapOf("query" to "YOUR_QUERY"))
    .header(mapOf("auth" to "AUTH_KEY"))l
    .enableLog(BuildConfig.DEBUG)
    .setPriority(ThreadExecutor.DEFAULT)
    .execute(object: ResponseListener {
        override fun onFailure(e: Exception?) {
            
        }

        override fun onResponse(string: String?) {
            
        }
    })
    .execute(object: JSONArrayListener {
        override fun onFailure(e: Exception?) {
            
        }

        override fun onResponse(res: JSONArray?) {
            
        }
    })
    .execute(object: JSONObjectListener {
        override fun onFailure(e: Exception?) {
            
        }

        override fun onResponse(res: JSONObject?) {
            
        }
    })

```

In this implementation:

- `Http.Request()` initializes a new HTTP request.
- `.requestType()` specifies the type of request (GET, POST, PUT, PATCH, etc.), access the request types from RequestType enum class
- `.baseUrl()` takes a string param and sets the base URL for the request.
- `.endpoint()` takes a string and sets the endpoint for the request.
- `.contentType()` sets the content type of the request, accessible by ContentType enum class.
- `.query()` takes a hashmap for the queries and sets query parameters for the request.
- `.header()` takes a hashmap for the headers sets headers for the request.
- `.enableLog()` takes a boolean as param that enables logging for debugging purposes based on the build configuration.
- `.setPriority(ThreadExecutor.DEFAULT)` sets the priority of the thread for the request execution, different priorities are accessible by ThreadExecutor class (DEFAULT, MEDIUM, HIGH).
- `.execute(...)` executes the request with the specified response listeners for handling different types of responses (String, JSONObject, JSONArray), you can choose any one as per your usecase.

## Support

If you encounter any issues or have questions, please feel free to contact me at [rohitshar8600@gmail.com](mailto:rohitshar800@gmail.com), or you can create an issue on the project's GitHub repository for assistance.

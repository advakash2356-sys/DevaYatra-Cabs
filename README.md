# DevaYatra-Cabs

DevaYatra Cabs is an Android application designed for cab booking and pilgrimage travel management.

## Features

- **Dashboard:** Overview interface featuring KPIs, quick booking, emergency SOS, and WhatsApp assistance.
- **Booking Management:** Dedicated booking screens (`BookingsScreen`, `BookingDetailScreen`) to track and view cab reservations.
- **Driver Management:** Interface for managing and viewing driver information (`DriversScreen`).
- **Escalations:** Management screen for handling user escalations and issues (`EscalationsScreen`).
- **Heritage Section:** Dedicated section for heritage travel details (`HeritageSection.kt`).
- **Privacy Policy:** In-app privacy policy interface (`PrivacyPolicyScreen`).
- **Localization:** Multi-language support including English and Hindi (`Localization.kt`).
- **Local Persistence:** Structured database, DAO, and repository layers for local data storage (`AppDatabase`, `Daos`, `Models`, `Repository`).

## Tech Stack

- **Platform:** Android
- **Language:** Kotlin, Python (helper scripts)
- **UI Framework:** Jetpack Compose (Material Theme)
- **Build System:** Gradle (Kotlin DSL - `build.gradle.kts`, `app/build.gradle.kts`)
- **Testing:** JUnit, Robolectric (`ExampleRobolectricTest.kt`, `GreetingScreenshotTest.kt`, `ExampleUnitTest.kt`, `ExampleInstrumentedTest.kt`)

## Setup

Detailed build and installation instructions are not specified in the repository. Standard Android setup applies:
1. Open the project in Android Studio.
2. Ensure Android SDK dependencies are installed.
3. Configure environment settings based on `.env.example` if required.
4. Sync the project with Gradle files.

## Usage

Run the application on an Android device or emulator via Android Studio using the `app` run configuration.

## Project Structure

```
DevaYatra-Cabs/
├── .env.example
├── .gitignore
├── build.gradle.kts
├── assets/
│   └── .aistudio/
└── app/
    ├── build.gradle.kts
    ├── proguard-rules.pro
    └── src/
        ├── androidTest/
        │   └── java/com/example/ExampleInstrumentedTest.kt
        ├── test/
        │   └── java/com/example/
        │       ├── ExampleRobolectricTest.kt
        │       ├── ExampleUnitTest.kt
        │       └── GreetingScreenshotTest.kt
        └── main/
            ├── AndroidManifest.xml
            ├── java/com/example/
            │   ├── DevaYatraApplication.kt
            │   ├── MainActivity.kt
            │   ├── data/
            │   │   ├── AppDatabase.kt
            │   │   ├── Daos.kt
            │   │   ├── Models.kt
            │   │   └── Repository.kt
            │   └── ui/
            │       ├── BookingDetailScreen.kt
            │       ├── BookingsScreen.kt
            │       ├── DashboardScreen.kt
            │       ├── DevaYatraViewModel.kt
            │       ├── DriversScreen.kt
            │       ├── EmptyState.kt
            │       ├── EscalationsScreen.kt
            │       ├── HeritageSection.kt
            │       ├── Localization.kt
            │       ├── Navigation.kt
            │       ├── PrivacyPolicyScreen.kt
            │       ├── Utils.kt
            │       └── theme/
            │           ├── Color.kt
            │           ├── Theme.kt
            │           └── Type.kt
            └── res/
                ├── drawable/
                ├── mipmap-*/
                ├── values/
                └── xml/

# Payroll Management

Payroll Management is a small, offline-first Android application for creating payrolls and reviewing employee wages, taxes, and net pay. It is implemented with Kotlin and Jetpack Compose using MVI and Clean Architecture.

Payrolls are persisted locally with Room, while a `PayrollApi` abstraction and Hilt-provided mock implementation represent the remote integration point.

# Demo

https://github.com/user-attachments/assets/6f1f7796-a5c6-4ce9-b50b-f2715ab09dc2

https://github.com/user-attachments/assets/47bd6f96-b4ad-4035-aa63-2b0dcd0ba72c

## Features

- View all previously created payrolls, newest first.
- See each payroll's creation date, employee count, and total wages.
- Create a payroll containing one or more employees.
- Mark individual employees as tax exempt.
- Calculate wages, taxes, and net pay.
- Persist payrolls locally so they remain available across app restarts.
- Support system light/dark themes and Android dynamic color.

## Payroll Rules

For each employee:

```text
tax = 5% of wages when wages > $1,000 and the employee is not exempt
tax = $0 otherwise
net = wages - tax
```

The threshold is exclusive, so wages of exactly `$1,000.00` are not taxed. Percentage calculations are rounded to the nearest cent.

## Tech Stack

- Kotlin 2.2.10
- Jetpack Compose with Material 3
- MVI-style presentation contracts
- Clean Architecture
- Kotlin Coroutines, Flow, and StateFlow
- Room 2.8.4
- Hilt 2.59.2
- Navigation Compose 2.9.8
- KSP
- JUnit 4
- Gradle 9.4.1 and Android Gradle Plugin 9.2.1

## Requirements

- Android Studio with support for Android Gradle Plugin 9.2.1
- JDK 21 for Gradle
- Android SDK 36.1 installed
- An emulator or physical device running Android 10/API 29 or newer

The app has `minSdk 29`, `targetSdk 36`, and compiles against Android SDK release 36 with minor API level 1.

## Running the Project

### Android Studio

1. Open the repository root in Android Studio.
2. Allow Android Studio to use the Gradle wrapper and sync the project.
3. Install any requested Android SDK 36.1 components.
4. Select the `app` run configuration.
5. Select an API 29+ emulator or connected Android device.
6. Click **Run**.

```properties
sdk.dir=/absolute/path/to/Android/sdk
```

### Command Line

Build the debug APK:

```bash
./gradlew assembleDebug
```

The generated APK is located at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

With an emulator or device already available through `adb`, install it with:

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

## Tests

Run local unit tests:

```bash
./gradlew testDebugUnitTest
```

The focused unit tests currently cover:

- The 5% tax rule above the threshold.
- The exact `$1,000.00` boundary.
- Tax-exempt employees.
- Payroll aggregate totals.
- Payroll creation using the example employee data.
- Basic employee input validation.

## Architecture

The codebase uses a single application module with a feature-first `payroll` package. Within that feature, responsibilities are separated into presentation, domain, data, and dependency-injection layers.

```text
Compose UI
    |
    v
ViewModel (StateFlow + Actions + Effects)
    |
    v
Use Cases
    |
    v
PayrollRepository interface
    |
    v
PayrollRepositoryImpl
    |                    |
    v                    v
Room database       PayrollApi
(source of truth)   (mock upload)
```

### Offline-First Data Flow

Room is the single source of truth consumed by the UI. A payroll and its employees are inserted in one Room transaction, after which Room's `Flow` invalidation updates the list and detail screens automatically.

After the local transaction succeeds, the repository performs a best-effort upload through `PayrollApi`. The current `MockPayrollApi` always succeeds, but the interface allows a real remote implementation to be introduced without changing the domain or presentation layers. A remote failure does not roll back locally saved payroll data.

## Project Structure

```text
app/src/main/java/com/sumit/payrollmanagement/
|-- MainActivity.kt
|-- PayrollApplication.kt
|-- payroll/
|   |-- data/
|   |   |-- local/          # Room database, DAO, entities, relations
|   |   |-- mapper/         # Persistence/domain conversions
|   |   |-- remote/         # API contract and mock implementation
|   |   `-- repository/     # Repository implementation
|   |-- di/                 # Hilt modules and bindings
|   |-- domain/
|   |   |-- model/          # Payroll and employee domain models
|   |   |-- repository/     # Repository abstraction
|   |   `-- usecase/        # Validation, calculation, create, observe
|   `-- presentation/
|       |-- common/         # Currency and date formatting
|       |-- create/         # Create contract, ViewModel, and Compose UI
|       |-- detail/         # Detail contract, ViewModel, and Compose UI
|       |-- list/           # List contract, ViewModel, and Compose UI
|       `-- navigation/     # Navigation graph
`-- ui/theme/               # Material 3 theme and typography
```

## AI-Assisted Development

The developer created:

- [`AGENTS.md`](AGENTS.md) for project-specific development instructions.
- [`implementation-skill`](.agents/skills/implementation-skill/SKILL.md) for feature requirements and implementation guidance.

Feel free to check out these two files as these resources were used with OpenAI Codex to support AI-assisted development of the application.

## Architectural Decisions

### Room as the source of truth

All observable app data comes from Room rather than mixing local and remote state in the UI. This makes offline behavior predictable and allows a successful local write to appear immediately through reactive queries.

### Repository abstraction at the domain boundary

Use cases depend on `PayrollRepository`, not Room or the mock API. Persistence and remote coordination remain implementation details, and fake repositories can be used in domain tests.

### Use cases for business rules

Tax calculation, validation, parsing, and payroll creation are centralized in domain use cases. This prevents tax logic from being duplicated across composables or ViewModels and makes threshold behavior independently testable.

### Immutable state and explicit effects

State is modeled as immutable values and exposed as read-only `StateFlow`. Navigation is represented as a one-time effect instead of persistent state, avoiding accidental re-navigation during recomposition.

## What I Would Improve With More Time

1. WorkManager retries, connectivity constraints and visible sync state. The current implementation intentionally ignores upload failures after local persistence.
2. **Separate remote DTOs from domain models.** A production API should have request/response DTOs, mapping, error classification, authentication, rather than accepting `Payroll` directly.
3. **Add ViewModel and Compose tests.**
4. **Improve error modeling.** Replace exception-message-driven UI errors so each employee form can show precise errors next to the relevant input.
5. Introduce a currency value type, configurable currency/locale.
6. **Improve accessibility and resources.** **Move user-facing text into string resources.**
7. **Introduce lint/static analysis, formatting checks, CI builds.**
8. Currently, only the **implementation-skill** is created for AI-assisted development. Additional skills will be introduced over time, each focused on a distinct responsibility.

## Current Scope and Limitations

- The remote service is mocked and no network request is made.
- Payrolls can be created and viewed, but not edited or deleted.
- The app currently assumes US dollars and US currency formatting.
- There is no user account or authentication.

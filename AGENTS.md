## Tech Stack

- Kotlin
- Jetpack Compose
- MVI
- Clean Architecture
- Coroutines
- StateFlow
- Room
- Hilt
- Navigation Compose

## Project Rules

- UI layer must not access data sources directly.
- ViewModels expose immutable StateFlow.
- Repository pattern must be used.
- Use Hilt to inject dependency throughout the app.
- All business logic belongs in use cases.
- Follow offline-first approach.
- Mock network interactions.
- Use immutable UI state.
- Prefer single source of truth.
- Write readable and testable code.
- No backend provided. We'd like you to design a network abstraction layer and mock all
  data interactions.

## Architecture Flow

```text
UI ↓
ViewModel ↓ 
UseCase ↓ 
Repository Interface ↓
Repository Implementation ↓ 
API / Database
```

## Project Structure Example

```text
app/
└── src/main/java/com/example/app/
    ├── MainActivity.kt
    ├── MyApplication.kt
    └── login/
        ├── data/
        │   ├── local/
        │   │   └── LoginDao.kt
        │   ├── mapper/
        │   │   └── LoginMapper.kt
        │   ├── remote/
        │   │   ├── LoginApi.kt
        │   │   └── LoginResponseDto.kt
        │   └── repository/
        │       └── LoginRepositoryImpl.kt
        ├── di/
        │   └── LoginModule.kt
        ├── domain/
        │   ├── model/
        │   │   └── User.kt
        │   ├── repository/
        │   │   └── LoginRepository.kt
        │   └── usecase/
        │       └── LoginUseCase.kt
        └── presentation/
            ├── LoginContract.kt
            ├── LoginScreen.kt
            └── LoginViewModel.kt
```



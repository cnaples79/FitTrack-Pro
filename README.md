# FitTrack Pro

FitTrack Pro is a cross-platform fitness tracking application designed for both Android and iOS. It allows users to log workouts, set fitness goals, and analyze progress, all while maintaining a consistent experience across platforms. The app is powered by Kotlin Multiplatform Mobile (KMM), leveraging shared logic for business operations while providing platform-specific UI using Jetpack Compose for Android and SwiftUI for iOS.

## Features

### Frontend
- **Cross-Platform Support**:
  - Android UI built with Jetpack Compose
  - iOS UI built with SwiftUI
- **Workout Tracking**:
  - Log workouts with name, duration, and date
  - View past workouts
  - Track workout categories (strength, cardio, flexibility)
- **Goal Setting**:
  - Add and manage fitness goals
  - View progress and target dates
  - Set recurring goals
- **Analytics**:
  - Track total workouts and total duration
  - View progress charts and statistics
  - Export workout data

### Backend
- RESTful API with Ktor
- SQLDelight for database management
- JWT authentication
- Real-time sync with WebSocket support

## Tech Stack

### Frontend
- **Kotlin Multiplatform Mobile (KMM)**
  - Shared business logic and data models
  - Kotlinx.coroutines for async operations
  - Kotlinx.serialization for JSON handling
- **Android**
  - Jetpack Compose
  - Material Design 3
  - ViewModel and LiveData
- **iOS**
  - SwiftUI
  - Combine framework
  - Swift Package Manager

### Backend
- **Ktor** - Kotlin-based web framework
- **SQLDelight** - Type-safe database
- **Docker** - Containerization
- **Azure Kubernetes Service (AKS)** - Cloud deployment

## Project Structure

```
FitTrackPro/
├── shared/                # Shared KMM code
│   ├── src/
│   │   ├── commonMain/    
│   │   │   ├── domain/   # Business logic
│   │   │   ├── data/     # Data layer
│   │   │   ├── network/  # API client
│   │   ├── androidMain/
│   │   └── iosMain/
├── androidApp/           
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/
│   │   │   │   ├── ui/
│   │   │   │   ├── viewmodels/
│   ├── build.gradle.kts
├── iosApp/              
│   ├── FitTrackPro/
│   │   ├── Views/
│   │   ├── ViewModels/
│   ├── Podfile
└── backend/
    ├── src/
    │   ├── main/
    │   │   ├── kotlin/
    │   │   │   ├── routes/
    │   │   │   ├── models/
    ├── Dockerfile
```

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Xcode 13 or later
- JDK 11 or later
- Docker Desktop
- Kotlin Multiplatform Mobile plugin

### Development Setup

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/FitTrackPro.git
cd FitTrackPro
```

2. **Android Development**
- Open project in Android Studio
- Sync Gradle files
- Run the androidApp configuration

3. **iOS Development**
```bash
cd iosApp
pod install
```
- Open `iosApp.xcworkspace` in Xcode
- Build and run

4. **Backend Development**
```bash
cd backend
./gradlew run
```

### Building for Production

1. **Android**
```bash
./gradlew :androidApp:assembleRelease
```

2. **iOS**
- Open Xcode
- Product > Archive

3. **Backend**
```bash
docker build -t fittrackpro-backend .
docker push yourregistry/fittrackpro-backend
```

## API Documentation

### Authentication
- POST `/auth/register`
- POST `/auth/login`
- POST `/auth/refresh`

### Workouts
- GET `/api/workouts`
- POST `/api/workouts`
- GET `/api/workouts/{id}`
- PUT `/api/workouts/{id}`
- DELETE `/api/workouts/{id}`

### Goals
- GET `/api/goals`
- POST `/api/goals`
- PUT `/api/goals/{id}`
- DELETE `/api/goals/{id}`

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

Chase Naples - cnaples79@gmail.com

Project Link: [https://github.com/yourusername/FitTrackPro](https://github.com/yourusername/FitTrackPro)


Developed with ❤️ from Charlotte.
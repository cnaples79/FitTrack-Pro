import SwiftUI
import shared

@main
struct FitTrackProApp: App {
    init() {
        // Initialize Koin
        let databaseDriverFactory = DatabaseDriverFactory()
        KoinKt.doInitKoin(databaseDriverFactory: databaseDriverFactory) { _ in }
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(SharedWorkoutViewModel(repository: KoinHelper.workoutRepository))
                .environmentObject(SharedGoalViewModel(repository: KoinHelper.goalRepository))
                .environmentObject(SharedProfileViewModel(repository: KoinHelper.profileRepository))
        }
    }
}

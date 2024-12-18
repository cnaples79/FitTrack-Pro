import Foundation
import shared

@MainActor
class SharedWorkoutViewModel: ObservableObject {
    private let viewModel: WorkoutViewModel
    @Published var workouts: [Workout] = []
    @Published var isLoading = false
    @Published var error: String?
    
    init(repository: WorkoutRepository) {
        self.viewModel = WorkoutViewModel(repository: repository)
        startObserving()
    }
    
    private func startObserving() {
        viewModel.workouts.subscribe(scope: nil) { [weak self] workouts in
            self?.workouts = workouts
        }
        
        viewModel.isLoading.subscribe(scope: nil) { [weak self] isLoading in
            self?.isLoading = isLoading
        }
        
        viewModel.error.subscribe(scope: nil) { [weak self] error in
            self?.error = error
        }
    }
    
    func loadWorkouts() {
        viewModel.loadWorkouts()
    }
    
    func addWorkout(_ workout: Workout) {
        viewModel.addWorkout(workout: workout)
    }
    
    func updateWorkout(_ workout: Workout) {
        viewModel.updateWorkout(workout: workout)
    }
    
    func deleteWorkout(id: String) {
        viewModel.deleteWorkout(id: id)
    }
}

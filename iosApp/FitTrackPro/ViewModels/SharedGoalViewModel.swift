import Foundation
import shared

@MainActor
class SharedGoalViewModel: ObservableObject {
    private let viewModel: GoalViewModel
    @Published var goals: [Goal] = []
    @Published var activeGoals: [Goal] = []
    @Published var isLoading = false
    @Published var error: String?
    
    init(repository: GoalRepository) {
        self.viewModel = GoalViewModel(repository: repository)
        startObserving()
    }
    
    private func startObserving() {
        viewModel.goals.subscribe(scope: nil) { [weak self] goals in
            self?.goals = goals
        }
        
        viewModel.activeGoals.subscribe(scope: nil) { [weak self] activeGoals in
            self?.activeGoals = activeGoals
        }
        
        viewModel.isLoading.subscribe(scope: nil) { [weak self] isLoading in
            self?.isLoading = isLoading
        }
        
        viewModel.error.subscribe(scope: nil) { [weak self] error in
            self?.error = error
        }
    }
    
    func loadGoals() {
        viewModel.loadGoals()
    }
    
    func addGoal(_ goal: Goal) {
        viewModel.addGoal(goal: goal)
    }
    
    func updateGoalProgress(id: String, progress: Int32, completed: Bool) {
        viewModel.updateGoalProgress(id: id, progress: Int32(progress), completed: completed)
    }
    
    func deleteGoal(id: String) {
        viewModel.deleteGoal(id: id)
    }
}

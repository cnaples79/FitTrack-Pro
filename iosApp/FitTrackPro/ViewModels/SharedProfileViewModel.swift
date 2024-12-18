import Foundation
import shared

@MainActor
class SharedProfileViewModel: ObservableObject {
    private let viewModel: ProfileViewModel
    @Published var profile: UserProfile?
    @Published var isLoading = false
    @Published var error: String?
    
    init(repository: UserProfileRepository) {
        self.viewModel = ProfileViewModel(repository: repository)
        startObserving()
    }
    
    private func startObserving() {
        viewModel.profile.subscribe(scope: nil) { [weak self] profile in
            self?.profile = profile
        }
        
        viewModel.isLoading.subscribe(scope: nil) { [weak self] isLoading in
            self?.isLoading = isLoading
        }
        
        viewModel.error.subscribe(scope: nil) { [weak self] error in
            self?.error = error
        }
    }
    
    func loadProfile() {
        viewModel.loadProfile()
    }
    
    func updateProfile(_ profile: UserProfile) {
        viewModel.updateProfile(profile: profile)
    }
    
    func createProfile(_ profile: UserProfile) {
        viewModel.createProfile(profile: profile)
    }
}

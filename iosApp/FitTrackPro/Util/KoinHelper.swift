import Foundation
import shared

enum KoinHelper {
    static var workoutRepository: WorkoutRepository {
        KoinKt.koin.get(objCProtocol: WorkoutRepository.self)
    }
    
    static var goalRepository: GoalRepository {
        KoinKt.koin.get(objCProtocol: GoalRepository.self)
    }
    
    static var profileRepository: UserProfileRepository {
        KoinKt.koin.get(objCProtocol: UserProfileRepository.self)
    }
}

package com.fittrackpro.shared.di

import com.fittrackpro.shared.data.DatabaseDriverFactory
import com.fittrackpro.shared.data.Database
import com.fittrackpro.shared.domain.repository.WorkoutRepository
import com.fittrackpro.shared.domain.repository.GoalRepository
import com.fittrackpro.shared.domain.repository.UserProfileRepository
import com.fittrackpro.shared.presentation.WorkoutViewModel
import com.fittrackpro.shared.presentation.GoalViewModel
import com.fittrackpro.shared.presentation.ProfileViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    databaseDriverFactory: DatabaseDriverFactory,
    appDeclaration: KoinAppDeclaration = {}
) = startKoin {
    appDeclaration()
    modules(commonModule(databaseDriverFactory))
}

fun commonModule(databaseDriverFactory: DatabaseDriverFactory) = module {
    single { Database(databaseDriverFactory) }
    
    // Repositories
    single<WorkoutRepository> { get<Database>().workoutRepository }
    single<GoalRepository> { get<Database>().goalRepository }
    single<UserProfileRepository> { get<Database>().userProfileRepository }
    
    // ViewModels
    factory { WorkoutViewModel(get()) }
    factory { GoalViewModel(get()) }
    factory { ProfileViewModel(get()) }
}

package com.fittrackpro.shared.di

import com.fittrackpro.shared.data.Database
import com.fittrackpro.shared.data.DatabaseDriverFactory
import com.fittrackpro.shared.domain.repository.GoalRepository
import com.fittrackpro.shared.domain.repository.UserProfileRepository
import com.fittrackpro.shared.domain.repository.WorkoutRepository
import com.fittrackpro.shared.presentation.GoalViewModel
import com.fittrackpro.shared.presentation.ProfileViewModel
import com.fittrackpro.shared.presentation.WorkoutViewModel
import com.fittrackpro.shared.util.Dispatchers
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(
    databaseDriverFactory: DatabaseDriverFactory,
    dispatchers: Dispatchers,
    userId: Long,
    appDeclaration: KoinAppDeclaration = {}
) = startKoin {
    appDeclaration()
    modules(commonModule(databaseDriverFactory, dispatchers, userId))
}

fun commonModule(
    databaseDriverFactory: DatabaseDriverFactory, 
    dispatchers: Dispatchers,
    userId: Long
) = module {
    single { Database(databaseDriverFactory) }
    single { dispatchers }
    
    // Initialize database with userId
    single { 
        val database = get<Database>()
        database.initializeRepositories(userId)
        database
    }
    
    // Repositories
    single<UserProfileRepository> { get<Database>().userProfileRepository }
    single<WorkoutRepository> { get<Database>().workoutRepository }
    single<GoalRepository> { get<Database>().goalRepository }
    
    // ViewModels
    factory { ProfileViewModel(get(), get()) }
    factory { WorkoutViewModel(get(), get()) }
    factory { GoalViewModel(get(), get()) }
}

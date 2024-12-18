package com.fittrackpro.shared.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers as KotlinDispatchers

internal class AndroidDispatchers : Dispatchers {
    override val main: CoroutineDispatcher = KotlinDispatchers.Main
    override val io: CoroutineDispatcher = KotlinDispatchers.IO
    override val default: CoroutineDispatcher = KotlinDispatchers.Default
    override val unconfined: CoroutineDispatcher = KotlinDispatchers.Unconfined
}

package com.fittrackpro.shared.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

expect class CommonFlow<T>(flow: Flow<T>): Flow<T>

expect class CommonStateFlow<T>(flow: StateFlow<T>): StateFlow<T>

fun <T> Flow<T>.asCommonFlow(): CommonFlow<T> = CommonFlow(this)
fun <T> StateFlow<T>.asCommonStateFlow(): CommonStateFlow<T> = CommonStateFlow(this)

package com.fittrackpro.shared.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

actual class CommonFlow<T> actual constructor(
    flow: Flow<T>
) : Flow<T> by flow

actual class CommonStateFlow<T> actual constructor(
    flow: StateFlow<T>
) : StateFlow<T> by flow

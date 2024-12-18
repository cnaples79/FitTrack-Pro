package com.fittrackpro.shared.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

actual class CommonFlow<T> actual constructor(
    private val flow: Flow<T>
) : Flow<T> by flow {
    fun subscribe(
        coroutineScope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        onCollect: (T) -> Unit
    ): Job = coroutineScope.launch(dispatcher) {
        flow.onEach(onCollect).launchIn(this)
    }
}

actual class CommonStateFlow<T> actual constructor(
    private val stateFlow: StateFlow<T>
) : StateFlow<T> by stateFlow {
    fun subscribe(
        coroutineScope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        onCollect: (T) -> Unit
    ): Job = coroutineScope.launch(dispatcher) {
        stateFlow.onEach(onCollect).launchIn(this)
    }
}

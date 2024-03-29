package com.example.meet_up.tools

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun <T> Flow<T>.launchWhenStarted(lifecycleScope: LifecycleCoroutineScope) {
    lifecycleScope.launchWhenStarted {
        this@launchWhenStarted.collect()
    }
}

fun <T> Flow<T>.launch(owner: LifecycleOwner, state: Lifecycle.State) {
    owner.lifecycleScope.launch {
        owner.repeatOnLifecycle(state) {
            collect()
        }
    }
}

fun <T> Flow<T>.launchWhenCreated(owner: LifecycleOwner) = launch(owner, Lifecycle.State.CREATED)
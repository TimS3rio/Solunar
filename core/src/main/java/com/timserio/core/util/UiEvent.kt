package com.timserio.core.util

sealed class UiEvent {
    object Success : UiEvent()
    object NavigateUp : UiEvent()
}

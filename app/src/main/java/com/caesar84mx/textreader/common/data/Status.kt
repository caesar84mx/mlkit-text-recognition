package com.caesar84mx.textreader.common.data

sealed class Status {
    object Idle: Status()
    object Loading: Status()
    object Success: Status()
    data class Error(val message: String): Status()
}

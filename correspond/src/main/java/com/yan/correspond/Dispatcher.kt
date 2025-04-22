package com.yan.correspond

import java.util.concurrent.Executors

object Dispatcher {
    private val execute = Executors.newCachedThreadPool()

    fun onWork(runnable: Runnable) {
        execute.execute(runnable)
    }
}
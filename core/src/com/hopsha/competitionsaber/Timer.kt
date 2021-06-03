package com.hopsha.competitionsaber

import kotlin.math.max

class Timer(
    private val maxSeconds: Int
) {

    private var finishTimestamp: Long? = null
    private var pausedRemainingSeconds: Int? = null

    val remainingSeconds: Int
        get() = pausedRemainingSeconds
            ?: finishTimestamp?.let { finishTimestamp ->
                ((max(0, finishTimestamp - System.currentTimeMillis())) / 1000).toInt()
            }
            ?: 0

    fun start() {
        val pausedSeconds = pausedRemainingSeconds
        pausedRemainingSeconds = null
        finishTimestamp = if (pausedSeconds != null) {
            System.currentTimeMillis() + pausedSeconds * 1000
        } else {
            System.currentTimeMillis() + maxSeconds * 1000
        }
    }

    fun cancel() {
        pausedRemainingSeconds = null
        finishTimestamp = null
    }

    fun pause() {
        pausedRemainingSeconds = remainingSeconds
    }
}
package com.hopsha.competitionsaber.model

class ScoreRegistry {

    private val scoreMap = mutableMapOf<String, Int>()

    fun add(id: String, scores: Int) {
        val existing = get(id)
        scoreMap[id] = existing + scores
    }

    fun get(id: String): Int {
        return scoreMap[id] ?: 0
    }
}
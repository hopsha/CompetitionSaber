package com.hopsha.competitionsaber.model

import com.hopsha.competitionsaber.model.effect.Effect
import com.hopsha.competitionsaber.model.entity.Entity

class EffectRegistry {

    private val effects: MutableMap<Entity, MutableSet<EffectItem>> = mutableMapOf()

    fun add(entity: Entity, effect: Effect) {
        remove(entity, effect)
        val item = EffectItem(
            effect,
            System.currentTimeMillis()
        )
        add(entity, item)
    }

    private fun add(entity: Entity, item: EffectItem) {
        val set = effects[entity] ?: mutableSetOf()
        set.add(item)
        effects[entity] = set
    }

    fun remove(entity: Entity, effect: Effect) {
        effects[entity]?.removeIf { item ->
            item.effect == effect
        }
    }

    fun has(entity: Entity, effect: Effect): Boolean {
        return effects[entity]?.any { item ->
            item.effect == effect && item.endMillis > System.currentTimeMillis()
        } == true
    }

    private data class EffectItem(
        val effect: Effect,
        val startMillis: Long
    ) {
        val endMillis = startMillis + effect.durationMillis
    }
}
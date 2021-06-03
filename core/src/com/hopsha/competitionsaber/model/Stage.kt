package com.hopsha.competitionsaber.model

import com.hopsha.competitionsaber.model.controller.Vision
import com.hopsha.competitionsaber.model.entity.Entity

class Stage(
    val width: Float,
    val height: Float
) {

    var debugVision: Vision? = null
    val entities = mutableListOf<Entity>()

    inline fun <reified T : Entity> getEntity(id: String): T {
        val entity = entities.first { entity ->
            entity.id == id
        }
        return entity as T
    }

    fun addEntity(entity: Entity) {
        entities.add(entity)
    }

    fun addEntities(entities: List<Entity>) {
        this.entities.addAll(entities)
    }
}
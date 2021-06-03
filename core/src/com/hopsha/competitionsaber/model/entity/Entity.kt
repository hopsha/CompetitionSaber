package com.hopsha.competitionsaber.model.entity

import com.hopsha.competitionsaber.model.shape.Shape

interface Entity {

    val id: String
    val x: Float
    val y: Float

    val shape: Shape

    val isActual: Boolean
}
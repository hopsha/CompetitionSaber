package com.hopsha.competitionsaber.model.controller

data class PlayerState(
    val isAttacking: Boolean,
    val isInvulnerable: Boolean,
    val isDisarmed: Boolean,
    val isInPain: Boolean,
)
package com.hopsha.competitionsaber.desktop

import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.hopsha.competitionsaber.CompetitionSaber

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            title = "Saber"
            width = 1000
            height = 480
            samples = 3
        }
        LwjglApplication(CompetitionSaber(), config)
    }
}
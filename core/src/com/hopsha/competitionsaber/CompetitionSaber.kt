package com.hopsha.competitionsaber

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils
import com.hopsha.competitionsaber.model.*
import com.hopsha.competitionsaber.model.entity.player.Player
import kotlin.system.measureTimeMillis

class CompetitionSaber : ApplicationAdapter() {
    private var camera: OrthographicCamera? = null
    private var batch: SpriteBatch? = null
    private var shapeRenderer: ShapeRenderer? = null
    private var stage: Stage? = null
    private var effectRegistry: EffectRegistry? = null
    private var scoreRegistry: ScoreRegistry? = null
    private var engine: Engine? = null
    private var renderer: StageRenderer? = null
    private var statsRenderer: StatsRenderer? = null
    private var saberSound: Sound? = null
    private var kickSound: Sound? = null
    private var deathSound: Sound? = null
    private val timer: Timer = Timer(60)
    private var isRunning = false

    override fun create() {
        camera = OrthographicCamera().apply {
            setToOrtho(false, WIDTH.toFloat(), HEIGHT.toFloat())
        }

        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        stage = InitialStageFactory().create(GAME_FIELD_WIDTH.toFloat(), HEIGHT.toFloat())
        effectRegistry = EffectRegistry()
        scoreRegistry = ScoreRegistry()

        engine = Engine(
            stage!!,
            effectRegistry!!,
            scoreRegistry!!,
            onSaber = { saberSound?.play() },
            onAttack = { kickSound?.play() },
            onKill = { deathSound?.play() }
        )
        renderer = StageRenderer()

        statsRenderer = StatsRenderer(
            GAME_FIELD_WIDTH.toFloat(),
            0f,
            STATS_FIELD_WIDTH.toFloat(),
            HEIGHT.toFloat(),
            scoreRegistry!!,
            onChaos = { engine?.turnAlivePlayersRandomly() },
            onStart = {
                timer.start()
                isRunning = true
            },
            onStop = {
                timer.pause()
                isRunning = false
            }
        )
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT or GL20.GL_DEPTH_BUFFER_BIT or if (Gdx.graphics.bufferFormat.coverageSampling) GL20.GL_COVERAGE_BUFFER_BIT_NV else 0)

        saberSound = Gdx.audio.newSound(Gdx.files.internal("saber_sound.mp3"))
        kickSound = Gdx.audio.newSound(Gdx.files.internal("kick.mp3"))
        deathSound = Gdx.audio.newSound(Gdx.files.internal("death.mp3"))
    }

    override fun render() {
        if (timer.remainingSeconds == 0) {
            isRunning = false
        }
        if (stage?.entities
                ?.filterIsInstance<Player>()
                ?.filter { it.isAlive }
                ?.count() == 1
        ) {
            isRunning = false
        }

        ScreenUtils.clear(0f, 0f, 0.2f, 1f)
        camera!!.update()

        val input = Engine.Input(
            Gdx.input.isKeyPressed(Input.Keys.LEFT),
            Gdx.input.isKeyPressed(Input.Keys.RIGHT),
            Gdx.input.isKeyPressed(Input.Keys.UP),
            Gdx.input.isKeyPressed(Input.Keys.DOWN),
            Gdx.input.isKeyPressed(Input.Keys.SPACE)
        )

        if (isRunning) {
            engine!!.refresh(input)
        }
        batch!!.projectionMatrix = camera!!.combined
        shapeRenderer!!.projectionMatrix = camera!!.combined
        renderer!!.render(shapeRenderer!!, stage!!)

        val mouseVector = Vector3().apply {
            set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)
        }
        camera?.unproject(mouseVector)
        val mouseState = StatsRenderer.MouseState(
            mouseVector.x,
            mouseVector.y,
            Gdx.input.isTouched
        )
        statsRenderer!!.render(shapeRenderer!!, batch!!, mouseState, timer.remainingSeconds, stage!!)
    }

    override fun dispose() {
        timer.cancel()
        isRunning = false
        batch?.dispose()
        saberSound?.dispose()
        kickSound?.dispose()
        deathSound?.dispose()
    }

    companion object {
        private const val GAME_FIELD_WIDTH = 800
        private const val STATS_FIELD_WIDTH = 200
        private const val WIDTH = GAME_FIELD_WIDTH + STATS_FIELD_WIDTH
        private const val HEIGHT = 480
    }
}
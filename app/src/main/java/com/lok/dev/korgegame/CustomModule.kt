package com.lok.dev.korgegame

import com.lok.dev.korgegame.scene.SceneCatDog
import com.lok.dev.korgegame.scene.SceneDraw
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import kotlin.reflect.KClass

class CustomModule(
    private val width: Int = DEFAULT_WIDTH,
    private val height: Int = DEFAULT_HEIGHT,
    private val kClass: KClass<out Scene>,
    val callback: () -> Unit
) : Module() {

    companion object {
        const val DEFAULT_WIDTH = 2000
        const val DEFAULT_HEIGHT = 2000
    }

    override val size: SizeInt
        get() = SizeInt.invoke(width, height)

    override val windowSize: SizeInt
        get() = SizeInt.invoke(width, height)

    override val mainScene: KClass<out Scene> = kClass

    override suspend fun AsyncInjector.configure() {
        when (kClass) {
            SceneDraw::class -> {
                mapPrototype { SceneDraw() }
            }
            SceneCatDog::class -> {
                mapPrototype { SceneCatDog() }
            }
            else -> {
                mapPrototype { SceneDraw() }
            }
        }
    }


}
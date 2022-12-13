package com.lok.dev.korgegame

import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Stage
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import kotlin.reflect.KClass

class CustomModule(
    private val width : Int = DEFAULT_WIDTH,
    private val height : Int = DEFAULT_HEIGHT,
    val callback : () -> Unit
) : Module() {

    companion object {
        const val DEFAULT_WIDTH = 1920
        const val DEFAULT_HEIGHT = 1080
    }

    override val size : SizeInt
        get() = SizeInt.invoke(width, height)

    override val windowSize : SizeInt
        get() = SizeInt.invoke(width, height)

    override val mainScene : KClass<out Scene> = SceneLifeCycle::class

    override suspend fun AsyncInjector.configure() {
        mapPrototype { SceneLifeCycle() }
    }


}
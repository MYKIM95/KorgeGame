package com.lok.dev.korgegame

import android.util.Log
import com.soywiz.klock.seconds
import com.soywiz.korge.Korge
import com.soywiz.korge.particle.particleEmitter
import com.soywiz.korge.particle.readParticleEmitter
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.sceneContainer
import com.soywiz.korge.time.delay
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.position
import com.soywiz.korim.color.Colors
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Point

suspend fun main() = Korge(width = 1080, height = 1980, bgcolor = Colors["#2b2b2b"]) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo({ MyScene() })
}

class MyScene : Scene() {

    override suspend fun SContainer.sceneInit() {
        Log.d("123123123", "MyScene() Init")
    }

    override suspend fun SContainer.sceneMain() {
        Log.d("123123123", "MyScene() Main")
        val emitter = resourcesVfs["game-res/fire.pex"].readParticleEmitter()

        emitter.gravity = Point(20.0, 20.0)
        //val emitter = resourcesVfs["particle/particle.pex"].readParticleEmitter()
        //val emitter = resourcesVfs["particle/1/particle.pex"].readParticleEmitter()
        val particlesView = particleEmitter(emitter, time = 100.seconds).position(views.virtualWidth * 0.5, -views.virtualHeight * 0.5)

        delay(10.seconds)

        println("RESTART")
        particlesView.restart()
    }

    override suspend fun sceneAfterInit() {
        Log.d("123123123", "MyScene() AfterInit")
    }

    override suspend fun sceneBeforeLeaving() {
        Log.d("123123123", "MyScene() BeforeLeaving")
    }

    override suspend fun sceneDestroy() {
        Log.d("123123123", "MyScene() Destroy")
    }

    override suspend fun sceneAfterDestroy() {
        Log.d("123123123", "MyScene() AfterDestroy")
    }


//    override suspend fun SContainer.sceneMain() {
//        val minDegrees = (-16).degrees
//        val maxDegrees = (+16).degrees
//
//        val image = image(resourcesVfs["game-res/smileyOutline.png"].readBitmap()){
//            rotation = maxDegrees
//            anchor(.5,.5)
//            scale(0.8)
//            position(256,256)
//        }
//
//        while (true) {
//            image.tween(image::rotation[minDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
//            image.tween(image::rotation[maxDegrees], time = 1.seconds, easing = Easing.EASE_IN_OUT)
//        }
//
//    }
}
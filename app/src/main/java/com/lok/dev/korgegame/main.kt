package com.lok.dev.korgegame

import android.util.Log
import com.soywiz.klock.seconds
import com.soywiz.korge.Korge
import com.soywiz.korge.animate.animate
import com.soywiz.korge.input.onClick
import com.soywiz.korge.particle.particleEmitter
import com.soywiz.korge.particle.readParticleEmitter
import com.soywiz.korge.resources.resourceBitmap
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.sceneContainer
import com.soywiz.korge.time.delay
import com.soywiz.korge.tween.get
import com.soywiz.korge.tween.tween
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.resources.ResourcesContainer
import com.soywiz.korma.geom.Angle
import com.soywiz.korma.geom.degrees
import com.soywiz.korma.interpolation.Easing

suspend fun main() = Korge(width = 512, height = 512, bgcolor = Colors["#2b2b2b"]) {
    val sceneContainer = sceneContainer()

    sceneContainer.changeTo({ MyScene() })
}

class MyScene : Scene() {

    override suspend fun SContainer.sceneInit() {
        Log.d("123123123", "MyScene() Init")
    }

    override suspend fun SContainer.sceneMain() {
        Log.d("123123123", "MyScene() Main")
        val emitter = resourcesVfs["game-res/carrot.pex"].readParticleEmitter()
        //val emitter = resourcesVfs["particle/particle.pex"].readParticleEmitter()
        //val emitter = resourcesVfs["particle/1/particle.pex"].readParticleEmitter()
        val particlesView = particleEmitter(emitter, time = 4.seconds).position(views.virtualWidth * 0.5, views.virtualHeight * 0.5)

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
package com.lok.dev.korgegame

import com.soywiz.klock.seconds
import com.soywiz.korge.Korge
import com.soywiz.korge.particle.particleEmitter
import com.soywiz.korge.particle.readParticleEmitter
import com.soywiz.korge.time.delay
import com.soywiz.korge.view.position
import com.soywiz.korio.file.std.resourcesVfs

suspend fun particlesMain() = Korge(width = 600, height = 600) {
    val emitter = resourcesVfs["game-res/demo2.pex"].readParticleEmitter()
    //val emitter = resourcesVfs["particle/particle.pex"].readParticleEmitter()
    //val emitter = resourcesVfs["particle/1/particle.pex"].readParticleEmitter()
    val particlesView = particleEmitter(emitter, time = 2.seconds).position(views.virtualWidth * 0.5, views.virtualHeight * 0.5)

    delay(4.seconds)

    println("RESTART")
    particlesView.restart()
}
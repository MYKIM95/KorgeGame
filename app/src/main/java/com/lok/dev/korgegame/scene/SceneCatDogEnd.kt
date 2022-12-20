package com.lok.dev.korgegame.scene

import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.text
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors

class SceneCatDogEnd(val score : Int) : Scene() {
    @OptIn(KorgeInternal::class)
    override suspend fun SContainer.sceneMain() {

        text("Your Score is $score", 150.0, Colors.WHITE).xy(
            views.nativeWidth / 2,
            views.nativeHeight / 2
        )

    }
}

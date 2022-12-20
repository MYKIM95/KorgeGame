package com.lok.dev.korgegame.scene

import com.soywiz.korge.input.onClick
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.SContainer
import com.soywiz.korge.view.text
import com.soywiz.korge.view.xy
import com.soywiz.korim.color.Colors

class SceneCatDogStart() : Scene() {

    @OptIn(KorgeInternal::class)
    override suspend fun SContainer.sceneMain() {

        text("Touch to Start", 150.0, Colors.WHITE).xy(
            views.nativeWidth / 2,
            views.nativeHeight / 2
        )

        onClick {
            sceneContainer.changeTo({ SceneCatDog() })
        }

    }

}

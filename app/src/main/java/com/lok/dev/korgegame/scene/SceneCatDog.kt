package com.lok.dev.korgegame.scene

import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.korge.Korge
import com.soywiz.korge.animate.animate
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.delay
import com.soywiz.korge.time.timeout
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.ui.uiProgressBar
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.format.readBitmapSlice
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchAsap
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import kotlinx.coroutines.cancel
import kotlinx.coroutines.ensureActive
import java.util.*

class SceneCatDog : Scene() {

    private lateinit var random: Random
    var score = 0
    var health = 30.0

    @OptIn(KorgeInternal::class)
    override suspend fun SContainer.sceneMain() {
        random = Random()
        var totalTargets = 0.0
        val targetList = arrayListOf<Image>()
        val catBitmap = resourcesVfs["game-res/catDog/cat.png"].readBitmap()
        val dogBitmap = resourcesVfs["game-res/catDog/dog.png"].readBitmap()
        val smallCatBitmap = resourcesVfs["game-res/catDog/catSmall.png"].readBitmap()
        val smallDogBitmap = resourcesVfs["game-res/catDog/dogSmall.png"].readBitmap()
        val leftArrowBmpSlice = resourcesVfs["game-res/catDog/leftArrow.png"].readBitmapSlice()
        val rightArrowBmpSlice = resourcesVfs["game-res/catDog/rightArrow.png"].readBitmapSlice()

        val catImage = Image(catBitmap).scale(2.0, 2.0)
        val dogImage = Image(dogBitmap).scale(2.0, 2.0)

        val targets = listOf(catImage, dogImage)

        addFixedUpdater(time = 1.seconds) {
            if(health <= 0) {
                launch {
                    sceneContainer.changeTo({ SceneCatDogEnd(score) })
                }
            }
        }

        Korge(
            virtualWidth = views.nativeWidth,
            virtualHeight = views.nativeHeight
        ) {

            val width = views.nativeWidth.toDouble()
            val height = views.nativeHeight.toDouble()

            text("Time", textSize = 80.0, color = Colors.WHITE).xy(0.0, 10.0)
            val healthBar = uiProgressBar {
                position(width / 10 * 2, 10.0)
                current = health
                maximum = 30.0
                scaledWidth = width / 10 * 7
                scaledHeight = 100.0
                colorMul = Colors.GREEN
            }
            healthBar.addUpdater {
                current = health
            }

            addFixedUpdater(time = 1.seconds) {
                health -= 1
            }

            uiButton(
                label = "",
                icon = leftArrowBmpSlice,
                width = width / 4,
                height = width / 4
            ).xy(.0, height / 10 * 8).onPress {
                matchTarget(this@Korge, targetList, width, Input.LEFT)
                makeTarget(targets).apply {
                    xy(width / 2 - catImage.width, height / 10 * 7 - (9 * (catImage.width)))
                    totalTargets += 1
                    zIndex = -totalTargets
                    targetList.add(this)
                    addTo(this@Korge)
                }
            }
            uiButton(
                label = "",
                icon = rightArrowBmpSlice,
                width = width / 4,
                height = width / 4
            ).xy(width - rightArrowBmpSlice.width * 2, height / 10 * 8).onPress {
                matchTarget(this@Korge, targetList, width, Input.RIGHT)
                makeTarget(targets).apply {
                    xy(width / 2 - catImage.width, height / 10 * 7 - (9 * (catImage.width)))
                    totalTargets += 1
                    zIndex = -totalTargets
                    targetList.add(this)
                    addTo(this@Korge)
                }
            }

            image(smallCatBitmap).scale(2.0, 2.0).xy(.0, height / 10 * 7)
            image(smallDogBitmap).scale(2.0, 2.0)
                .xy(width - smallDogBitmap.width * 2, height / 10 * 7)

            text("score : 0", textSize = 50.0, color = Colors.WHITE).xy(.0, height / 10)
                .addUpdater {
                    this.text = "score : $score"
                }

            for (i in 1..9) {
                makeTarget(targets).apply {
                    targetList.add(this)
                    xy(width / 2 - catImage.width, height / 10 * 7 - (i * (catImage.width)))
                    totalTargets += 1
                    zIndex = -totalTargets
                    addTo(this@Korge)
                }
            }

        }

    }

    private fun makeTarget(targets : List<Image>) : Image {
        val num = random.nextInt(2)
        val targetImage = targets[num].clone()
        targetImage.name(if (num == 0) "cat" else "dog")
        return targetImage as Image
    }

    private fun matchTarget(view: View, targetList: ArrayList<Image>, width: Double, input: Input) {
        val target = targetList.first()
        targetList.removeAt(0)

        launch {
            view.animate {
                parallel {
                    when {
                        input is Input.LEFT && target.name == "cat" -> {
                            target.moveTo(-target.width * 2, target.y, time = 200.milliseconds)
                            score += 1
                        }
                        input is Input.RIGHT && target.name == "dog" -> {
                            target.moveTo(width, target.y, time = 200.milliseconds)
                            score += 1
                        }
                        else -> {
                            target.alpha(0, time = 100.milliseconds)
                            score -= 1
                            health -= 1
                        }
                    }

                    targetList.forEach { image ->
                        image.moveTo(image.x, image.y + image.height, time = 100.milliseconds)
                    }
                }
            }
        }
    }

}

sealed class Input {
    object LEFT : Input()
    object RIGHT : Input()
}

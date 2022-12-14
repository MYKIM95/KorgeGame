package com.lok.dev.korgegame

import android.util.Log
import com.soywiz.klock.seconds
import com.soywiz.korge.Korge
import com.soywiz.korge.input.SingleTouchHandler
import com.soywiz.korge.input.singleTouch
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.delay
import com.soywiz.korge.ui.korui.koruiComponent
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.format.readBitmapSlice
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Point
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class SceneLifeCycle : Scene() {

    override fun createSceneView(width: Double, height: Double): SContainer {

        Log.d("123123123", "CreateSceneView")
        Log.d("123123123", "width : $width")
        Log.d("123123123", "height : $height")

        return super.createSceneView(width, height)
    }

    override suspend fun SContainer.sceneInit() {
        Log.d("123123123", "SceneInit")
    }

    override suspend fun SContainer.sceneMain() {
        Log.d("123123123", "SceneMain")

//        delay(2.seconds)

//        Log.d("123123123", "Change to MyScene()")
//        sceneContainer.changeTo({ MyScene() })

        var imageCnt = 0
        var prevPoint = Point(.0, .0)
        val imagePoint = arrayListOf<Point>()

        val imageMap = HashMap<Int, Point>()
        val backPoint = arrayListOf(0)
        var saveImage : List<View> = listOf()

        val uiMargin = 150

        var job : Job? = null

        // TODO 전체화면 모드 찾아보기

        Korge(width = 1200, height = 1980) {

            val bitmap = resourcesVfs["game-res/pika.png"].readBitmap()
            val image = Image(bitmap).scale(0.8)

            // 총 몇개인지 보여줌
            val text = text("Hello PIKA!", textSize = 100.0, color = Colors.WHITE)
            text.addUpdater {
                this.text = "Total : $imageCnt"
            }

            // TODO 구역 나누기 찾아보기, 타기종 비율 맞춰서 해보기
            // 지우기
            val removeBmpSlice = resourcesVfs["game-res/eraser.png"].readBitmapSlice()
            val removeButton = uiButton(label = "remove", icon = removeBmpSlice, width = 120.0, height = 120.0).xy(500.0, 0.0)
            removeButton.onPress {
                if(it.localY < uiMargin) {
                    this.removeChildrenIf(cond = { index, child ->
                        child is Image
                    })
                    imageCnt = 0
                    job?.takeIf { it.isActive }?.cancel()
                }
            }

            // 되돌리기
            val backBmpSlice = resourcesVfs["game-res/back.png"].readBitmapSlice()
            val backButton = uiButton(label = "remove", icon = backBmpSlice, width = 120.0, height = 120.0).xy(700.0, 0.0)
            backButton.onPress {
                if(it.localY < uiMargin && backPoint.size > 0) {
                    val backNum = backPoint.last()
                    this.removeChildrenIf(cond = { index, child ->
                        (child is Image) && index - 3 > backNum
                    })
                    imageCnt = backNum
                    backPoint.removeLast()
                }
            }

            // 저장하기
            val saveBmpSlice = resourcesVfs["game-res/save.png"].readBitmapSlice()
            val saveButton = uiButton(label = "remove", icon = saveBmpSlice, width = 120.0, height = 120.0).xy(900.0, 0.0)
            saveButton.onPress {
                if(it.localY < uiMargin) {
                    saveImage = this.children.slice(2..children.lastIndex)
                    this.removeChildrenIf(cond = { index, child ->
                        child is Image
                    })
                    imageCnt = 0
                }
            }

            // 재생하기
            val playBmpSlice = resourcesVfs["game-res/play.png"].readBitmapSlice()
            val playButton = uiButton(label = "remove", icon = playBmpSlice, width = 120.0, height = 120.0).xy(1100.0, 0.0)
            playButton.onPress {
                if(it.localY < uiMargin) {
                    job = launch {
                        saveImage.forEach { view ->
                            view.addTo(this@Korge)
                            imageCnt += 1
                            delay(0.05.seconds)
                        }
                    }
                }
            }


            // 터치 이벤트가 있을때만 실행
            this.singleTouch(removeTouch = true) {

                start {
                    if(it.localY > uiMargin) {
                        prevPoint = it.startLocal
                        backPoint.add(imageCnt)
                        Log.d("123123123", "touchStart")
                    }
                }

                addUpdater {
                    move {
                        if(it.localY > uiMargin) {
                            val point = Point(mouseX, mouseY)
                            if(imageCnt == 0) {
                                prevPoint = point
                                imagePoint.add(point)
                                image.clone().xy(point).addTo(this@Korge)
                                imageCnt += 1
                            }else {
                                if(prevPoint.distanceTo(point) > image.width) {
                                    imagePoint.add(point)
                                    val angle = atan2((point.y - prevPoint.y) , (point.x - prevPoint.x))
                                    val newX = prevPoint.x + ((image.width * 0.8) * cos(angle))
                                    val newY = prevPoint.y + ((image.width * 0.8) * sin(angle))
                                    image.clone().xy(newX, newY).addTo(this@Korge)
                                    imageCnt += 1
                                    prevPoint = Point(newX, newY)
                                }
                            }
                        }
                    }
                }

                tap {
                    if(it.localY > uiMargin) {
                        val point = Point(mouseX, mouseY)
                        prevPoint = point
                        imagePoint.add(point)
                        image.clone().xy(point).addTo(this@Korge)
                        imageCnt += 1
                    }
                }

                end {
                    if(it.localY > uiMargin) {
                        Log.d("123123123", "touchEnd")
                    }
                }
            }

        }

    }

    override suspend fun sceneAfterInit() {
        Log.d("123123123", "SceneAfterInit")
    }

    override suspend fun sceneBeforeLeaving() {
        Log.d("123123123", "SceneBeforeLeaving")
    }

    override suspend fun sceneDestroy() {
        Log.d("123123123", "SceneDestroy")
    }

    override suspend fun sceneAfterDestroy() {
        Log.d("123123123", "SceneAfterDestroy")
    }


}
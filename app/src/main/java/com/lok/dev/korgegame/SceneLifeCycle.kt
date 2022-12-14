package com.lok.dev.korgegame

import android.util.Log
import com.soywiz.klock.seconds
import com.soywiz.korge.Korge
import com.soywiz.korge.input.singleTouch
import com.soywiz.korge.input.touch
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.delay
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Point
import java.util.*
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

    var imageCnt = 0
    val imagePoint = arrayListOf<Point>()
    val drawQueue = LinkedList<View>()
    val queue = LinkedList<Point>()
    var prevPoint = Point(.0, .0)

    override suspend fun SContainer.sceneMain() {
        Log.d("123123123", "SceneMain")

//        delay(2.seconds)

        Log.d("123123123", "Change to MyScene()")
        sceneContainer.changeTo({ MyScene() })
//
//        Korge(width = 1080, height = 1980) {
//
//            val bitmap = resourcesVfs["game-res/pika.png"].readBitmap()
//            val image = Image(bitmap).scale(0.6)
//
//            val text = text("Hello PIKA!", textSize = 100.0, color = Colors.WHITE)
//            text.addUpdater {
//                this.text = imageCnt.toString()
//            }
//
//            // TODO 찍었다가 떼었을때도 그려기제 하기, 삭제 기능 구현
//
//            addUpdater {
//
//                // 터치 이벤트가 있을때만 실행
//                singleTouch {
//                    start {
//                        prevPoint = it.startLocal
//                    }
//
//                    move {
//                        val point = Point(mouseX, mouseY)
//                        if (imageCnt == 0) {
//                            prevPoint = point
//                            imagePoint.add(point)
//                            image.clone().xy(point).addTo(this@Korge)
//                            imageCnt += 1
//                        } else {
//                            if(prevPoint.distanceTo(point) > image.width / 2) {
//                                imagePoint.add(point)
//                                val angle = atan2((point.y - prevPoint.y) , (point.x - prevPoint.x))
//                                val newX = prevPoint.x + ((image.width / 2) * cos(angle))
//                                val newY = prevPoint.y + ((image.width / 2) * sin(angle))
//                                image.clone().xy(newX, newY).addTo(this@Korge)
//                                imageCnt += 1
//                                prevPoint = Point(newX, newY)
//                            }
//                        }
//
//                    }
//
//                }
//
//            }
//
//        }

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
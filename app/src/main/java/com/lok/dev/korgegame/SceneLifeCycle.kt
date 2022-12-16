package com.lok.dev.korgegame

import android.util.Log
import com.soywiz.klock.seconds
import com.soywiz.korge.Korge
import com.soywiz.korge.animate.*
import com.soywiz.korge.input.SingleTouchHandler
import com.soywiz.korge.input.mouse
import com.soywiz.korge.input.singleTouch
import com.soywiz.korge.internal.KorgeInternal
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.scene.delay
import com.soywiz.korge.ui.korui.koruiComponent
import com.soywiz.korge.ui.uiButton
import com.soywiz.korge.view.*
import com.soywiz.korge.view.filter.filter
import com.soywiz.korge.view.filter.removeFilter
import com.soywiz.korim.color.Colors
import com.soywiz.korim.format.readBitmap
import com.soywiz.korim.format.readBitmapSlice
import com.soywiz.korio.async.launchImmediately
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

    @OptIn(KorgeInternal::class)
    override suspend fun SContainer.sceneMain() {
        Log.d("123123123", "SceneMain")

        var imageCnt = 0
        var prevPoint = Point(.0, .0)
        val imagePoint = arrayListOf<Point>()

        val backPoint = arrayListOf(0)
        var saveImage: List<Image> = listOf()

        var job: Job? = null

        Korge(
            virtualWidth = views.nativeWidth,
            virtualHeight = views.nativeHeight
        ) {

            val width = views.nativeWidth.toDouble()
            val height = views.nativeHeight.toDouble()
            val uiMargin = width / 10


            val bitmap = resourcesVfs["game-res/pika.png"].readBitmap()
            val image = Image(bitmap).apply {
                scaledWidth = width / 15
                scaledHeight = width / 15
            }

            // 총 몇개인지 보여줌
            val text = text("Hello PIKA!", textSize = 60.0, color = Colors.WHITE).xy(.0, .0)
            text.addUpdater {
                this.text = "Total : $imageCnt"
            }

            // 지우기
            val removeBmpSlice = resourcesVfs["game-res/eraser.png"].readBitmapSlice()
            val removeButton = uiButton(
                label = "remove",
                icon = removeBmpSlice,
                width = width / 10,
                height = width / 10
            ).xy(width / 10 * 3, 0.0)
            removeButton.onPress {
                if (it.localY < uiMargin) {
                    this.removeChildrenIf(cond = { index, child ->
                        child is Image
                    })
                    imageCnt = 0
                    job?.takeIf { it.isActive }?.cancel()
                }
            }

            // 되돌리기
            val backBmpSlice = resourcesVfs["game-res/back.png"].readBitmapSlice()
            val backButton = uiButton(
                label = "remove",
                icon = backBmpSlice,
                width = width / 10,
                height = width / 10
            ).xy(width / 10 * 4, 0.0)
            backButton.onPress {
                if (it.localY < uiMargin && backPoint.size > 0) {

                    imageCnt -= children.count { child ->
                        (child is Image) && child.index >= backPoint.last()
                    }

                    this.removeChildrenIf(cond = { index, child ->
                        (child is Image) && index >= backPoint.last()
                    })

                    backPoint.removeLast()
                }
                job?.takeIf { it.isActive }?.cancel()
            }

            // 저장하기
            val saveBmpSlice = resourcesVfs["game-res/save.png"].readBitmapSlice()
            val saveButton = uiButton(
                label = "remove",
                icon = saveBmpSlice,
                width = width / 10,
                height = width / 10
            ).xy(width / 10 * 5, 0.0)
            saveButton.onPress {
                if (it.localY < uiMargin) {
                    saveImage = this.children.filterIsInstance<Image>()
                    this.removeChildrenIf(cond = { index, child ->
                        child is Image
                    })
                    imageCnt = 0
                }
            }

            // 재생하기
            val playBmpSlice = resourcesVfs["game-res/play.png"].readBitmapSlice()
            val playButton = uiButton(
                label = "remove",
                icon = playBmpSlice,
                width = width / 10,
                height = width / 10
            ).xy(width / 10 * 6, 0.0)
            playButton.onPress {
                if (it.localY < uiMargin) {
                    this.removeChildrenIf(cond = { index, child ->
                        child is Image
                    })
                    imageCnt = 0
                    job?.takeIf { it.isActive }?.cancel()

                    launch {
                        saveImage.forEach { imageSaved ->

                            imageSaved.addTo(this@Korge).animate(completeOnCancel = false) {
                                parallel {
                                    imageSaved.apply {
                                        scale(sx = 0.1, sy = 0.1)
                                        scaleTo(0.6, 0.6, 0.1.seconds)
                                        scaleTo(0.5, 0.5, 0.1.seconds)
                                    }
                                }
                            }

                            imageCnt += 1
                            delay(0.05.seconds)
                        }

                    }

                    backPoint.add(0)
                }
            }

            // 하트 만들기
            val heartBmpSlice = resourcesVfs["game-res/heart.png"].readBitmapSlice()
            val heartButton = uiButton(
                label = "remove",
                icon = heartBmpSlice,
                width = width / 10,
                height = width / 10
            ).xy(width / 10 * 7, 0.0)
            heartButton.onPress {
                if (it.localY < uiMargin) {
                    this.removeChildrenIf(cond = { index, child ->
                        child is Image
                    })
                    imageCnt = 0
                    job?.takeIf { it.isActive }?.cancel()

                    job = launch {
                        for (i in heartPoints.indices step 2) {
                            val imageClone = image.clone()
                            imageClone.xy(heartPoints[i] * width, heartPoints[i + 1] * height)
                                .addTo(this@Korge).animate(completeOnCancel = false) {
                                    parallel {
                                        imageClone.apply {
                                            scale(sx = 0.1, sy = 0.1)
                                            scaleTo(0.65, 0.65, 0.1.seconds)
                                            scaleTo(0.55, 0.55, 0.1.seconds)
                                        }
                                    }
                                }
                            imageCnt += 1
                            delay(0.05.seconds)
                        }
                    }
                    backPoint.add(0)
                }
            }


            // 터치 이벤트가 있을때만 실행
            this.singleTouch(removeTouch = true) {

                start {
                    if (it.localY > uiMargin && it.localY < height * 0.4) {
                        prevPoint = it.startLocal
                        backPoint.add(children.size)
                    }
                }

                addUpdater {
                    move {
                        if (it.localY > uiMargin && it.localY < height * 0.4) {
                            val point = Point(mouseX, mouseY)
                            if (imageCnt == 0) {
                                prevPoint = point
                                imagePoint.add(point)
                                image.clone().xy(point).addTo(this@Korge)
                                imageCnt += 1
                            } else {
                                if (prevPoint.distanceTo(point) > image.width) {
                                    imagePoint.add(point)
                                    val angle =
                                        atan2((point.y - prevPoint.y), (point.x - prevPoint.x))
                                    val newX = prevPoint.x + ((image.width * 0.6) * cos(angle))
                                    val newY = prevPoint.y + ((image.width * 0.6) * sin(angle))
                                    image.clone().xy(newX, newY).addTo(this@Korge)
                                    imageCnt += 1
                                    prevPoint = Point(newX, newY)
                                }
                            }
                        }
                    }
                }

                tap {
                    if (it.localY > uiMargin && it.localY < height * 0.4) {
                        val point = Point(mouseX, mouseY)
                        prevPoint = point
                        imagePoint.add(point)
                        image.clone().xy(point).addTo(this@Korge)
                        imageCnt += 1
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

    private val heartPoints = listOf(
        0.47265625, 0.15366093983907186,
        0.40575451929748685, 0.13822137624799768,
        0.33320323797207546, 0.13512942262492658,
        0.2654982850040635, 0.14951616413768087,
        0.22494091337078498, 0.18208287169374227,
        0.21747120947670176, 0.22109729413451235,
        0.24452596647308028, 0.2575080374424665,
        0.2831590575093286, 0.29074730039110797,
        0.33247641772338327, 0.319590477197795,
        0.39042120849754786, 0.3433214549802684,
        0.4574970406182146, 0.3585400449344218,
        0.512451171875, 0.3689432658121258,
        0.5522653825736474, 0.14375873413498536,
        0.6190954235621186, 0.12822925892929035,
        0.6899054859503377, 0.13728796171751043,
        0.7190756572347666, 0.17322120435400423,
        0.7242451005391051, 0.21234369318563606,
        0.708319677882258, 0.2506147022534479,
        0.6809748525960696, 0.2869624566398614,
        0.6418070096050575, 0.32001945587062103,
        0.600087318919045, 0.3521568998263195
    )

    private val newPoints = listOf(
        0.26666666666666666, 0.44227886056971516,
        0.232, 0.4062968515742129,
        0.224, 0.3650674662668666,
        0.25066666666666665, 0.3268365817091454,
        0.30533333333333335, 0.29910044977511246,
        0.37733333333333335, 0.29160419790104947,
        0.44666666666666666, 0.30509745127436283,
        0.4693333333333333, 0.2661169415292354,
        0.5026666666666667, 0.22938530734632684,
        0.568, 0.2098950524737631,
        0.6413333333333333, 0.20539730134932535,
        0.712, 0.2166416791604198,
        0.7613333333333333, 0.24662668665667167,
        0.7746666666666666, 0.2871064467766117,
        0.7666666666666667, 0.328335832083958,
        0.84, 0.3350824587706147,
        0.8986666666666666, 0.35907046476761617,
        0.9333333333333333, 0.39580209895052476,
        0.948, 0.43553223388305845,
        0.9453333333333334, 0.4767616191904048,
        0.9133333333333333, 0.5142428785607196,
        0.8586666666666667, 0.5412293853073463,
        0.7866666666666666, 0.5494752623688156,
        0.7466666666666667, 0.5839580209895052,
        0.7306666666666667, 0.6244377811094453,
        0.6866666666666666, 0.6574212893553223,
        0.6253333333333333, 0.6799100449775113,
        0.5533333333333333, 0.6859070464767616,
        0.484, 0.6731634182908546,
        0.43066666666666664, 0.6446776611694153,
        0.39866666666666667, 0.6079460269865068,
        0.3253333333333333, 0.610944527736132,
        0.25333333333333335, 0.6139430284857571,
        0.19333333333333333, 0.5899550224887556,
        0.156, 0.5539730134932533,
        0.14666666666666667, 0.5134932533733133,
        0.156, 0.47226386806596704,
        0.20933333333333334, 0.444527736131934,
        0.5773333333333334, 0.3853073463268366,
        0.5093333333333333, 0.4002998500749625,
        0.4693333333333333, 0.43553223388305845,
        0.476, 0.4760119940029985,
        0.5346666666666666, 0.5007496251874063,
        0.6066666666666667, 0.5104947526236882,
        0.6733333333333333, 0.49325337331334335,
        0.7133333333333334, 0.4587706146926537,
        0.6946666666666667, 0.41904047976011993,
        0.636, 0.39430284857571213
    )

}
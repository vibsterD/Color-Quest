package com.example.colorquest.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlin.math.roundToInt

// TODO: Implement the ColorPicker

data class BrushPoint(val x: Float, val y: Float, val size: Float, val color: Color)

@Composable
fun SketchInterfaceScreen() {
    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    var size by remember { mutableStateOf(Size.Zero) }
    var brushPoints by remember { mutableStateOf(listOf<BrushPoint>()) }
    var brushSize by remember { mutableStateOf(50.dp) }
    val maxBrushSize = 100.dp



    Column(Modifier.fillMaxSize()) {
        Slider(value = brushSize/maxBrushSize, onValueChange = {brushSize = (it * maxBrushSize.value).dp})

        Surface(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .onSizeChanged { size = it.toSize() }
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            brushPoints.forEach { point ->
                                drawCircle(point.color, point.size, Offset(point.x, point.y))
                            }
                        }
                    }
                    .pointerInput(Unit) {


                        detectDragGestures(
                            onDragStart = { offset ->
                                offsetX.value = offset.x
                                offsetY.value = offset.y
                            },
                        ) { change, dragAmount ->
                            val original = Offset(offsetX.value, offsetY.value)
                            val summed = original + dragAmount
                            val newValue = Offset(
                                x = summed.x.coerceIn(0f, size.width - brushSize.toPx()),
                                y = summed.y.coerceIn(0f, size.height - brushSize.toPx())
                            )
                            brushPoints += BrushPoint(
                                x = newValue.x,
                                y = newValue.y,
                                size = brushSize.toPx(),
                                color = Color.Blue
                            )
                            offsetX.value = newValue.x
                            offsetY.value = newValue.y

                        }

                    }
            ) {

                Box(
                    Modifier
                        .offset {
                            IntOffset(
                                offsetX.value.roundToInt(),
                                offsetY.value.roundToInt()
                            )
                        }
                        .size(brushSize)
                        .clip(CircleShape)
                        .background(Color.Blue)
                )

            }

        }

    }



}


package com.example.colorquest.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
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
    var brushColor: Color by remember { mutableStateOf(Color.Blue) }
    val context = LocalContext.current
    var openColorPickerDialog: Boolean by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize()) {
        Slider(value = brushSize/maxBrushSize, onValueChange = {brushSize = (it * maxBrushSize.value).dp})
        Button(onClick = { openColorPickerDialog = true }) {
            Text("Change Color")
        }
        if(openColorPickerDialog) {
            ColorPickerDialog(context = context, changeColor = { brushColor = it }, onDismissRequest = { openColorPickerDialog = false })
        }

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
                                color = brushColor
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
                        .background(brushColor)
                )

            }

        }

    }



}


@Composable
fun ColorPickerDialog(context: Context, changeColor: (Color) -> Unit, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),

        ) {
            ColorPickerCard(context = context,
                changeColor = {color ->
                    changeColor(color)
                }
            )
        }
    }

}

@Composable
fun ColorPickerCard(context: Context, changeColor: (Color) -> Unit) {
    val controller = ColorPickerController()
    var brushColor: Color by remember { mutableStateOf(Color.Transparent) }

    Card {
        Column(
            Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(brushColor)
            )

            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(10.dp),
                controller = controller,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    changeColor(colorEnvelope.color)
                    brushColor = colorEnvelope.color
                }
            )

        }


    }


}



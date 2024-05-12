package com.example.colorquest.ui.screens

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.applyCanvas
import coil.compose.rememberImagePainter
import coil.transform.Transformation
import com.example.colorquest.MainActivity
import com.example.colorquest.R
import com.example.colorquest.data.ImageEntity
import com.example.colorquest.data.ImageViewModel
import com.example.colorquest.ui.ColorPalette
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt


// TODO: Implement the ColorPicker

data class BrushPoint(val x: Float, val y: Float, val size: Float, val color: Color)

@Composable
fun ColorPickerDialog(
    context: Context,
    changeColor: (Color) -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),

            ) {
            ColorPickerCard(context = context,
                changeColor = { color ->
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

@Composable
fun DrawingNameDialog(
    onNameConfirmed: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    var drawingName by remember { mutableStateOf(TextFieldValue()) }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Enter drawing name:")
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = drawingName,
                    onValueChange = {
                        drawingName = it
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    onNameConfirmed(drawingName.text)
                }) {
                    Text("Confirm")
                }
            }
        }
    }
}

@Composable
fun SketchInterface(context: Context, imageViewModel: ImageViewModel) {

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    DisposableEffect(Unit) {
        // Check for camera permission when the composable is first composed
        val permissionCheckResult =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            cameraLauncher.launch(uri)
        } else {
            // Request camera permission
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }

        // Clean up if the composable is removed from the composition
        onDispose { }
    }

    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    var size by remember { mutableStateOf(Size.Zero) }
    var brushPoints by remember { mutableStateOf(listOf<BrushPoint>()) }
    var brushSize by remember { mutableStateOf(50.dp) }
    val maxBrushSize = 100.dp
    var brushColor: Color by remember { mutableStateOf(Color.Blue) }
    val context = LocalContext.current
    var openColorPickerDialog: Boolean by remember { mutableStateOf(false) }
    var serializedBrushPoints: String by remember { mutableStateOf("") }
    var drawingNameDialogShown by remember { mutableStateOf(false) }
    var drawingName by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val showDialog = { drawingNameDialogShown = true }

    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(0.75f)
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.White)
                .border(BorderStroke(2.dp, Color.Black))
        ) {
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
                    val painter = rememberImagePainter(
                        data = capturedImageUri,
                        builder = {
                            transformations(GrayscaleTransformation())
                        }
                    )
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painter,
                        contentDescription = "Captured Image!"
                    )

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

        Box(
            modifier = Modifier
                .weight(0.25f)
                .fillMaxWidth()
                .background(color = ColorPalette.primaryLight)
                .border(BorderStroke(2.dp, Color.Black))
        ) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Slider(
                    value = brushSize / maxBrushSize,
                    onValueChange = { brushSize = (it * maxBrushSize.value).dp },
                    colors = SliderDefaults.colors(
                        thumbColor = Color.Black,
                        activeTrackColor = ColorPalette.primary,
                        inactiveTrackColor = ColorPalette.primary.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.size(width = 400.dp, height = 50.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularIconButton(
                        icon = R.drawable.save_icon,
                        onClick = {
                            showDialog()
                        }
                    )
                    CircularIconButton(
                        icon = R.drawable.color_picker,
                        onClick = { openColorPickerDialog = true }
                    )
                }
            }
        }
    }

    if (drawingNameDialogShown) {
        DrawingNameDialog(
            onNameConfirmed = { name ->
                drawingName = name
                drawingNameDialogShown = false
                coroutineScope.launch {
                    // Serialize brush points when save button is clicked
                    serializedBrushPoints = serializeBrushPoints(brushPoints)
                    // Create ImageEntity with drawing name and other data
                    val imageEntity = ImageEntity(
                        uri = capturedImageUri.toString(), // Replace with actual image data
                        drawingName = drawingName,
                        lastUpdatedTimestamp = System.currentTimeMillis(),
                        serializedBrushPoints = serializedBrushPoints
                    )

                    // Insert imageEntity into the database
                    imageViewModel.insert(imageEntity)
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            },
            onDismissRequest = {
                drawingNameDialogShown = false
            }

        )
    }

    if (openColorPickerDialog) {
        ColorPickerDialog(
            context = context,
            changeColor = { brushColor = it },
            onDismissRequest = { openColorPickerDialog = false })
    }
}

@Composable
fun CircularIconButton(icon: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(110.dp)
            .border(2.dp, Color.Black, shape = CircleShape)
            .clickable(onClick = onClick)
            .background(Color.White, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(60.dp)
        )
    }
}


private fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

private fun Uri.toBitmap(contentResolver: ContentResolver): Bitmap? {
    return try {
        MediaStore.Images.Media.getBitmap(contentResolver, this)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

class GrayscaleTransformation() : Transformation {

    override val cacheKey: String = GrayscaleTransformation::class.java.name

    override suspend fun transform(input: Bitmap, size: coil.size.Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        paint.colorFilter = COLOR_FILTER

        val output = createBitmap(input.width, input.height, input.config)
        output.applyCanvas {
            drawBitmap(input, 0f, 0f, paint)
        }

        return output
    }

    override fun equals(other: Any?) = other is GrayscaleTransformation

    override fun hashCode() = javaClass.hashCode()

    override fun toString() = "GrayscaleTransformation()"

    private companion object {
        val COLOR_FILTER = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
    }
}

fun serializeBrushPoints(brushPoints: List<BrushPoint>): String {
    val gson = Gson()
    return gson.toJson(brushPoints)
}




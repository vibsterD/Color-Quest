package com.example.colorquest.ui.screens

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberImagePainter
import coil.transform.Transformation
import com.example.colorquest.MainActivity
import com.example.colorquest.R
import com.example.colorquest.data.ImageEntity
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


data class BrushPoint(val x: Float, val y: Float, val size: Dp, val color: Color)

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

fun SketchInterface(context: Context, homeScreenViewModel: HomeScreenViewModel, sketch: ImageEntity? = null, isShake: Boolean = false, OldUri: Uri): Uri {
//fun SketchInterface(context: Context, homeScreenViewModel: HomeScreenViewModel) {
    var brushPoints by remember { mutableStateOf(listOf<BrushPoint>()) }
    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    var showToast by remember { mutableStateOf(false) }

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
    var emptyRequired by remember { mutableStateOf(isShake) }


    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    var size by remember { mutableStateOf(Size.Zero) }
    var brushSize by remember { mutableStateOf(50.dp) }
    val maxBrushSize = 100.dp
    var brushColor: Color by remember { mutableStateOf(Color.Blue) }
    val context = LocalContext.current
    var openColorPickerDialog: Boolean by remember { mutableStateOf(false) }
    var serializedBrushPoints: String by remember { mutableStateOf("") }
    var drawingNameDialogShown by remember { mutableStateOf(false) }
    var drawingName by remember { mutableStateOf("") }
    val showDialog = { drawingNameDialogShown = true }

    if(isShake){
        DisposableEffect(Unit) {
            brushPoints = emptyList()
            capturedImageUri = OldUri
            Log.d("Inside Uri", uri.toString())

            onDispose { }
        }

    }
    else {
        if(sketch != null) {
            DisposableEffect(Unit) {
                capturedImageUri = Uri.parse(sketch.uri)
                drawingName = sketch.drawingName
                serializedBrushPoints = sketch.serializedBrushPoints
                brushPoints = Gson().fromJson(serializedBrushPoints, Array<BrushPoint>::class.java).toList()
                onDispose { }
            }
        } else{
            // First time
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
                onDispose {

                }
            }


        }
    }





    val coroutineScope = rememberCoroutineScope()


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
                                val canvasHeight = size.height
                                val canvasWidth = size.width

                                drawContent()
                                brushPoints.forEach { point ->
                                    drawCircle(
                                        point.color,
                                        point.size.toPx(),
                                        Offset(point.x * size.width, point.y * size.height)
                                    )
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
                                    x = newValue.x / size.width,
                                    y = newValue.y / size.height,
                                    size = brushSize,
                                    color = brushColor
                                )
                                offsetX.value = newValue.x
                                offsetY.value = newValue.y

                            }

                        }
                ) {
                    Log.d("Captured Image Uri", capturedImageUri.toString())
                    if(!isShake && sketch == null && capturedImageUri != Uri.EMPTY && !showToast){
                        Toast.makeText(context, "Processing image! may take upto 10 seconds", Toast.LENGTH_SHORT).show()
                        showToast = true
                    }
                    val painter = rememberImagePainter(
                        data = capturedImageUri,
                        builder = {
                            transformations(GrayscaleTransformationWithEdges())
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
                    homeScreenViewModel.insertImage(imageEntity)
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

    return capturedImageUri
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

class GrayscaleTransformationWithEdges : Transformation {

    override val cacheKey: String = GrayscaleTransformationWithEdges::class.java.name

    override suspend fun transform(input: Bitmap, size: coil.size.Size): Bitmap {
        val output = Bitmap.createBitmap(input.width, input.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        // Convert the input bitmap to grayscale
        val paint = Paint()
        val matrix = ColorMatrix()
        matrix.setSaturation(0f)
        paint.colorFilter = ColorMatrixColorFilter(matrix)
        canvas.drawBitmap(input, 0f, 0f, paint)

        // Apply edge detection using Sobel operator
        val sobel = SobelOperator(input)
        sobel.apply(43)
        val edgesBitmap = sobel.getResultBitmap()

        // Overlay edges on the grayscale image
        canvas.drawBitmap(edgesBitmap, 0f, 0f, null)

        return output
    }

    override fun equals(other: Any?) = other is GrayscaleTransformationWithEdges

    override fun hashCode() = javaClass.hashCode()

    override fun toString() = "GrayscaleTransformationWithEdges()"
}

class SobelOperator(private val bitmap: Bitmap) {
    private lateinit var resultBitmap: Bitmap

    fun apply(threshold: Int) {
        val grayscaleBitmap = toGrayscale(bitmap)
        val edgeBitmap = Bitmap.createBitmap(grayscaleBitmap.width, grayscaleBitmap.height, Bitmap.Config.ARGB_8888)
        val sobelX = intArrayOf(-1, 0, 1, -2, 0, 2, -1, 0, 1)
        val sobelY = intArrayOf(-1, -2, -1, 0, 0, 0, 1, 2, 1)

        for (x in 1 until grayscaleBitmap.width - 1) {
            for (y in 1 until grayscaleBitmap.height - 1) {
                var sumX = 0
                var sumY = 0
                for (i in 0 until 3) {
                    for (j in 0 until 3) {
                        val pixel = grayscaleBitmap.getPixel(x + i - 1, y + j - 1)
                        val gray = (pixel shr 16 and 0xFF)
                        sumX += gray * sobelX[i * 3 + j]
                        sumY += gray * sobelY[i * 3 + j]
                    }
                }
                val magnitude = Math.sqrt((sumX * sumX + sumY * sumY).toDouble()).toInt()
                val edgePixel = if (magnitude > threshold) 0 else 255 // Thresholding
                edgeBitmap.setPixel(x, y,
                    (0xFF000000 or ((edgePixel shl 16).toLong()) or ((edgePixel shl 8).toLong()) or edgePixel.toLong()).toInt()
                )
            }
        }

        resultBitmap = edgeBitmap
    }

    fun getResultBitmap(): Bitmap {
        return resultBitmap
    }

    private fun toGrayscale(bitmap: Bitmap): Bitmap {
        val grayscaleBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.RGB_565)
        val canvas = Canvas(grayscaleBitmap)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        canvas.drawBitmap(bitmap, null, rect, paint)
        return grayscaleBitmap
    }
}


fun serializeBrushPoints(brushPoints: List<BrushPoint>): String {
    val gson = Gson()
    return gson.toJson(brushPoints)
}




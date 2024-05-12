package com.example.colorquest.ui.screens

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.colorquest.R
import com.example.colorquest.SketchInterfaceActivity
import com.example.colorquest.data.ImageEntity
import com.example.colorquest.ui.AppViewModelProvider
import com.example.colorquest.ui.ColorPalette

@Composable
fun ViewSavedDrawings() {
    val homeScreenViewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val savedSketchesUiState by homeScreenViewModel.savedSketchesUiState.collectAsState()

    val font = FontFamily(Font(R.font.adriana))
    val coroutineScope = rememberCoroutineScope()
//    var userImages by remember { mutableStateOf<List<ImageEntity>>(emptyList()) }
//
//    LaunchedEffect(Unit) {
//        coroutineScope.launch {
//            userImages = imageViewModel.getAllImages()
//        }
//    }

    Scaffold(
        modifier = Modifier.background(ColorPalette.background),
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp)
                    .background(ColorPalette.background),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Header(font)
                    Spacer(modifier = Modifier.height(16.dp))
                    SavedDrawingsGrid(savedSketchesUiState.itemList)
                }
            }
        }
    )
}

@Composable
fun Header(font: FontFamily) {
    Text(
        text = "Your Drawings",
        fontFamily = font,
        fontSize = 70.sp,
        fontWeight = FontWeight.Bold,
        color = ColorPalette.primary
    )
}

@Composable
fun SavedDrawingsGrid(savedSketches: List<ImageEntity>) {

    val context = LocalContext.current
    val sketchInterface = { sketch: ImageEntity ->
        val intent = Intent(context, SketchInterfaceActivity::class.java)
        intent.putExtra("sketch", sketch)
        context.startActivity(intent)
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {



        items(savedSketches.size) { index ->
            val sketch = savedSketches[index]
            val homeScreenViewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(Color.Gray)
                    .clickable {
                        Log.d("SavedDrawingsGrid", "Clicked on sketch: $sketch")
                        sketchInterface(sketch)
                    }
            )
            Text(text = sketch.drawingName, fontSize = 24.sp)
        }
    }
}


@Preview
@Composable
fun ViewSavedDrawingsPreview() {
    SavedDrawingsGrid(listOf())
}

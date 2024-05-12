package com.example.colorquest.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colorquest.R
import com.example.colorquest.data.ImageEntity
import com.example.colorquest.data.ImageViewModel
import com.example.colorquest.ui.ColorPalette
import kotlinx.coroutines.launch

@Composable
fun ViewSavedDrawings(imageViewModel: ImageViewModel) {
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
                    SavedDrawingsGrid()
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
fun SavedDrawingsGrid() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(5) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Showing blank boxes for now. In the future, these boxes will display saved drawings.
                repeat(2) {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(Color.Gray)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun ViewSavedDrawingsPreview() {
    SavedDrawingsGrid()
}

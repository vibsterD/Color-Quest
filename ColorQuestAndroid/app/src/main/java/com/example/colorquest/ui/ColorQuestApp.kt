package com.example.colorquest.ui

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.colorquest.R
import com.example.colorquest.ViewSavedDrawingsActivity
import com.example.colorquest.ui.screens.CaptureImageScreen
import com.example.colorquest.ui.screens.HomeScreen
import com.example.colorquest.ui.screens.SketchInterfaceScreen


// enum for the different screens
enum class Screen {
    HOME,
    SKETCH_INTERFACE,
    CAMERA_CAPTURE,
}

// Defining the colour theme for the app.
// This is for testing out, might shift these colours to Theme.kt/Color.kt.
object ColorPalette {
    val primary = Color(0xFFE91E63) // Pink
    val primaryLight = Color(0xFFFF80AB) // Light pink
    val background = Color.White
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorQuestApp() {
    var currentScreen by remember { mutableStateOf(Screen.HOME) }


    Scaffold(
        topBar = { TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = { Text(text = "Color Quest") })}
    ) {innerPadding ->
        Surface(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            if (currentScreen == Screen.HOME) {
                HomeScreen(Modifier.fillMaxSize(), { screen ->  currentScreen = screen})
            } else if (currentScreen == Screen.SKETCH_INTERFACE) {
                 SketchInterfaceScreen()
            } else if (currentScreen == Screen.CAMERA_CAPTURE) {
                 CaptureImageScreen()
            }

        }
    }
}

@Composable
fun ColourQuestApp(context: Context) {
    val font = FontFamily(Font(R.font.adriana))

    Scaffold(
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorPalette.background)
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Header(font)
                    Spacer(modifier = Modifier.height(64.dp))
                    Buttons(font, context)
                }
            }
        }
    )
}

@Composable
fun Header(font: FontFamily) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_colour_book),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp).padding(bottom = 20.dp),
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Colour Quest",
                fontFamily = font,
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold,
                color = ColorPalette.primary
            )
        }
    }
}


@Composable
fun Buttons(font: FontFamily, context: Context) {
    val viewSavedDrawings = {
        val intent = Intent(context, ViewSavedDrawingsActivity::class.java)
        context.startActivity(intent)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .height(56.dp)
                .background(ColorPalette.primaryLight, RoundedCornerShape(8.dp))
        ) {
            Button(
                onClick = { /* Handle Create New Drawing button click */ },
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            ) {
                Text(
                    text = "Create New Drawing",
                    fontFamily = font,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Box(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .height(56.dp)
                .background(ColorPalette.primaryLight, RoundedCornerShape(8.dp))
        ) {
            Button(
                onClick = viewSavedDrawings,
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            ) {
                Text(
                    text = "View Saved Drawings",
                    fontFamily = font,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

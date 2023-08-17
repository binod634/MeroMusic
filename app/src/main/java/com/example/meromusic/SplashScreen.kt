package com.example.meromusic

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen( context:Context, musicCore: MusicCore, navToHome:() -> Unit) {
    LaunchedEffect(key1 = Unit) {
        musicCore.fetchMusicFiles(context.contentResolver)
        // delay 1 second
        delay(1000)
        navToHome()
    }
    FakeSplashScreenWindow()
}

@Composable
fun FakeSplashScreenWindow() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(painter = painterResource(R.drawable.background),contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Text(text = "Musically", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, fontSize = 40.sp,modifier = Modifier.fillMaxWidth().padding(start = 24.dp, bottom = 200.dp))
    }
}

@Composable
fun SplashScreenWindow() {
    Box(modifier  = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary)) {

    }
}




@Preview
@Composable
fun ShowPreview() {
    FakeSplashScreenWindow()
}
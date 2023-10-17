package com.example.meromusic

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.meromusic.Logics.MusicCore
import com.example.meromusic.Logics.checkPermission
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(context:Context, musicCore: MusicCore, navToHome:() -> Unit) {
    val permissionGranted = remember {
        mutableStateOf(checkPermission(context))}
    val completedMusicFetch = remember { mutableStateOf(false) }


    LaunchedEffect(key1 = Unit) {

        while (true) {
            if (permissionGranted.value) {
                completedMusicFetch.value = musicCore.fetchMusicFiles(context.contentResolver)
                break
            } else {
                delay(1000)
                permissionGranted.value = checkPermission(context)
            }
        }

        while (!permissionGranted.value or !completedMusicFetch.value  ) {
            delay(500)
        }

        navToHome()
    }
    NewSplashScreen()
}

@Composable
fun FakeSplashScreenWindow() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(painter = painterResource(R.drawable.background),contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
        Text(text = "Musically", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Black, fontSize = 40.sp,modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, bottom = 200.dp))
    }
}

@Composable
fun SplashScreenWindow() {
    Box(modifier  = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)) {

    }
}


@Composable
fun NewSplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.logo_round),
                contentDescription = null,
                contentScale = ContentScale.FillBounds
            )
            Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Preview
@Composable
fun ShowPreview() {
    NewSplashScreen()
}



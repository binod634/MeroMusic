package com.example.meromusic


import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun PrimaryMusicScreen(musicCore: MusicCore,context:Context,backToHome:() -> Unit) {
    val currentMusicData by musicCore.currentlyStartedMusicData.collectAsState()
    val totalTime = currentMusicData!!.duration.toInt()
    val currentPosition = remember { mutableStateOf(0) }
    val isMusicStarted by musicCore.isStarted.collectAsState()
    val isMusicPlaying by musicCore.isPlaying.collectAsState()
    val sliderPosition = remember { mutableStateOf(0.3f) }
    val visibleRepeat  = remember { mutableStateOf(true) }
    val visibleShuffle  = remember { mutableStateOf(false) }
    val rotationSpeed = remember { mutableStateOf(1f) }
    var rotationState by remember { mutableStateOf(0f) }
    val mediaProgressPercentage = remember { mutableStateOf(0f) }

    LaunchedEffect(rotationSpeed.value) {
        while (true) {
            withFrameMillis { frameTime ->
                rotationState += rotationSpeed.value * frameTime / 1e9f // Adjust speed as needed
            }
        }
    }

    LaunchedEffect(isMusicStarted) {
        while (isMusicStarted) {
            currentPosition.value = musicCore.getMediaPlayerCurrentDuration()
            mediaProgressPercentage.value = currentPosition.value.toFloat() / totalTime
            delay(250)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        listOf(Color(0xFF4D5C72), MaterialTheme.colorScheme.primary),
                        center = Offset(1100 / 2f, 1300 / 2f),
                        radius = (1200 / 2f)
                    )
                )
        ) {
            // for spacing future content
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .weight(0.55f), contentAlignment = Alignment.Center) {
                Box(modifier = Modifier
                    .clip(CircleShape)
                    .width(250.dp)
                    .aspectRatio(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.newed),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(rotationState)
                    )
                }
            }
                // lower half section of screen
                Column(modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxHeight()
                    .weight(0.45f),
                verticalArrangement = Arrangement.spacedBy(15.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    ) {
                        Text(
                            text = currentMusicData!!.title,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1
                        )
                        Text(text = "By Alen Walker", maxLines = 1)
                    }
                    Row(modifier = Modifier.align(Alignment.End), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Icon(painter = painterResource(id = R.drawable.repeatall), contentDescription = null,  tint = Color.Unspecified, modifier = Modifier
                            .alpha(if (visibleRepeat.value) 1f else 0.4f)
                            .clickable { visibleRepeat.value = !visibleRepeat.value })
                        Icon(painter = painterResource(id = R.drawable.shuffle), contentDescription = null,  tint = Color.Unspecified, modifier = Modifier
                            .alpha(if (visibleShuffle.value) 1f else 0.4f)
                            .clickable { visibleShuffle.value = !visibleShuffle.value })
                    }
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = formatMilliseconds(currentPosition.value),
                                style = MaterialTheme.typography.labelSmall
                            )
                            Text(
                                text = formatMilliseconds(totalTime),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                        Slider(
                            value = mediaProgressPercentage.value,
                            onValueChange = {
                                musicCore.MediaSeek((it * totalTime).toInt());mediaProgressPercentage.value =
                                it
                            },
                            colors = SliderDefaults.colors(
                                Color(0xff725EA9),
                                activeTrackColor = Color.Gray
                            )
                        )
                    }
                    Row(modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically) {
                      Icon(painter = painterResource(id = R.drawable.previous), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.clickable { musicCore.playPreviousMusic(context) })
                      Icon(painter = painterResource(id = R.drawable.rewind), contentDescription = null, tint = Color.Unspecified)
                      Icon(painter = painterResource(id = if (isMusicPlaying) R.drawable.pause else R.drawable.play ), contentDescription = null, tint = Color.Unspecified, modifier = Modifier
                          .size(56.dp)
                          .clickable { musicCore.togglePlaying() })
                      Icon(painter = painterResource(id = R.drawable.seekforeward), contentDescription = null, tint = Color.Unspecified)
                      Icon(painter = painterResource(id = R.drawable.nextmusic), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.clickable { musicCore.playNextMusic(context) })
                    }
                    Spacer(modifier = Modifier
                        .height(100.dp)
                        .background(Color.Red))
                }
        }
    }
    Box(
            modifier = Modifier
                .padding(start = 24.dp, top = 32.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = null, tint = Color(0xFF0947A2), modifier = Modifier.size(32.dp).clickable { backToHome() })
        }
}

@Preview
@Composable
fun RotatingImage( rotationSpeed: Float = 1f) {
    var rotationState by remember { mutableStateOf(0f) }

    LaunchedEffect(rotationSpeed) {
        while (true) {
            withFrameMillis { frameTime ->
                rotationState += rotationSpeed * frameTime / 1e9f // Adjust speed as needed
            }
        }
    }

    Image(
        painter = painterResource(id = R.drawable.kingpng),
        contentDescription = null,
        modifier = Modifier
            .rotate(rotationState)
            .fillMaxSize()
    )
}
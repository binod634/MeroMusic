package com.example.meromusic

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun NewHomeScreen(
    musicCore: MusicCore,
    playingMusicScreen: (MusicData) -> Unit,
    oldPlaying: () -> Unit
) {
    val isMusicStarted by musicCore.isStarted.collectAsState()
    val currentPlayingMusic by musicCore.currentlyStartedMusicData.collectAsState()
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
        Column(modifier = Modifier.background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.White, Color.Transparent),
                        radius = 3500f,
                        center = Offset(1200/1f, 4000/1f)
                    ))) {
            Spacer(modifier = Modifier.height(32.dp))
            HomeHeader()
            Spacer(modifier = Modifier.height(32.dp))
            ShowRecommendationRow()
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 24.dp, start = 24.dp)
            ) {
                Text("Local music", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(4.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(musicCore.musicFiles.size) { it ->
                        var isCurrentMusicPlaying = false
                        if (isMusicStarted) {
                            if (currentPlayingMusic!!.id == musicCore.musicFiles[it].id) {
                                isCurrentMusicPlaying = true
                            }
                        }
                        ShowSelectedMusicRow(
                            musicCore.musicFiles[it],
                            isCurrentMusicPlaying,
                            showCurrentSelectedMusic = { musicData -> if (isCurrentMusicPlaying) oldPlaying() else playingMusicScreen(musicData) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun HomeHeader() {
    Box(contentAlignment = Alignment.Center,modifier = Modifier.fillMaxWidth()) {
        Text("Musically", style = MaterialTheme.typography.labelLarge, color = Color(0xff00FFC6))
    }
}

@Composable
fun ShowSelectedMusicRow(musicData : MusicData,isThisPlaying: Boolean, showCurrentSelectedMusic:(MusicData) -> Unit) {
    Card(elevation = CardDefaults.cardElevation(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isThisPlaying) Color(0xffFF0000) else MaterialTheme.colorScheme.secondary)
                .height(60.dp)
                .clickable { showCurrentSelectedMusic(musicData) },
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.padding(start = 16.dp,top = 8.dp,bottom = 8.dp)) {
                Image(painterResource(id = R.drawable.kingpng), contentDescription = null)
            }
            Text(text = musicData.title, style =  MaterialTheme.typography.labelSmall, maxLines = 1,modifier = Modifier.padding(end = 16.dp), color = if (isThisPlaying) Color.Green else Color.Unspecified)
        }
    }
}

@Composable
fun ShowRecommendationRow() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 24.dp)) {
        Text("Our Recommendation", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(4.dp))
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(10) {
                BoxIcon(R.drawable.aylex)
            }
        }
    }
}

@Composable
fun BoxIcon(img: Int) {
    Card(elevation = CardDefaults.cardElevation(16.dp), shape = RoundedCornerShape(12.dp), modifier = Modifier.size(100.dp)) {
        Image(painter = painterResource(img), contentDescription = null)
    }
}


@Preview(showBackground = true)
@Composable
fun ShowPreview2() {
    Column {
        Spacer(modifier = Modifier.height(50.dp))
        ShowRecommendationRow()
    }
}
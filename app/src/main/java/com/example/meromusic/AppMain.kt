package com.example.meromusic

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPrimary() {
    val context = LocalContext.current
    val musicCore = MusicCore()
    val isMusicStarted by musicCore.isStarted.collectAsState()
    val isMusicPlaying by musicCore.isPlaying.collectAsState()
    musicCore.fetchMusicFiles(context.contentResolver)
    val currentPlayingMusic by musicCore.currentlyStartedMusicData.collectAsState()
    Scaffold(topBar = { TopAppBar() }, bottomBar = { ShowMusicBar(
        musicData = currentPlayingMusic,
        isMusicStarted = isMusicStarted,
        isMusicPlaying = isMusicPlaying
    ) {
//         if (isMusicPlaying) musicCore.pausePlaying() else musicCore.resumePlaying()
    } }) { it ->
        AppContent(
                it,
                musicCore.musicFiles,
                playMusic = { musicCore.startPlaying(context, it) }
            )
    }
}


@Composable
fun AppContent(it:PaddingValues,musicList:MutableList<MusicData>,playMusic: (MusicData) -> Unit) {
    Column(modifier = Modifier.padding(it)) {
            LazyColumn {
                items(musicList.size) {
                    ShowMusicRow(musicList[it]) { currentMusicData ->
                        playMusic(currentMusicData)
                    }
                }
            }
    }
}


@Composable
fun ShowMusicRow(musicModel: MusicData,playMusic:(MusicData) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
        .clickable { playMusic(musicModel) }
        ) {
        Box(modifier = Modifier
            .aspectRatio(1f)
            .fillMaxHeight()
            .padding(start = 10.dp, end = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.ThumbUp, contentDescription = null)
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
            Text(text  = musicModel.title, maxLines = 1)
        }
        Spacer(modifier = Modifier.width(50.dp))
    }
}


@Composable
fun ShowMusicBar(musicData: MusicData?, isMusicStarted:Boolean, isMusicPlaying:Boolean, PauseAndPlayMusic:() -> Unit) {
      AnimatedVisibility(
        visible = isMusicStarted,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(animationSpec = tween(durationMillis = 500))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .height(65.dp)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = musicData?.title ?: "Unknown Title",
                maxLines = 1,
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            )
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
            ) {
                Icon(
                     if (isMusicPlaying) painterResource(id = R.drawable.music4) else painterResource(
                         id = R.drawable.music3
                     ),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp)
                        .aspectRatio(1f)
                        .clickable { PauseAndPlayMusic() }
                )
            }
        }
    }
}

@Composable
fun TopAppBar() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primary)
        .height(50.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxHeight()
            .padding(10.dp)
            .aspectRatio(1f),
            contentAlignment = Alignment.Center

            ) {
            Image(painter = painterResource(id = R.mipmap.ic_launcher_foreground), contentDescription = null)
        }
        Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
            Text(
                text = "Mero Music",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun NewMusicHomeSreen() {
    val context = LocalContext.current
    val musicCore = MusicCore()

    val currentPlayingMusic by musicCore.currentlyStartedMusicData.collectAsState()
    Column(modifier = Modifier.background(Color(0xff0A1A55))) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.musicbg),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
            Box(modifier = Modifier.align(Alignment.Center)) {
                Text(text = "Let's", modifier = Modifier.fillMaxWidth())
                Text(text = "Explore")
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xaa091542))
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "All Music",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xff00C6C3)
                )
                Text(
                    text = "Playlist",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xff00C6C3)
                )
                Text(
                    text = "Folder",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xff00C6C3)
                )
                Text(
                    text = "Favourite",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xff00C6C3)
                )
            }
        }
        Box {
            ShowLowerContent(musicCore)
        }
    }
}

@Composable
fun ShowLowerContent(musicCore: MusicCore) {
    LazyColumn {
        items(musicCore.musicFiles.size) {
            IndividualMusicRow(musicCore.musicFiles[it].title)
        }
    }
}

@Composable
fun IndividualMusicRow(title:String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .shadow(
            elevation = 8.dp,
            shape = ShapeDefaults.Medium,
            clip = false,
            ambientColor = Color.White,
            spotColor = Color.White
        )
        .padding(top = 10.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically) {
        Image(painterResource(id = R.drawable.musicicon), contentDescription = null, modifier = Modifier
            .height(30.dp)
            .padding(start = 16.dp)
            .aspectRatio(1f))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth())
    }
}

@Composable
fun NewHomeScreen(musicCore: MusicCore,context: Context, playingMusicScreen:(MusicData) -> Unit) {
    val isMusicStarted by musicCore.isStarted.collectAsState()
    val isMusicPlaying by musicCore.isPlaying.collectAsState()
    val currentPlayingMusic by musicCore.currentlyStartedMusicData.collectAsState()
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
        Column(modifier = Modifier.background(
                    brush = Brush.radialGradient(
                        colors = listOf(Color.White, Color.Transparent),
                        radius = 3500f,
                        center = Offset(1200/1f, 4000/1f)
                    ))) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.2f),
            ) {
                Text(
                    text = "${musicCore.musicFiles.size} Music Found",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.8f),
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(end = 24.dp, start = 24.dp)
                ) {
                    items(musicCore.musicFiles.size) { it ->
                        ShowSelectedMusicRow(
                            musicCore.musicFiles[it],
                            isThisPlaying = musicCore.musicFiles[it] == currentPlayingMusic,
                            showCurrentSelectedMusic = { musicData -> playingMusicScreen(musicData) }
                        )
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowSelectedMusicRow(musicData : MusicData,isThisPlaying: Boolean, showCurrentSelectedMusic:(MusicData) -> Unit) {
    Card(elevation = CardDefaults.cardElevation(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xff0883D9))
                .height(60.dp)
                .clickable { showCurrentSelectedMusic(musicData) },
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.padding(start = 16.dp,top = 8.dp,bottom = 8.dp)) {
                Image(painterResource(id = R.drawable.kingpng), contentDescription = null)
            }
            Text(text = musicData.title, style = MaterialTheme.typography.labelSmall, maxLines = 1,modifier = Modifier.padding(end = 16.dp))
        }
    }
}
//
//@Preview
//@Composable
//fun ShowNewMusicHome() {
//    NewMusicHomeSreen()
//}


//
//@Preview
//@Composable
//fun ShowTopBAr() {
//    ShowMusicBar(MusicData(1,"something","","",1000L,"".toUri()),isMusicStarted = true, isMusicPlaying = true, PauseAndPlayMusic = {})
//}


@Preview
@Composable
fun ShowRowShadow() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "Someone")
            Text(text = "Somethings")
        }
}
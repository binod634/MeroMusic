package com.example.meromusic

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.meromusic.ui.theme.MeroMusicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeroMusicTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val context = LocalContext.current
//                  when (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> { /* NULL */ } )
                    val musicCore = MusicCore()
                    musicCore.fetchMusicFiles(context.contentResolver)
                    AppNavigation(context,musicCore)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(context:Context,musicCore:MusicCore) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = MusicScreens.Home.name) {
        composable(route = MusicScreens.Home.name) {
                NewHomeScreen(musicCore,context, playingMusicScreen = { musicData -> musicCore.startPlaying(context,musicData);navController.navigate(MusicScreens.PlayingMusicScreen.name) }, oldPlaying = { navController.navigate(MusicScreens.PlayingMusicScreen.name) })
            }
        composable(route = MusicScreens.PlayingMusicScreen.name) {
            PrimaryMusicScreen( musicCore,context, backToHome = { navController.popBackStack() } )
            }
    }
}
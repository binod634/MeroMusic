package com.example.meromusic

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PowerManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.meromusic.AppScreens.Screens
import com.example.meromusic.Logics.MusicCore
import com.example.meromusic.ui.theme.MeroMusicTheme

class MainActivity : ComponentActivity() {
    private var wakeLck:PowerManager.WakeLock? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLck = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MeroMusic:PartialWakeLock")
        setContent {
            MeroMusicTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!wakeLck.isHeld) {
                        wakeLck.acquire(60*60*1000L /*60 minutes*/)
                    }

                    val context = LocalContext.current
                    val musicCore = MusicCore()
                    checkAndRequestPermissions(context)

                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screens.Splash.name) {
                    composable(route = Screens.Splash.name) {
                        SplashScreen(
                            context,
                            musicCore,
                            navToHome = {
                                navController.navigate(Screens.Home.name)
                            })
                        }
                    composable(route = Screens.Home.name) {
                        NewHomeScreen(
                            musicCore,
                            playingMusicScreen = { musicData ->
                                musicCore.startPlaying(context,musicData);
                                navController.navigate(Screens.PlayingMusicScreen.name)
                            }) {
                                navController.navigate(Screens.PlayingMusicScreen.name)
                            }
                        }
                    composable(route = Screens.PlayingMusicScreen.name) {
                            PrimaryMusicScreen( musicCore,context, backToHome = { navController.popBackStack() } )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeLck?.release()
    }

    private fun checkAndRequestPermissions(context:Context) {
        if (ContextCompat.checkSelfPermission(context,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),101)
        }
    }
}

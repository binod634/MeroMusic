package com.example.meromusic.Logics

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.meromusic.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import kotlin.random.Random


data class MusicData(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val filePath: Uri
)

class MusicCore:ViewModel()   {
    private var _isStarted =   MutableStateFlow(false)
    val isStarted = _isStarted.asStateFlow()
    private var _isShuffle =   MutableStateFlow(false)
    val isShuffle = _isShuffle.asStateFlow()
    private var _isRepeat =   MutableStateFlow(true)
    val isRepeat = _isRepeat.asStateFlow()
    private var _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()
    private var _currentlyStartedMusicData:MutableStateFlow<MusicData?> = MutableStateFlow(null)
    val currentlyStartedMusicData = _currentlyStartedMusicData.asStateFlow()
    val musicFiles = mutableListOf<MusicData>()
    val lockedRepeatShuffleState = false


    // MediaPlayer object
    private var mediaPlayer:MediaPlayer? = null

    // for top layout Row
    val topImgFiles = listOf<Int>(
        R.drawable.aylex,
        R.drawable.rabbit,
        R.drawable.damtaronothing,
        R.drawable.aylexfighter,
        R.drawable.aylexfurious,
        R.drawable.anotherkid,
        R.drawable.aylexliving
    )

    val topMusicFiles = listOf<Int>(
        R.raw.aylex,
        R.raw.rabbittheft,
        R.raw.damatro,
        R.raw.damtarostart,
        R.raw.furious,
        R.raw.aylexokay,
        R.raw.aylextoo
    )

    fun playRecommendedMusic(context:Context,id:Int) {
        if (_isStarted.value) {
            mediaPlayer?.release()
        }
        mediaPlayer = MediaPlayer.create(context,topMusicFiles[id])
        mediaPlayer?.start()
        _currentlyStartedMusicData.value = null
        _isStarted.value = true
        _isPlaying.value = true
    }

    fun fetchMusicFiles(contentResolver: ContentResolver):Boolean {

        // for not stressing State variable
        val localMusicFiles = mutableListOf<MusicData>()

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//        val projection = arrayOf(MediaStore.Audio.Media.DATA)

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        )

        val cursor = contentResolver.query(uri, projection, selection, null, sortOrder, null)

        // array of music files

        cursor?.use { innerCursor ->
            val idColumn = innerCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = innerCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = innerCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = innerCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationColumn = innerCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val filePathColumn = innerCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while (innerCursor.moveToNext()) {
                if (File(innerCursor.getString(filePathColumn)).exists()) {
                    localMusicFiles.add(
                        MusicData(
                            innerCursor.getLong(idColumn),
                            innerCursor.getString(titleColumn),
                            innerCursor.getString(artistColumn),
                            innerCursor.getString(albumColumn),
                            innerCursor.getLong(durationColumn),
                            innerCursor.getString(filePathColumn).toUri()
                        )
                    )
                }
            }
        }
        musicFiles.addAll(localMusicFiles)
        return true
    }

    fun startPlaying(context:Context, musicData: MusicData) {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.release()
        }

        _currentlyStartedMusicData.value = musicData
        mediaPlayer = MediaPlayer.create(context,musicData.filePath)

        // checking for null. I am unsure if that can actually happen, but better safe then sorry.
        if (mediaPlayer == null) {
            throw Exception("Cannot Start MediaPlayer")
        }

        // TODO: Change this as this isn't always next music But shuffle or Repeat-Once only
        mediaPlayer!!.setOnCompletionListener { playNewMusic(context) }
        mediaPlayer?.start()
        _isStarted.value = true
        _isPlaying.value = true
    }

    fun togglePlaying() {
        if (_isPlaying.value) {
            mediaPlayer?.pause()
            _isPlaying.value = false
        } else {
            mediaPlayer?.start()
            _isPlaying.value = true
        }
    }

    fun playNewMusic(context: Context) {
        var id = 0
        if (_isShuffle.value && _isRepeat.value) {
            id = Random(musicFiles.size).nextInt()
            startPlaying(context,musicFiles[id])
        } else if (_isRepeat.value) {
            playNextMusic(context)
        }
    }

    fun playNextMusic(context: Context) {
        val id = (musicFiles.indexOf(currentlyStartedMusicData.value) + 1) % musicFiles.size
        startPlaying(context,musicFiles[id])
    }
    fun playPreviousMusic(context: Context) {
        val id = if (musicFiles.indexOf(currentlyStartedMusicData.value) - 1 < 0) 0 else musicFiles.indexOf(currentlyStartedMusicData.value) - 1
        startPlaying(context,musicFiles[id])
    }



    fun endPlaying() {
        _isStarted.value = false
        _isPlaying.value = false
        mediaPlayer?.release()
    }

    fun currentPlaying(): MusicData? {
        return _currentlyStartedMusicData.value
    }

    fun getMediaPlayerCurrentDuration():Int {
        return mediaPlayer!!.currentPosition
    }

    fun getMediaPlayerTotalDuration():Int {
        return mediaPlayer!!.duration
    }

    fun MediaSeek(seekToTime:Int) {
        mediaPlayer!!.seekTo(seekToTime)
    }

    fun toggleMediaRepeat() {
        _isRepeat.value =  !(_isRepeat.value)
    }

    fun toggleMediaShuffle() {
        _isShuffle.value = !(_isShuffle.value)
    }

}


fun checkPermission(context:Context):Boolean {
    return ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
}
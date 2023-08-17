package com.example.meromusic

import java.time.Duration

fun formatMilliseconds(milliseconds: Int): String {
    val totalseconds = milliseconds / 1000L
    val minutes = "%02d".format(totalseconds / 60L)
    val seconds = "%02d".format(totalseconds % 60)
    return "$minutes:$seconds"
}
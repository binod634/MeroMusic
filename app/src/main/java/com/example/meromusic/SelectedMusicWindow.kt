package com.example.meromusic

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedMusicWindow() {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column {
                Image(
                    modifier = Modifier
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.newwalpaper),
                    contentDescription = null
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Icon(painter = painterResource(id = R.drawable.music1), contentDescription = null,modifier = Modifier.clickable {  })
                    Icon(painter = painterResource(id = R.drawable.music2), contentDescription = null,modifier = Modifier.clickable {  })
                    Icon(painter = painterResource(id = R.drawable.music3), contentDescription = null,modifier = Modifier.clickable {  })
                    Icon(painter = painterResource(id = R.drawable.music4), contentDescription = null,modifier = Modifier.clickable {  })
                    Icon(painter = painterResource(id = R.drawable.music5), contentDescription = null,modifier = Modifier.clickable {  })
                    Icon(painter = painterResource(id = R.drawable.music6), contentDescription = null,modifier = Modifier.clickable {  })
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
    }
}
@Preview
@Composable
fun ShowSelectedWindowPreview() {
    SelectedMusicWindow()
}
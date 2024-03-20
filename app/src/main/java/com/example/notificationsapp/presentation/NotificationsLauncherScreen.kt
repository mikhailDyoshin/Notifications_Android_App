package com.example.notificationsapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun NotificationsLauncherScreen(
    notifyBasic: () -> Unit,
    notifyReply: () -> Unit,
    clearAll: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = { notifyBasic() }) {
                Text(text = "Basic")
            }
            Button(onClick = { notifyReply() }) {
                Text(text = "Reply")
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = { clearAll() }) {
                Text(text = "Clear all")
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun NotificationsLauncherScreenPreview() {
    NotificationsLauncherScreen(
        notifyBasic = {},
        notifyReply = {},
        clearAll = {},
    )
}
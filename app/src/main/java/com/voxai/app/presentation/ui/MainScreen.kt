package com.voxai.app.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.voxai.app.R

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val isRecording by viewModel.isRecording.collectAsState()
    val isPaused by viewModel.isPaused.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo o título
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Indicador de estado
        Text(
            text = when {
                !isRecording -> stringResource(id = R.string.start_recording)
                isPaused -> stringResource(id = R.string.pause_recording)
                else -> stringResource(id = R.string.stop_recording)
            },
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de control
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botón de grabación
            FloatingActionButton(
                onClick = { viewModel.toggleRecording() },
                containerColor = if (isRecording) 
                    MaterialTheme.colorScheme.error 
                else 
                    MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = if (isRecording) 
                        stringResource(id = R.string.stop_recording)
                    else 
                        stringResource(id = R.string.start_recording)
                )
            }

            // Botón de pausa/reanudar (solo visible durante la grabación)
            if (isRecording) {
                FloatingActionButton(
                    onClick = { viewModel.togglePause() },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(
                        imageVector = if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = if (isPaused) 
                            stringResource(id = R.string.resume_recording)
                        else 
                            stringResource(id = R.string.pause_recording)
                    )
                }
            }
        }
    }
} 
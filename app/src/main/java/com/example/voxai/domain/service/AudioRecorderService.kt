package com.example.voxai.domain.service

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AudioRecorderService(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    private var currentFile: File? = null
    private var startTime: Long = 0

    fun startRecording(): Flow<RecordingState> = flow {
        try {
            val outputFile = createOutputFile()
            currentFile = outputFile
            startTime = System.currentTimeMillis()

            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder()
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            mediaRecorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setAudioChannels(2) // Estéreo
                setAudioSamplingRate(44100) // 44.1kHz
                setAudioEncodingBitRate(128000) // 128kbps
                setOutputFile(outputFile.absolutePath)
                prepare()
                start()
            }

            emit(RecordingState.Recording)
            startAmplitudeUpdates()
        } catch (e: IOException) {
            emit(RecordingState.Error("Error al iniciar la grabación: ${e.message}"))
        }
    }

    fun stopRecording(): RecordingResult {
        return try {
            val duration = System.currentTimeMillis() - startTime
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null

            currentFile?.let { file ->
                RecordingResult.Success(
                    file = file,
                    duration = duration
                )
            } ?: RecordingResult.Error("No hay archivo de grabación")
        } catch (e: Exception) {
            RecordingResult.Error("Error al detener la grabación: ${e.message}")
        }
    }

    private fun createOutputFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "VoxAI_$timestamp.mp3"
        val recordingsDir = File(context.getExternalFilesDir(null), "Recordings").apply {
            if (!exists()) mkdirs()
        }
        return File(recordingsDir, fileName)
    }

    private suspend fun startAmplitudeUpdates() {
        while (mediaRecorder != null) {
            val amplitude = mediaRecorder?.maxAmplitude?.toFloat() ?: 0f
            emit(RecordingState.Amplitude(amplitude / 32768f))
            kotlinx.coroutines.delay(50) // Actualizar cada 50ms
        }
    }

    sealed class RecordingState {
        object Recording : RecordingState()
        data class Amplitude(val value: Float) : RecordingState()
        data class Error(val message: String) : RecordingState()
    }

    sealed class RecordingResult {
        data class Success(val file: File, val duration: Long) : RecordingResult()
        data class Error(val message: String) : RecordingResult()
    }
} 
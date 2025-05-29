package com.example.voxai.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voxai.domain.model.Recording
import com.example.voxai.domain.repository.RecordingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val recordingRepository: RecordingRepository
) : ViewModel() {

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _recordings = MutableLiveData<List<Recording>>()
    val recordings: LiveData<List<Recording>> = _recordings

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _currentPlayingRecording = MutableLiveData<Recording?>()
    val currentPlayingRecording: LiveData<Recording?> = _currentPlayingRecording

    init {
        loadRecordings()
        _status.value = "Listo para grabar"
        _isPlaying.value = false
    }

    fun startRecording() {
        viewModelScope.launch {
            try {
                _isRecording.value = true
                _status.value = "Grabando..."
                recordingRepository.startRecording()
            } catch (e: Exception) {
                _error.value = "Error al iniciar la grabación: ${e.message}"
                _isRecording.value = false
                _status.value = "Error al grabar"
            }
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            try {
                recordingRepository.stopRecording()
                _isRecording.value = false
                _status.value = "Grabación completada"
                loadRecordings()
            } catch (e: Exception) {
                _error.value = "Error al detener la grabación: ${e.message}"
                _status.value = "Error al detener la grabación"
            }
        }
    }

    fun playRecording(recording: Recording) {
        viewModelScope.launch {
            try {
                if (_currentPlayingRecording.value == recording) {
                    // Si ya está reproduciendo esta grabación, la detenemos
                    recordingRepository.stopPlayback()
                    _isPlaying.value = false
                    _currentPlayingRecording.value = null
                    _status.value = "Reproducción detenida"
                } else {
                    // Si está reproduciendo otra grabación, la detenemos primero
                    recordingRepository.stopPlayback()
                    // Iniciamos la nueva reproducción
                    recordingRepository.playRecording(recording)
                    _isPlaying.value = true
                    _currentPlayingRecording.value = recording
                    _status.value = "Reproduciendo: ${recording.fileName}"
                }
            } catch (e: Exception) {
                _error.value = "Error al reproducir la grabación: ${e.message}"
                _status.value = "Error al reproducir"
            }
        }
    }

    fun deleteRecording(recording: Recording) {
        viewModelScope.launch {
            try {
                recordingRepository.deleteRecording(recording)
                _status.value = "Grabación eliminada"
                loadRecordings()
            } catch (e: Exception) {
                _error.value = "Error al eliminar la grabación: ${e.message}"
                _status.value = "Error al eliminar"
            }
        }
    }

    private fun loadRecordings() {
        viewModelScope.launch {
            try {
                val recordingsList = recordingRepository.getRecordings()
                _recordings.value = recordingsList
            } catch (e: Exception) {
                _error.value = "Error al cargar las grabaciones: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            recordingRepository.stopPlayback()
        }
    }
} 
package com.example.voxai.presentation

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.voxai.R
import com.example.voxai.databinding.ActivityMainBinding
import com.example.voxai.domain.model.Recording
import com.example.voxai.presentation.ui.adapters.RecordingAdapter
import com.example.voxai.presentation.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val recordingAdapter = RecordingAdapter(
        onPlayClick = { recording -> viewModel.playRecording(recording) },
        onDeleteClick = { recording -> showDeleteConfirmationDialog(recording) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvRecordings.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recordingAdapter
        }
    }

    private fun setupClickListeners() {
        binding.fabRecord.setOnClickListener {
            viewModel.startRecording()
            showRecordingUI()
        }

        binding.fabStop.setOnClickListener {
            viewModel.stopRecording()
            showReadyUI()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isRecording.collect { isRecording ->
                    binding.recordingStatus.setRecording(isRecording)
                }
            }
        }

        viewModel.recordings.observe(this) { recordings ->
            recordingAdapter.submitList(recordings)
        }

        viewModel.status.observe(this) { status ->
            binding.tvStatus.text = status
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                showError(it)
                viewModel.clearError()
            }
        }

        viewModel.isPlaying.observe(this) { isPlaying ->
            // Actualizar UI según el estado de reproducción
            binding.fabRecord.isEnabled = !isPlaying
        }
    }

    private fun showRecordingUI() {
        binding.fabRecord.visibility = View.GONE
        binding.fabStop.visibility = View.VISIBLE
        binding.fabStop.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_in))
    }

    private fun showReadyUI() {
        binding.fabStop.visibility = View.GONE
        binding.fabRecord.visibility = View.VISIBLE
        binding.fabRecord.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_in))
    }

    private fun showDeleteConfirmationDialog(recording: Recording) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_recording)
            .setMessage(getString(R.string.delete_recording_confirmation, recording.fileName))
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteRecording(recording)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
} 
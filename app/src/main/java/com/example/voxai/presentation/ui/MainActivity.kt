package com.example.voxai.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.voxai.R
import com.example.voxai.databinding.ActivityMainBinding
import com.example.voxai.presentation.ui.adapters.RecordingsAdapter
import com.example.voxai.presentation.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val PERMISSION_REQUEST_CODE = 123
    private lateinit var recordingsAdapter: RecordingsAdapter

    private val requiredPermissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupUI()
        observeViewModel()
        checkPermissions()
    }

    private fun setupRecyclerView() {
        recordingsAdapter = RecordingsAdapter { recording ->
            // TODO: Implementar reproducción de audio
            Toast.makeText(this, "Reproduciendo: ${recording.fileName}", Toast.LENGTH_SHORT).show()
        }

        binding.rvRecordings.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recordingsAdapter
        }
    }

    private fun setupUI() {
        binding.fabRecord.setOnClickListener {
            if (hasRequiredPermissions()) {
                startRecording()
            } else {
                requestPermissions()
            }
        }

        binding.fabStop.setOnClickListener {
            stopRecording()
        }
    }

    private fun observeViewModel() {
        viewModel.recordingState.observe(this) { state ->
            when (state) {
                is MainViewModel.RecordingState.Ready -> {
                    binding.fabRecord.visibility = View.VISIBLE
                    binding.fabStop.visibility = View.GONE
                    binding.tvStatus.text = getString(R.string.ready_to_record)
                }
                is MainViewModel.RecordingState.Recording -> {
                    binding.fabRecord.visibility = View.GONE
                    binding.fabStop.visibility = View.VISIBLE
                    binding.tvStatus.text = getString(R.string.recording)
                }
                is MainViewModel.RecordingState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.amplitude.observe(this) { amplitude ->
            binding.waveformView.addAmplitude(amplitude)
        }

        viewModel.recordings.observe(this) { recordings ->
            recordingsAdapter.submitList(recordings)
            binding.tvRecentRecordings.visibility = if (recordings.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun checkPermissions() {
        if (!hasRequiredPermissions()) {
            requestPermissions()
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, requiredPermissions, PERMISSION_REQUEST_CODE)
    }

    private fun startRecording() {
        viewModel.startRecording()
    }

    private fun stopRecording() {
        viewModel.stopRecording()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_LONG).show()
            }
        }
    }
} 
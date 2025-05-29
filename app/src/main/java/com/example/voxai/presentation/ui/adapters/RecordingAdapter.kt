package com.example.voxai.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.voxai.databinding.ItemRecordingBinding
import com.example.voxai.domain.model.Recording
import java.text.SimpleDateFormat
import java.util.*

class RecordingAdapter(
    private val onPlayClick: (Recording) -> Unit,
    private val onDeleteClick: (Recording) -> Unit
) : ListAdapter<Recording, RecordingAdapter.RecordingViewHolder>(RecordingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordingViewHolder {
        val binding = ItemRecordingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecordingViewHolder(binding, onPlayClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: RecordingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RecordingViewHolder(
        private val binding: ItemRecordingBinding,
        private val onPlayClick: (Recording) -> Unit,
        private val onDeleteClick: (Recording) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        private var currentRecording: Recording? = null

        init {
            binding.root.setOnClickListener {
                currentRecording?.let { recording ->
                    onPlayClick(recording)
                }
            }

            binding.ivDelete.setOnClickListener {
                currentRecording?.let { recording ->
                    onDeleteClick(recording)
                }
            }
        }

        fun bind(recording: Recording) {
            currentRecording = recording
            binding.apply {
                tvFileName.text = recording.fileName
                tvDate.text = dateFormat.format(recording.dateCreated)
                tvDuration.text = formatDuration(recording.duration)
                tvFileSize.text = formatFileSize(recording.fileSize)
            }
        }

        private fun formatDuration(durationMs: Long): String {
            val seconds = durationMs / 1000
            val minutes = seconds / 60
            val remainingSeconds = seconds % 60
            return String.format("%02d:%02d", minutes, remainingSeconds)
        }

        private fun formatFileSize(size: Long): String {
            val kb = size / 1024
            val mb = kb / 1024
            return when {
                mb > 0 -> String.format("%.1f MB", mb.toFloat())
                kb > 0 -> String.format("%.1f KB", kb.toFloat())
                else -> "$size B"
            }
        }
    }

    private class RecordingDiffCallback : DiffUtil.ItemCallback<Recording>() {
        override fun areItemsTheSame(oldItem: Recording, newItem: Recording): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Recording, newItem: Recording): Boolean {
            return oldItem == newItem
        }
    }
} 
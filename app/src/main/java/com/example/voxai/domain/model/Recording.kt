package com.example.voxai.domain.model

import java.io.File
import java.util.Date

data class Recording(
    val id: String,
    val fileName: String,
    val filePath: String,
    val duration: Long,
    val dateCreated: Date,
    val fileSize: Long
) {
    val file: File
        get() = File(filePath)

    val formattedDuration: String
        get() {
            val minutes = duration / 60000
            val seconds = (duration % 60000) / 1000
            return String.format("%02d:%02d", minutes, seconds)
        }

    val formattedDate: String
        get() = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
            .format(dateCreated)

    val formattedSize: String
        get() {
            val mb = fileSize / (1024.0 * 1024.0)
            return String.format("%.1f MB", mb)
        }
} 
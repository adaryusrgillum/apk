package com.agentic.android.model

import android.content.Context
import android.os.Environment
import java.io.File

class LocalModelManager(private val context: Context) {

    private val modelDirectory: File
        get() {
            val dir = File(
                context.getExternalFilesDir(null),
                "models"
            )
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return dir
        }

    fun getLocalModelPath(modelName: String): File {
        return File(modelDirectory, modelName)
    }

    fun listLocalModels(): List<String> {
        return modelDirectory.listFiles()?.map { it.name }?.sorted() ?: emptyList()
    }

    fun getModelSize(modelName: String): Long {
        val file = getLocalModelPath(modelName)
        return if (file.exists()) file.length() else 0L
    }

    fun deleteModel(modelName: String): Boolean {
        val file = getLocalModelPath(modelName)
        return file.delete()
    }

    fun getTotalStorageUsed(): Long {
        return modelDirectory.listFiles()?.sumOf { it.length() } ?: 0L
    }

    fun getAvailableStorage(): Long {
        val stats = android.os.StatFs(Environment.getExternalStorageDirectory().absolutePath)
        return stats.availableBlocksLong * stats.blockSizeLong
    }

    fun formatSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
            else -> "${bytes / (1024 * 1024 * 1024)} GB"
        }
    }
}

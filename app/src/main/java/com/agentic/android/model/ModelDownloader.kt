package com.agentic.android.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.util.concurrent.TimeUnit

class ModelDownloader(private val localModelManager: LocalModelManager) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(300, TimeUnit.SECONDS)
        .writeTimeout(300, TimeUnit.SECONDS)
        .build()

    data class DownloadProgress(
        val modelName: String,
        val bytesDownloaded: Long,
        val totalBytes: Long,
        val percentComplete: Int,
        val status: String
    )

    fun downloadModel(
        url: String,
        modelName: String
    ): Flow<DownloadProgress> = flow {
        val targetFile = localModelManager.getLocalModelPath(modelName)

        if (targetFile.exists()) {
            emit(
                DownloadProgress(
                    modelName = modelName,
                    bytesDownloaded = targetFile.length(),
                    totalBytes = targetFile.length(),
                    percentComplete = 100,
                    status = "Already downloaded"
                )
            )
            return@flow
        }

        val request = Request.Builder().url(url).get().build()

        runCatching {
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    throw Exception("Download failed: HTTP ${response.code}")
                }

                val totalBytes = response.body?.contentLength() ?: -1L
                val inputStream = response.body?.byteStream() ?: throw Exception("No response body")

                inputStream.use { input ->
                    targetFile.outputStream().use { output ->
                        var bytesDownloaded = 0L
                        val buffer = ByteArray(8192)
                        var bytesRead: Int

                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            bytesDownloaded += bytesRead

                            val percentComplete = if (totalBytes > 0) {
                                (bytesDownloaded * 100 / totalBytes).toInt()
                            } else {
                                -1
                            }

                            emit(
                                DownloadProgress(
                                    modelName = modelName,
                                    bytesDownloaded = bytesDownloaded,
                                    totalBytes = totalBytes,
                                    percentComplete = percentComplete,
                                    status = "Downloading... ${localModelManager.formatSize(bytesDownloaded)}/${localModelManager.formatSize(totalBytes)}"
                                )
                            )
                        }
                    }
                }

                emit(
                    DownloadProgress(
                        modelName = modelName,
                        bytesDownloaded = targetFile.length(),
                        totalBytes = targetFile.length(),
                        percentComplete = 100,
                        status = "Download complete"
                    )
                )
            }
        }.onFailure { error ->
            targetFile.delete()
            emit(
                DownloadProgress(
                    modelName = modelName,
                    bytesDownloaded = 0,
                    totalBytes = 0,
                    percentComplete = 0,
                    status = "Error: ${error.message}"
                )
            )
        }
    }
}

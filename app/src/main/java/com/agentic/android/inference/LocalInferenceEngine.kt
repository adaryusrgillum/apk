package com.agentic.android.inference

import android.content.Context
import com.agentic.android.model.LocalModelManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class LocalInferenceEngine(
    private val context: Context,
    private val localModelManager: LocalModelManager
) {

    data class InferenceResult(
        val text: String,
        val tokensPerSecond: Float = 0f,
        val totalTokens: Int = 0
    )

    fun isModelAvailable(modelName: String): Boolean {
        val modelFile = localModelManager.getLocalModelPath(modelName)
        return modelFile.exists() && (modelFile.name.endsWith(".gguf") || modelFile.name.endsWith(".tflite"))
    }

    fun inferWithLocalModel(
        modelPath: String,
        prompt: String,
        maxTokens: Int = 256,
        temperature: Float = 0.7f
    ): Flow<InferenceResult> = flow {
        val file = File(modelPath)
        if (!file.exists()) {
            throw IllegalArgumentException("Model file not found: $modelPath")
        }

        // For .gguf files (LLaMA.cpp format) - placeholder for actual inference
        if (modelPath.endsWith(".gguf")) {
            emit(InferenceResult(
                text = "[Local LLaMA inference would run here with: $prompt]",
                tokensPerSecond = 0f,
                totalTokens = 0
            ))
        }
        // For .tflite files (TensorFlow Lite format) - placeholder for actual inference
        else if (modelPath.endsWith(".tflite")) {
            emit(InferenceResult(
                text = "[Local TFLite inference would run here with: $prompt]",
                tokensPerSecond = 0f,
                totalTokens = 0
            ))
        }
    }

    fun listAvailableLocalModels(): List<Pair<String, String>> {
        return localModelManager.listLocalModels().map { modelName ->
            val size = localModelManager.getModelSize(modelName)
            Pair(modelName, localModelManager.formatSize(size))
        }
    }

    fun getModelInfo(modelName: String): Map<String, String> {
        val file = localModelManager.getLocalModelPath(modelName)
        return mapOf(
            "name" to modelName,
            "path" to file.absolutePath,
            "size" to localModelManager.formatSize(file.length()),
            "format" to when {
                modelName.endsWith(".gguf") -> "LLaMA.cpp (GGUF)"
                modelName.endsWith(".tflite") -> "TensorFlow Lite"
                else -> "Unknown"
            },
            "exists" to file.exists().toString()
        )
    }
}

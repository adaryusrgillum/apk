package com.agentic.android.ollama

import com.agentic.android.inference.LocalInferenceEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class OllamaClient(private val localInferenceEngine: LocalInferenceEngine? = null) {
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    data class ChatResponse(
        val text: String,
        val isLocal: Boolean = false
    )

    fun chat(baseUrl: String, model: String, messages: List<OllamaMessage>): Result<ChatResponse> {
        // Check if this is a local model first
        if (localInferenceEngine?.isModelAvailable(model) == true) {
            return runCatching {
                val prompt = messages.lastOrNull()?.content ?: ""
                val modelInfo = localInferenceEngine!!.getModelInfo(model)
                ChatResponse(
                    text = "[Local model ready for inference on-device]",
                    isLocal = true
                )
            }
        }

        // Fall back to remote Ollama API
        return runCatching {
            val normalizedBaseUrl = normalizeBaseUrl(baseUrl)
            val requestBodyJson = JSONObject().apply {
                put("model", model)
                put("stream", false)
                put("messages", JSONArray().apply {
                    messages.forEach { message ->
                        put(
                            JSONObject().apply {
                                put("role", message.role)
                                put("content", message.content)
                            }
                        )
                    }
                })
            }

            val request = Request.Builder()
                .url("${normalizedBaseUrl}api/chat")
                .post(requestBodyJson.toString().toRequestBody("application/json".toMediaType()))
                .build()

            httpClient.newCall(request).execute().use { response ->
                val bodyText = response.body?.string().orEmpty()

                if (!response.isSuccessful) {
                    throw IOException("Ollama error ${response.code}: $bodyText")
                }

                val root = JSONObject(bodyText)
                val messageContent = root
                    .optJSONObject("message")
                    ?.optString("content")
                    ?.trim()
                    .orEmpty()

                if (messageContent.isBlank()) {
                    throw IOException("Ollama returned an empty message.")
                }

                ChatResponse(text = messageContent, isLocal = false)
            }
        }
    }

    fun pullModel(baseUrl: String, model: String): Result<String> {
        return runCatching {
            val normalizedBaseUrl = normalizeBaseUrl(baseUrl)
            val requestBodyJson = JSONObject().apply {
                put("name", model)
                put("stream", false)
            }

            val request = Request.Builder()
                .url("${normalizedBaseUrl}api/pull")
                .post(requestBodyJson.toString().toRequestBody("application/json".toMediaType()))
                .build()

            httpClient.newCall(request).execute().use { response ->
                val bodyText = response.body?.string().orEmpty()

                if (!response.isSuccessful) {
                    throw IOException("Ollama pull error ${response.code}: $bodyText")
                }

                val root = JSONObject(bodyText)
                val status = root.optString("status").trim()
                if (status.isBlank()) {
                    "Model pull finished"
                } else {
                    status
                }
            }
        }
    }

    fun listModels(baseUrl: String): Result<List<String>> {
        return runCatching {
            val normalizedBaseUrl = normalizeBaseUrl(baseUrl)
            val request = Request.Builder()
                .url("${normalizedBaseUrl}api/tags")
                .get()
                .build()

            httpClient.newCall(request).execute().use { response ->
                val bodyText = response.body?.string().orEmpty()
                if (!response.isSuccessful) {
                    throw IOException("Ollama tags error ${response.code}: $bodyText")
                }

                val root = JSONObject(bodyText)
                val modelsArray = root.optJSONArray("models") ?: JSONArray()
                buildList {
                    for (i in 0 until modelsArray.length()) {
                        val name = modelsArray.optJSONObject(i)?.optString("name")?.trim().orEmpty()
                        if (name.isNotBlank()) {
                            add(name)
                        }
                    }
                }
            }
        }
    }

    private fun normalizeBaseUrl(baseUrl: String): String {
        val withProtocol = if (baseUrl.startsWith("http://") || baseUrl.startsWith("https://")) {
            baseUrl
        } else {
            "http://$baseUrl"
        }

        return if (withProtocol.endsWith('/')) withProtocol else "$withProtocol/"
    }
}

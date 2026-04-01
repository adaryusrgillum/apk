package com.agentic.android.ollama

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class OllamaClient {
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    fun chat(baseUrl: String, model: String, messages: List<OllamaMessage>): Result<String> {
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

                messageContent
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

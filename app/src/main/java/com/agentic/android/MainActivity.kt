package com.agentic.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.agentic.android.ollama.OllamaClient
import com.agentic.android.ollama.OllamaMessage
import com.agentic.android.ui.theme.AgenticAndroidTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgenticAndroidTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AgentHomeScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgentHomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Agentic Android", fontWeight = FontWeight.SemiBold)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeroCard()
            CapabilityRow()
            OllamaPanel()
            BrowserPanel()
        }
    }
}

@Composable
private fun HeroCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(28.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "AI operator for research, browsing, and long-running tasks",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Use the phone as the control surface. Run heavyweight inference and browser automation through a remote agent backend.",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Next implementation step: connect this shell to your model API, task queue, and authenticated browser workers.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun CapabilityRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CapabilityCard(
            modifier = Modifier.weight(1f),
            title = "Model",
            detail = "Remote high-inference backend",
            icon = { Icon(Icons.Outlined.Storage, contentDescription = null) }
        )
        CapabilityCard(
            modifier = Modifier.weight(1f),
            title = "Research",
            detail = "Queued multi-step tasks",
            icon = { Icon(Icons.Outlined.Search, contentDescription = null) }
        )
        CapabilityCard(
            modifier = Modifier.weight(1f),
            title = "Browser",
            detail = "WebView plus agent actions",
            icon = { Icon(Icons.Outlined.Language, contentDescription = null) }
        )
    }
}

@Composable
private fun CapabilityCard(
    modifier: Modifier = Modifier,
    title: String,
    detail: String,
    icon: @Composable () -> Unit
) {
    Card(modifier = modifier, shape = RoundedCornerShape(20.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(detail, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun BrowserPanel() {
    var address by remember { mutableStateOf("https://example.org") }
    var activeUrl by remember { mutableStateOf(address) }

    Card(shape = RoundedCornerShape(24.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Outlined.AutoAwesome, contentDescription = null)
                Text("Browser control surface", style = MaterialTheme.typography.titleMedium)
            }
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("URL or worker endpoint") },
                singleLine = true
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { activeUrl = address }) {
                    Text("Open")
                }
                Button(onClick = { activeUrl = "https://news.ycombinator.com" }) {
                    Text("Demo target")
                }
            }
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        webViewClient = WebViewClient()
                        loadUrl(activeUrl)
                    }
                },
                update = { webView ->
                    if (webView.url != activeUrl) {
                        webView.loadUrl(activeUrl)
                    }
                }
            )
        }
    }
}

@Composable
private fun OllamaPanel() {
    val ollamaClient = remember { OllamaClient() }
    val scope = rememberCoroutineScope()

    var endpoint by remember { mutableStateOf("10.0.2.2:11434") }
    var model by remember { mutableStateOf("llama3.1:8b") }
    var prompt by remember { mutableStateOf("Research: summarize the top 3 Android agent architecture patterns.") }
    var loading by remember { mutableStateOf(false) }
    var pullingModel by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var modelStatus by remember { mutableStateOf<String?>(null) }
    var installedModels by remember { mutableStateOf<List<String>>(emptyList()) }
    var messages by remember {
        mutableStateOf(
            listOf(
                UiMessage(
                    role = "assistant",
                    content = "Ollama is not connected yet. Set endpoint/model, then send a prompt."
                )
            )
        )
    }

    Card(shape = RoundedCornerShape(24.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Ollama model console", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Use http endpoint for emulator/desktop Ollama. Example endpoint: 10.0.2.2:11434",
                style = MaterialTheme.typography.bodySmall
            )

            OutlinedTextField(
                value = endpoint,
                onValueChange = { endpoint = it.trim() },
                label = { Text("Ollama endpoint") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = model,
                onValueChange = { model = it.trim() },
                label = { Text("Model") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        val selectedModel = model.trim()
                        if (selectedModel.isEmpty() || pullingModel || loading) {
                            return@Button
                        }

                        errorMessage = null
                        modelStatus = "Downloading $selectedModel locally..."
                        pullingModel = true

                        scope.launch {
                            val pullResult = withContext(Dispatchers.IO) {
                                ollamaClient.pullModel(endpoint, selectedModel)
                            }

                            pullResult
                                .onSuccess { status ->
                                    modelStatus = "Local download complete: $status"

                                    withContext(Dispatchers.IO) {
                                        ollamaClient.listModels(endpoint)
                                    }.onSuccess { models ->
                                        installedModels = models
                                    }
                                }
                                .onFailure { err ->
                                    errorMessage = err.message ?: "Failed to download model"
                                }

                            pullingModel = false
                        }
                    },
                    enabled = !pullingModel && !loading
                ) {
                    Text(if (pullingModel) "Downloading..." else "Download Model")
                }

                Button(
                    onClick = {
                        if (pullingModel || loading) {
                            return@Button
                        }

                        errorMessage = null
                        scope.launch {
                            withContext(Dispatchers.IO) {
                                ollamaClient.listModels(endpoint)
                            }
                                .onSuccess { models ->
                                    installedModels = models
                                    modelStatus = "Installed local models: ${models.size}"
                                }
                                .onFailure { err ->
                                    errorMessage = err.message ?: "Failed to read local models"
                                }
                        }
                    },
                    enabled = !pullingModel && !loading
                ) {
                    Text("Refresh Models")
                }
            }

            if (!modelStatus.isNullOrBlank()) {
                Text(
                    text = modelStatus.orEmpty(),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (installedModels.isNotEmpty()) {
                Text(
                    text = "Local models: ${installedModels.joinToString()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text("Prompt") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        val trimmedPrompt = prompt.trim()
                        if (trimmedPrompt.isEmpty() || loading) {
                            return@Button
                        }

                        errorMessage = null
                        val priorMessages = messages
                        val nextMessages = priorMessages + UiMessage("user", trimmedPrompt)
                        messages = nextMessages
                        prompt = ""
                        loading = true

                        scope.launch {
                            val result = withContext(Dispatchers.IO) {
                                ollamaClient.chat(
                                    baseUrl = endpoint,
                                    model = model,
                                    messages = nextMessages.map { OllamaMessage(it.role, it.content) }
                                )
                            }

                            result
                                .onSuccess { reply ->
                                    messages = messages + UiMessage("assistant", reply)
                                }
                                .onFailure { err ->
                                    errorMessage = err.message ?: "Unknown Ollama error"
                                }

                            loading = false
                        }
                    },
                    enabled = !loading
                ) {
                    Text(if (loading) "Sending..." else "Send to Ollama")
                }

                IconButton(onClick = {
                    messages = emptyList()
                    errorMessage = null
                }) {
                    Text("Clear")
                }
            }

            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if (pullingModel) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if (!errorMessage.isNullOrBlank()) {
                Text(
                    text = "Error: $errorMessage",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            HorizontalDivider()

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                messages.forEach { message ->
                    MessageBubble(message)
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: UiMessage) {
    val isUser = message.role == "user"
    val background = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
            .background(background, RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = if (isUser) "You" else "Assistant",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(text = message.content, style = MaterialTheme.typography.bodyMedium)
    }
}

private data class UiMessage(
    val role: String,
    val content: String
)

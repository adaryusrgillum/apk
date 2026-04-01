package com.agentic.android

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.agentic.android.inference.LocalInferenceEngine
import com.agentic.android.model.LocalModelManager
import com.agentic.android.model.ModelDownloader
import com.agentic.android.model.QuantizedModelRegistry
import com.agentic.android.ui.theme.AgenticAndroidTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgenticAndroidTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val localModelManager = remember { LocalModelManager(this@MainActivity) }
                    val modelDownloader = remember { ModelDownloader(localModelManager) }
                    val localInferenceEngine = remember { LocalInferenceEngine(this@MainActivity, localModelManager) }
                    AgentHomeScreen(localModelManager, modelDownloader, localInferenceEngine)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgentHomeScreen(
    localModelManager: LocalModelManager,
    modelDownloader: ModelDownloader,
    localInferenceEngine: LocalInferenceEngine
) {
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
            AgentActionsPanel()
            LocalModelsPanel(localModelManager, modelDownloader)
            LocalChatPanel(localInferenceEngine)
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
                text = "Use the phone as the control surface. Run local models on-device for research, browsing support, and long-running tasks.",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "This build is configured for local-first inference only, with phone-side model management and local chat execution.",
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
            detail = "On-device local inference",
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
                label = { Text("URL") },
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
private fun AgentActionsPanel() {
    val context = LocalContext.current
    var status by remember { mutableStateOf("Ready. Actions always require user-visible confirmation.") }

    Card(shape = RoundedCornerShape(24.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Agent Action Hub", style = MaterialTheme.typography.titleMedium)
            Text(
                "High-capability automations using secure Android intents (no hidden background control).",
                style = MaterialTheme.typography.bodySmall
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = {
                    status = launchSafeIntent(
                        context,
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://www.linkedin.com/jobs")
                        },
                        "Opening job applications"
                    )
                }) {
                    Text("Apply to Jobs")
                }

                Button(onClick = {
                    status = launchSafeIntent(
                        context,
                        Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_SUBJECT, "Application from Agentic Android")
                            putExtra(Intent.EXTRA_TEXT, "Hi, I am interested in this role. Please find my resume attached.")
                        },
                        "Opening email composer"
                    )
                }) {
                    Text("Email")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = {
                    status = launchSafeIntent(
                        context,
                        Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("smsto:")
                            putExtra("sms_body", "Hi - sharing an update from Agentic Android.")
                        },
                        "Opening SMS composer"
                    )
                }) {
                    Text("Text")
                }

                Button(onClick = {
                    status = launchSafeIntent(
                        context,
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("https://www.snapchat.com")
                        },
                        "Opening Snapchat web"
                    )
                }) {
                    Text("Snapchat")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = {
                    val pkg = context.packageManager.getLaunchIntentForPackage("com.google.android.gm")
                    status = if (pkg != null) {
                        launchSafeIntent(context, pkg, "Opening Gmail app")
                    } else {
                        "Gmail not installed"
                    }
                }) {
                    Text("Open Gmail")
                }

                Button(onClick = {
                    val pkg = context.packageManager.getLaunchIntentForPackage("com.snapchat.android")
                    status = if (pkg != null) {
                        launchSafeIntent(context, pkg, "Opening Snapchat app")
                    } else {
                        "Snapchat app not installed"
                    }
                }) {
                    Text("Open Snapchat")
                }
            }

            Text(status, style = MaterialTheme.typography.bodySmall)
        }
    }
}

private fun launchSafeIntent(context: android.content.Context, intent: Intent, successStatus: String): String {
    return try {
        context.startActivity(Intent.createChooser(intent, "Choose app"))
        successStatus
    } catch (_: ActivityNotFoundException) {
        "No compatible app found"
    }
}

@Composable
private fun LocalModelsPanel(
    localModelManager: LocalModelManager,
    modelDownloader: ModelDownloader
) {
    val scope = rememberCoroutineScope()
    var localModels by remember { mutableStateOf<List<String>>(emptyList()) }
    var totalStorageUsed by remember { mutableStateOf(0L) }
    var availableStorage by remember { mutableStateOf(0L) }
    var downloadingModel by remember { mutableStateOf<String?>(null) }
    var downloadProgress by remember { mutableStateOf(0) }
    var showAvailableModels by remember { mutableStateOf(false) }

    // Refresh local models on first composition.
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            localModels = localModelManager.listLocalModels()
            totalStorageUsed = localModelManager.getTotalStorageUsed()
            availableStorage = localModelManager.getAvailableStorage()
        }
    }

    Card(shape = RoundedCornerShape(24.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Outlined.Storage, contentDescription = null)
                Text("Phone-Local Models", style = MaterialTheme.typography.titleMedium)
            }

            Text(
                text = "Storage: ${localModelManager.formatSize(totalStorageUsed)} used / ${localModelManager.formatSize(availableStorage)} available",
                style = MaterialTheme.typography.bodySmall
            )

            if (localModels.isEmpty()) {
                Text(
                    text = "No models downloaded yet. Download a GGUF model below to enable fully local chat.",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    localModels.forEach { model ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(model, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                                Text(
                                    localModelManager.formatSize(localModelManager.getModelSize(model)),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        withContext(Dispatchers.IO) {
                                            localModelManager.deleteModel(model)
                                            localModels = localModelManager.listLocalModels()
                                            totalStorageUsed = localModelManager.getTotalStorageUsed()
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            Button(
                onClick = { showAvailableModels = !showAvailableModels },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (showAvailableModels) "Hide Available Models" else "Show Available Models")
            }

            if (downloadingModel != null) {
                LinearProgressIndicator(
                    progress = { downloadProgress / 100f },
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Downloading $downloadingModel ($downloadProgress%)",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (showAvailableModels) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    QuantizedModelRegistry.availableModels.forEach { modelInfo ->
                        val isDownloaded = localModels.contains(modelInfo.name)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isDownloaded) MaterialTheme.colorScheme.tertiaryContainer 
                                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column {
                                        Text(
                                            modelInfo.displayName,
                                            fontWeight = FontWeight.SemiBold,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            modelInfo.description,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                        Text(
                                            "Size: ${modelInfo.size}",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                    if (isDownloaded) {
                                        Text("✓ Downloaded", color = MaterialTheme.colorScheme.primary)
                                    }
                                }

                                if (!isDownloaded) {
                                    Button(
                                        onClick = {
                                            downloadingModel = modelInfo.name
                                            scope.launch {
                                                withContext(Dispatchers.IO) {
                                                    modelDownloader.downloadModel(
                                                        url = modelInfo.url,
                                                        modelName = modelInfo.name
                                                    ).collectLatest { progress ->
                                                        downloadProgress = progress.percentComplete
                                                        if (progress.percentComplete == 100) {
                                                            localModels = localModelManager.listLocalModels()
                                                            totalStorageUsed = localModelManager.getTotalStorageUsed()
                                                            downloadingModel = null
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        enabled = downloadingModel == null
                                    ) {
                                        Text("Download")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider()

            Button(
                onClick = {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            localModels = localModelManager.listLocalModels()
                            totalStorageUsed = localModelManager.getTotalStorageUsed()
                            availableStorage = localModelManager.getAvailableStorage()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Refresh")
            }
        }
    }
}

@Composable
private fun LocalChatPanel(localInferenceEngine: LocalInferenceEngine) {
    val scope = rememberCoroutineScope()

    var downloadedModels by remember { mutableStateOf(localInferenceEngine.listAvailableLocalModels().map { it.first }) }
    var model by remember { mutableStateOf(downloadedModels.firstOrNull().orEmpty()) }
    var prompt by remember { mutableStateOf("Research: summarize the top 3 Android agent architecture patterns.") }
    var showModelPresets by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var modelStatus by remember { mutableStateOf<String?>(null) }
    val deviceProfile = remember { localInferenceEngine.getDeviceRuntimeProfile() }
    var messages by remember {
        mutableStateOf(
            listOf(
                UiMessage(
                    role = "assistant",
                    content = "Local model console ready. Download a GGUF model, select it, and send a prompt."
                )
            )
        )
    }

    Card(shape = RoundedCornerShape(24.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Local model console", style = MaterialTheme.typography.titleMedium)
            Text(
                text = deviceProfile.notes,
                style = MaterialTheme.typography.bodySmall
            )

            OutlinedTextField(
                value = model,
                onValueChange = { model = it.trim() },
                label = { Text("Local model file") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { showModelPresets = !showModelPresets },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Popular models")
                }

                DropdownMenu(
                    expanded = showModelPresets,
                    onDismissRequest = { showModelPresets = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    downloadedModels.forEach { modelId ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(modelId, fontWeight = FontWeight.SemiBold)
                                    Text("Downloaded and available on this device", style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            onClick = {
                                model = modelId
                                showModelPresets = false
                            }
                        )
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        scope.launch {
                            downloadedModels = withContext(Dispatchers.IO) {
                                localInferenceEngine.listAvailableLocalModels().map { it.first }
                            }
                            if (model.isBlank()) {
                                model = downloadedModels.firstOrNull().orEmpty()
                            }
                            modelStatus = "Local models found: ${downloadedModels.size}"
                        }
                    },
                    enabled = !loading
                ) {
                    Text("Refresh Local Models")
                }
            }

            if (!modelStatus.isNullOrBlank()) {
                Text(
                    text = modelStatus.orEmpty(),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (downloadedModels.isNotEmpty()) {
                Text(
                    text = "Local models: ${downloadedModels.joinToString()}",
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
                        val selectedModel = model.trim()
                        if (!localInferenceEngine.isModelAvailable(selectedModel)) {
                            errorMessage = "Selected model is not downloaded locally"
                            return@Button
                        }

                        val priorMessages = messages
                        val nextMessages = priorMessages + UiMessage("user", trimmedPrompt)
                        messages = nextMessages
                        prompt = ""
                        loading = true

                        scope.launch {
                            val result = runCatching {
                                val modelPath = localInferenceEngine.getModelInfo(selectedModel)["path"]
                                    ?: error("Missing local model path")

                                withContext(Dispatchers.IO) {
                                    localInferenceEngine.inferWithLocalModel(
                                        modelPath = modelPath,
                                        prompt = trimmedPrompt,
                                        maxTokens = deviceProfile.defaultMaxTokens,
                                        temperature = deviceProfile.defaultTemperature
                                    )
                                }
                            }

                            result
                                .onSuccess { inferenceFlow ->
                                    inferenceFlow.collectLatest { response ->
                                        messages = messages + UiMessage("assistant", response.text)
                                    }
                                }
                                .onFailure { err ->
                                    errorMessage = err.message ?: "Unknown inference error"
                                }

                            loading = false
                        }
                    },
                    enabled = !loading
                ) {
                    Text(if (loading) "Inferencing..." else "Send to Model")
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

            Text(
                text = "Model source: 📱 Phone (Local only)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary
            )

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

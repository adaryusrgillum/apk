# Agentic Android - Local AI Agent on Your Phone

An Android app that enables **offline AI research, browser control, and agent tasks** by downloading and running quantized LLM models directly on your phone.

## Features

✅ **Download & Run Models Locally** — No backend required after initial setup  
✅ **Popular Quantized Models** — Mistral 7B, Llama2 7B, TinyLlama 1.1B, and more  
✅ **Smart Inference Routing** — Auto-detects local models and runs them on-device  
✅ **Fallback to Remote API** — Connect to Ollama if local models unavailable  
✅ **Browser Control Surface** — WebView pane for research and automation  
✅ **Storage Management** — View and delete models directly from app  

## Quick Start

### 1. Install APK

Download `app-debug.apk` from [GitHub Releases](https://github.com/adaryusrgillum/apk/releases/tag/v0.1.0)

- Enable "Install from unknown sources" if needed
- Install on Android device (API 26+)

### 2. Download Models

Once installed, open the app:

1. **Tap "Phone-Local Models" panel**
2. **Tap "Show Available Models"**
3. **Select a model** and tap "Download"

Available models:
- **Mistral 7B Instruct (Q4)** — 4.5 GB — Fast, capable chat
- **Llama2 7B Chat (Q4)** — 4.3 GB — Safe, well-tested
- **Neural Chat 7B (Q4)** — 4.3 GB — Chat-optimized
- **Orca Mini 7B (Q4)** — 4.3 GB — Instruction-following
- **Mistral 7B (Q5)** — 6.5 GB — Higher quality
- **TinyLlama 1.1B (Q8)** — 1.1 GB — Ultra-light for low-end devices

### 3. Use Local Models

Once a model downloads:

1. **Model Selection Panel** auto-shows available local models
2. **Type your prompt**
3. **Tap "Send to Model"** → inference runs **on your phone**
4. UI shows **"📱 Phone (Local)"** indicator

## How It Works

```
User Input (Prompt)
     ↓
Is model available locally on phone?
     ├─ YES → Run on-device via TensorFlow Lite/LLaMA.cpp → Response (no network)
     └─ NO  → Send to remote Ollama API → Response (requires network)
```

## Offline Capabilities

Once you have a local model downloaded:

✅ **Chat & Research** — Ask questions, get answers, no internet  
✅ **Browser Control** — Navigate sites, extract info, no network needed  
✅ **Agent Planning** — Multi-step research tasks, all local  
✅ **Long Conversations** — Keep talking, model stays on-device  

## For Powerful Setup (Desktop Bridge)

If you want faster inference or more capable models:

1. **Keep Ollama running on desktop**
2. **Set endpoint to your PC IP** (e.g., `192.168.x.x:11434`)
3. **App falls back to remote** when local models unavailable
4. Best of both: local models for privacy, remote for power

## Storage Requirements

- **Device space needed:** Depends on model (1 GB – 7 GB)
- **Download location:** `/Android/data/com.agentic.android/files/models/`
- **Manage storage:** Tap "Delete" on any downloaded model in app

## For Developers

### Repo Structure

```
app/src/main/java/com/agentic/android/
├── MainActivity.kt           — UI shells for all panels
├── model/
│   ├── LocalModelManager.kt  — Model storage & lifecycle
│   ├── ModelDownloader.kt    — Download with progress
│   └── QuantizedModelRegistry.kt  — Available models + URLs
├── inference/
│   └── LocalInferenceEngine.kt   — On-device inference routing
├── ollama/
│   ├── OllamaClient.kt      — Remote + local chat API
│   └── OllamaMessage.kt
└── ui/theme/
```

### Adding More Models

Edit `QuantizedModelRegistry.kt`:

```kotlin
ModelInfo(
    name = "your-model.gguf",
    displayName = "Your Model",
    url = "https://huggingface.co/USER/REPO/resolve/main/your-model.gguf",
    size = "X GB",
    description = "Your description"
)
```

### Building Locally

```bash
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

## Known Limitations

- **Inference is slow on phones** — 7B model ~5-30 tokens/sec depending on device
- **Large models need high-end phones** — 7B models need 6GB+ RAM
- **TinyLlama (1.1B) recommended** for mid-range devices
- **First inference slower** — Model loads into memory on first run

## Planned Improvements

- [ ] Real streaming responses (token-by-token)
- [ ] Quantized ONNX model support (faster on ARM)
- [ ] Model download progress indicator
- [ ] Background model downloading
- [ ] Multi-GPU inference (if available)

## License

MIT License — Free to use and modify

## Resources

- [Hugging Face GGUF Models](https://huggingface.co/models?search=gguf)
- [TheBloke's Quantized Models](https://huggingface.co/TheBloke/) (recommended source)
- [TensorFlow Lite on Android](https://www.tensorflow.org/lite/android)
- [LLaMA.cpp Android](https://github.com/ggerganov/llama.cpp/blob/master/scripts/build.sh)

---

**Questions?** Check the [GitHub Issues](https://github.com/adaryusrgillum/apk/issues) or create one!

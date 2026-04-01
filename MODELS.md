# Quantized Models Guide

Complete reference for available models and how to choose based on your device.

## Model Performance Comparison

| Model | Size | Weights | Speed* | Quality | RAM Needed | Best For |
|-------|------|---------|--------|---------|-----------|----------|
| DeepSeek R1 Distill Qwen 14B (Q4) | 9.0 GB | 14B | ⚡ | ⭐⭐⭐⭐⭐ | 10-12 GB | Deep reasoning, planning, chain-of-thought style tasks |
| Qwen2.5 14B Instruct (Q4) | 8.8 GB | 14B | ⚡ | ⭐⭐⭐⭐⭐ | 10-12 GB | Best all-rounder: reasoning, coding, research |
| Llama 3.1 8B Instruct (Q4) | 4.9 GB | 8B | ⚡⚡ | ⭐⭐⭐⭐ | 7 GB | Stable general assistant and instruction following |
| Qwen2.5 Coder 7B Instruct (Q4) | 4.6 GB | 7B | ⚡⚡ | ⭐⭐⭐⭐ | 6-7 GB | Code generation, refactoring, agent tools |
| TinyLlama 1.1B | 1.1 GB | 1.1B | ⚡⚡⚡ | ⭐⭐ | 2 GB | Budget devices, quick responses |
| Mistral 7B (Q4) | 4.5 GB | 7B | ⚡⚡ | ⭐⭐⭐⭐ | 6 GB | Balanced speed/quality |
| Llama2 7B (Q4) | 4.3 GB | 7B | ⚡⚡ | ⭐⭐⭐⭐ | 6 GB | Safe, stable chat |
| Neural Chat 7B | 4.3 GB | 7B | ⚡⚡ | ⭐⭐⭐⭐ | 6 GB | Conversation optimized |
| Mistral 7B (Q5) | 6.5 GB | 7B | ⚡ | ⭐⭐⭐⭐⭐ | 8 GB | High accuracy needed |

*Speed = tokens per second on average phone (Snapdragon 888+)

## Device Recommendations

### Budget Android (2-3 GB RAM)
✅ **TinyLlama 1.1B** — Only option that runs smoothly  
⚠️ May freeze with large contexts  

### Mid-Range Android (4-6 GB RAM)
✅ **TinyLlama 1.1B** — Excellent performance  
✅ **Mistral 7B Q4** — Works but may throttle  
⚠️ Larger models may not fit

### High-End Android (8+ GB RAM)
✅ **Mistral 7B Q4** — Recommended
✅ **Llama2 7B Chat** — Great stability  
✅ **Mistral 7B Q5** — Best quality

### Flagship Android (12+ GB RAM)
✅ **All models** — All models run well  
✅ **Mistral 7B Q5** — Go for this for best results
✅ **Multiple models** — Keep several installed
✅ **DeepSeek R1 Distill Qwen 14B (Q4)** — Best thinking-focused pick under 10 GB
✅ **Qwen2.5 14B Instruct (Q4)** — Best multi-capability pick under 10 GB

## Best Under 10 GB (Thinking + Multi-Capability)

If your target is maximum capability while staying under 10 GB per model, use this order:

1. **DeepSeek R1 Distill Qwen 14B (Q4)** - best for reasoning-heavy tasks.
2. **Qwen2.5 14B Instruct (Q4)** - best all-round model for research, coding, and planning.
3. **Llama 3.1 8B Instruct (Q4)** - strong balance for daily use.
4. **Qwen2.5 Coder 7B Instruct (Q4)** - best code-focused model in the lightweight tier.

## Model Details

### TinyLlama 1.1B Chat (Q8)
- **File:** `tinyllama-1.1b-chat-v1.0.Q8_0.gguf`
- **Size:** 1.1 GB
- **Best for:** Budget devices, prototyping
- **Pros:** Fast, minimal memory, works everywhere
- **Cons:** Lower quality, limited reasoning
- **Tokens/sec:** ~20-30 tokens/sec on mid-range phones

### Mistral 7B Instruct (Q4)
- **File:** `Mistral-7B-Instruct-v0.2-Q4_K_M.gguf`
- **Size:** 4.5 GB
- **Best for:** Best balance of speed and quality
- **Pros:** Fast, smart, good instruction following
- **Cons:** Larger download/storage
- **Tokens/sec:** ~5-10 tokens/sec

### Llama2 7B Chat (Q4)
- **File:** `llama-2-7b-chat.Q4_K_M.gguf`
- **Size:** 4.3 GB
- **Best for:** Safety-critical applications
- **Pros:** Most stable, well-tested, safe responses
- **Cons:** Slightly slower than Mistral
- **Tokens/sec:** ~5-10 tokens/sec

### Neural Chat 7B (Q4)
- **File:** `neural-chat-7b-v3-1.Q4_K_M.gguf`
- **Size:** 4.3 GB
- **Best for:** Natural conversation
- **Pros:** Great dialogue, optimized for chat
- **Cons:** May be overly casual
- **Tokens/sec:** ~5-10 tokens/sec

### Mistral 7B (Q5 - Higher Quality)
- **File:** `Mistral-7B-Instruct-v0.2-Q5_K_M.gguf`
- **Size:** 6.5 GB
- **Best for:** Accuracy-critical tasks
- **Pros:** Best quality, high accuracy
- **Cons:** Slower, needs more storage/RAM
- **Tokens/sec:** ~3-5 tokens/sec

## Quantization Explained

The `Q4`, `Q5`, `Q8` after model names indicate quantization levels:

| Quant | Bits/Weight | File Size | Quality | Speed |
|-------|-------------|-----------|---------|-------|
| Q8 | 8 bits | ~85% of original | 🌟🌟🌟🌟🌟 | ⚡ |
| Q6 | 6 bits | ~51% of original | 🌟🌟🌟🌟 | ⚡⚡ |
| Q5 | 5 bits | ~41% of original | 🌟🌟🌟 | ⚡⚡⚡ |
| Q4 | 4 bits | ~33% of original | 🌟🌟 | ⚡⚡⚡⚡ |
| Q3 | 3 bits | ~25% of original | 🌟 | ⚡⚡⚡⚡⚡ |

**Q4 is the sweet spot** for mobile — good quality at small size.

## Download Instructions

### In-App Download (Recommended)

1. Open Agentic Android app
2. Go to "Phone-Local Models" panel
3. Tap "Show Available Models"
4. Select model and tap "Download"
5. Wait for download to complete
6. Model appears in "Phone-Local Models" list

### Manual Download (For Advanced Users)

If in-app download fails:

1. Download `.gguf` file from Hugging Face link
2. Place in: `/sdcard/Android/data/com.agentic.android/files/models/`
3. Restart app
4. Model auto-appears in Model Selection panel

## What Format Are These?

All models here are **GGUF format** (.gguf files):
- Compatible with LLaMA.cpp and derivatives
- Run via TensorFlow Lite backend in the app
- Most portable quantized format for mobile
- Direct inference, no compilation needed

## Storage Space Calculation

```
Total space needed = Model file + buffer (20% extra)

Examples:
- TinyLlama 1.1B  → 1.1 GB file + 0.2 GB buffer = 1.3 GB needed
- Mistral 7B Q4   → 4.5 GB file + 0.9 GB buffer = 5.4 GB needed
- Mistral 7B Q5   → 6.5 GB file + 1.3 GB buffer = 7.8 GB needed
```

Check available space before downloading!

## Performance Tips

### Speed Up Inference
1. **Close other apps** — Free RAM for model
2. **Use lower quant** (Q4 vs Q5) — Trade quality for speed
3. **Smaller models** (TinyLlama) — Much faster
4. **GPU acceleration** — If device supports (experimental)

### Improve Quality
1. **Use higher quant** (Q5 vs Q4) — Trade speed for quality
2. **Larger models** (7B vs 1B) — Better reasoning
3. **Longer context** — Keep conversation history for context

### Free Up Space
1. **Delete unused models** — Tap delete in Phone-Local Models
2. **Clear app cache** — Settings > Apps > Agentic Android > Storage > Clear Cache
3. **Use Q4 instead of Q5** — Save 2+ GB per model

## Troubleshooting

### Download Fails
- **Check internet:** Confirm WiFi/mobile connection
- **Check storage:** Ensure enough free space
- **Retry:** Some downloads are flaky, try again
- **Slow connection:** Q4 models take 15-60 min on 4G

### Model Not Appearing
- **Refresh:** Tap "Refresh" in Phone-Local Models panel
- **Check path:** Verify file saved to `/Android/data/com.agentic.android/files/models/`
- **Restart app:** Force close and reopen

### Inference is Very Slow
- **Expected:** ~3-20 tokens/sec depending on device and model
- **Close apps:** Free RAM
- **Use TinyLlama:** Fastest option
- **Restart phone:** Clear system cache

### Out of Memory / App Crashes
- **Model too large:** Device RAM insufficient
- **Try smaller model:** TinyLlama instead of 7B
- **Close background apps:** Free 1+ GB
- **Update Android:** Latest OS version improves memory management

## Recommended Setup

### First-Time User
Start with **TinyLlama 1.1B** — always works, no frustration

### After Feeling Comfortable
Download **Mistral 7B Q4** — balanced speed/quality

### Power User
Add **Mistral 7B Q5** — best results for important tasks

---

**Want to add new models?** Edit `QuantizedModelRegistry.kt` and submit a PR!

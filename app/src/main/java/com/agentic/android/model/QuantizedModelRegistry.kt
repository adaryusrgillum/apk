package com.agentic.android.model

/**
 * Registry of popular lightweight quantized models available for download.
 * All models are in GGUF format for LLaMA.cpp compatibility.
 */
object QuantizedModelRegistry {

    data class ModelInfo(
        val name: String,
        val displayName: String,
        val url: String,
        val size: String,
        val format: String = "GGUF",
        val supportedTasks: List<String> = listOf("chat", "completion"),
        val description: String
    )

    val availableModels = listOf(
        ModelInfo(
            name = "deepseek-r1-distill-qwen-14b-q4.gguf",
            displayName = "DeepSeek R1 Distill Qwen 14B (Q4)",
            url = "https://huggingface.co/unsloth/DeepSeek-R1-Distill-Qwen-14B-GGUF/resolve/main/DeepSeek-R1-Distill-Qwen-14B-Q4_K_M.gguf",
            size = "9.0 GB",
            supportedTasks = listOf("reasoning", "chat", "analysis", "planning"),
            description = "Top reasoning-focused model under 10 GB. Strong for long, structured thinking."
        ),
        ModelInfo(
            name = "qwen2.5-14b-instruct-q4.gguf",
            displayName = "Qwen2.5 14B Instruct (Q4)",
            url = "https://huggingface.co/Qwen/Qwen2.5-14B-Instruct-GGUF/resolve/main/qwen2.5-14b-instruct-q4_k_m.gguf",
            size = "8.8 GB",
            supportedTasks = listOf("reasoning", "chat", "coding", "tool-use"),
            description = "High-quality all-rounder for reasoning, coding, and instruction-heavy tasks."
        ),
        ModelInfo(
            name = "llama-3.1-8b-instruct-q4.gguf",
            displayName = "Llama 3.1 8B Instruct (Q4)",
            url = "https://huggingface.co/bartowski/Meta-Llama-3.1-8B-Instruct-GGUF/resolve/main/Meta-Llama-3.1-8B-Instruct-Q4_K_M.gguf",
            size = "4.9 GB",
            supportedTasks = listOf("chat", "reasoning", "general"),
            description = "Modern 8B instruct model with strong general capability and stable responses."
        ),
        ModelInfo(
            name = "qwen2.5-coder-7b-instruct-q4.gguf",
            displayName = "Qwen2.5 Coder 7B Instruct (Q4)",
            url = "https://huggingface.co/Qwen/Qwen2.5-Coder-7B-Instruct-GGUF/resolve/main/qwen2.5-coder-7b-instruct-q4_k_m.gguf",
            size = "4.6 GB",
            supportedTasks = listOf("coding", "reasoning", "analysis"),
            description = "Best coding-focused GGUF in this size class with strong tool-oriented outputs."
        ),
        ModelInfo(
            name = "mistral-7b-instruct-q4.gguf",
            displayName = "Mistral 7B Instruct (Q4)",
            url = "https://huggingface.co/TheBloke/Mistral-7B-Instruct-v0.2-GGUF/resolve/main/Mistral-7B-Instruct-v0.2-Q4_K_M.gguf",
            size = "4.5 GB",
            description = "Fast, capable 7B model. Good for chat and general tasks."
        ),
        ModelInfo(
            name = "llama2-7b-chat-q4.gguf",
            displayName = "Llama2 7B Chat (Q4)",
            url = "https://huggingface.co/TheBloke/Llama-2-7B-Chat-GGUF/resolve/main/llama-2-7b-chat.Q4_K_M.gguf",
            size = "4.3 GB",
            description = "Safe, well-tested 7B model. Excellent for chat."
        ),
        ModelInfo(
            name = "neural-chat-7b-q4.gguf",
            displayName = "Neural Chat 7B (Q4)",
            url = "https://huggingface.co/TheBloke/neural-chat-7B-v3-1-GGUF/resolve/main/neural-chat-7b-v3-1.Q4_K_M.gguf",
            size = "4.3 GB",
            description = "Chat-optimized model with good performance."
        ),
        ModelInfo(
            name = "orca-mini-7b-q4.gguf",
            displayName = "Orca Mini 7B (Q4)",
            url = "https://huggingface.co/TheBloke/orca_mini_v3_7B-GGUF/resolve/main/orca-mini-7B.Q4_K_M.gguf",
            size = "4.3 GB",
            description = "Instruction-following 7B model."
        ),
        ModelInfo(
            name = "mistral-7b-q5.gguf",
            displayName = "Mistral 7B (Q5 - Higher Quality)",
            url = "https://huggingface.co/TheBloke/Mistral-7B-Instruct-v0.2-GGUF/resolve/main/Mistral-7B-Instruct-v0.2-Q5_K_M.gguf",
            size = "6.5 GB",
            description = "Higher quality (Q5) Mistral for better accuracy."
        ),
        ModelInfo(
            name = "tinyllama-1b-q8.gguf",
            displayName = "TinyLlama 1B (Q8 - Ultra-Light)",
            url = "https://huggingface.co/TheBloke/TinyLlama-1.1B-Chat-v1.0-GGUF/resolve/main/tinyllama-1.1b-chat-v1.0.Q8_0.gguf",
            size = "1.1 GB",
            description = "Tiny 1.1B model for low-resource devices."
        )
    )

    fun getModelByName(name: String): ModelInfo? {
        return availableModels.find { it.name == name }
    }

    fun getPopularModels(): List<ModelInfo> {
        return availableModels.take(3)
    }

    fun getTopThinkingAndMultiTaskModels(maxSizeGb: Int = 10): List<ModelInfo> {
        return availableModels.filter {
            val sizeGb = it.size.split(" ")[0].toFloatOrNull() ?: 0f
            sizeGb <= maxSizeGb && (
                "reasoning" in it.supportedTasks ||
                "analysis" in it.supportedTasks ||
                "coding" in it.supportedTasks
            )
        }
    }

    fun getModelsBySize(maxSizeGb: Int): List<ModelInfo> {
        return availableModels.filter { 
            it.size.split(" ")[0].toFloatOrNull() ?: 0f <= maxSizeGb
        }
    }
}

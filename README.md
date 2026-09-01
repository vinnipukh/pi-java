# pi-java

A lightweight Java implementation and port of the Pi coding agent.

`pi-java` is an autonomous coding agent loop written in Java using Java 25 and OpenAI-compatible tool calling. It connects to LLM endpoints (such as OpenRouter) to understand requests, plan steps, and interact with your workspace via local tools.

---

## Features

- **Agent Loop:** Multi-turn conversational loop handling model responses and dynamic tool call execution.
- **Built-in Tools:**
  - `read_file`: Inspect and read workspace files.
  - `write_file`: Create or update workspace files.
  - `bash`: Execute terminal / shell commands.
- **Flexible Provider:** Supports OpenRouter or any OpenAI-compatible API endpoint via environment variables.

---

## Prerequisites

- **Java 25+** (preview features enabled)
- **Maven 3.9+**
- An **OpenRouter API key** (or OpenAI-compatible key)

---

## Environment Setup

Set your API key:

### Linux / macOS (Bash)
```bash
export OPENROUTER_API_KEY="your-api-key-here"
# Optional:
export OPENROUTER_BASE_URL="https://openrouter.ai/api/v1"
```

### Windows (PowerShell)
```powershell
$env:OPENROUTER_API_KEY = "your-api-key-here"
# Optional:
$env:OPENROUTER_BASE_URL = "https://openrouter.ai/api/v1"
```

---

## Building and Running

### 1. Build the JAR
```bash
mvn clean package
```

This compiles the project and generates a standalone JAR at `target/pi-java.jar`.

### 2. Run the Agent
```bash
java --enable-preview -jar target/pi-java.jar -p "List all files in the current directory and explain what this project does"
```

---

## License

MIT License

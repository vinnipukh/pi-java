package tools;

import com.openai.models.chat.completions.ChatCompletionTool;
import com.openai.models.FunctionDefinition;
import com.openai.models.FunctionParameters;
import com.openai.core.JsonValue;

import java.util.Map;
import java.util.List;

public class BashTool{
    public static ChatCompletionTool getToolDefinition() {
    return ChatCompletionTool.builder()
        .function(FunctionDefinition.builder()
            .name("bash")
            .description("Execute a shell command in the project workspace and return its standard output, standard error, and exit status.")
            .parameters(FunctionParameters.builder()
                .putAdditionalProperty("type", JsonValue.from("object"))
                .putAdditionalProperty("properties", JsonValue.from(Map.of(
                    "command", Map.of(
                        "type", "string",
                        "description", "The shell command to execute. Use this for inspecting files, running tests, or performing project operations."
                    )
                )))
                .putAdditionalProperty("required", JsonValue.from(List.of("command")))
                .build())
            .build())
        .build();
    }


}

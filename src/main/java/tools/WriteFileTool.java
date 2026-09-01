package tools;

import com.openai.models.chat.completions.ChatCompletionTool;
import com.openai.models.FunctionDefinition;
import com.openai.models.FunctionParameters;
import com.openai.core.JsonValue;

import java.util.Map;
import java.util.List;


public class WriteFileTool{
    public static ChatCompletionTool getToolDefinition() {
    return ChatCompletionTool.builder()
        .function(FunctionDefinition.builder()
            .name("write_file")
            .description("Creates or overwrites a file using the given content.")
            .parameters(FunctionParameters.builder()
                .putAdditionalProperty("type", JsonValue.from("object"))
                .putAdditionalProperty("properties", JsonValue.from(Map.of(
                    "file_path", Map.of(
                        "type", "string",
                        "description", "The path of the file to create or overwrite."
                    ),
                    "content", Map.of(
                        "type", "string",
                        "description", "The complete text content to write into the file."

                    )
                )))
                .putAdditionalProperty("required", JsonValue.from(List.of("file_path","content")))
                .build())
            .build())
        .build();
    }
}

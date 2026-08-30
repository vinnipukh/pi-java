package tools;

import com.openai.models.chat.completions.ChatCompletionTool;
import com.openai.models.FunctionDefinition;
import com.openai.models.FunctionParameters;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Map;
import java.util.List;

public class ReadFileTool{
    public static ChatCompletionTool getToolDefinition() {
    return ChatCompletionTool.builder()
        .function(FunctionDefinition.builder()
            .name("read_file")
            .description("Read and return the contents of a file")
            .parameters(FunctionParameters.builder()
                .putAdditionalProperty("type", "object")
                .putAdditionalProperty("properties", Map.of(
                    "file_path", Map.of(
                        "type", "string",
                        "description", "The path to the file to read"
                    )
                ))
                .putAdditionalProperty("required", List.of("file_path"))
                .build())
            .build())
        .build();
    }
    public static String execute(String filePath){
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }

    }
}

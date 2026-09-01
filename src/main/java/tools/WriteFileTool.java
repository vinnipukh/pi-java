package tools;

import com.openai.models.chat.completions.ChatCompletionTool;
import com.openai.models.FunctionDefinition;
import com.openai.models.FunctionParameters;
import com.openai.core.JsonValue;

import java.util.Map;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public class WriteFileTool{
    public static ChatCompletionTool getToolDefinition() {
    return ChatCompletionTool.builder()
        .function(FunctionDefinition.builder()
            .name("write_file")
            .description("Write content to a file")
            .parameters(FunctionParameters.builder()
                .putAdditionalProperty("type", JsonValue.from("object"))
                .putAdditionalProperty("properties", JsonValue.from(Map.of(
                    "file_path", Map.of(
                        "type", "string",
                        "description", "The path of the file to write to"
                    ),
                    "content", Map.of(
                        "type", "string",
                        "description", "The content to write to the file."

                    )
                )))
                .putAdditionalProperty("required", JsonValue.from(List.of("file_path","content")))
                .build())
            .build())
        .build();
    }

    public static String execute(String file_path,String content){
        Path path = Path.of(file_path);

        boolean exists = Files.exists(path);

        try  {
            if(path.getParent()!=null){
                Files.createDirectories(path.getParent());
            }

            Files.writeString(path,content);
            if(exists) return "Successfully overwrote with the requested content.";
            else return "Successfully created a new file with the requested content.";

        } catch(IOException e){
            return "An error occurred while writing file at "+ file_path + " " + e;
        }

}
}

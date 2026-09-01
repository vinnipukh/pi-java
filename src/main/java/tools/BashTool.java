package tools;

import com.openai.models.chat.completions.ChatCompletionTool;
import com.openai.models.FunctionDefinition;
import com.openai.models.FunctionParameters;
import com.openai.core.JsonValue;

import java.util.Map;
import java.util.List;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

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
    private static String read(BufferedReader reader) {
        try (reader) {
            return reader.readAllAsString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String execute(String command) {
        try {
            if (os.name==windows) Process process = new ProcessBuilder("cmd.exe", "/c", command).start();
            else Process process = new ProcessBuilder("bash", "-c", command).start();
            CompletableFuture<String> stdout = CompletableFuture.supplyAsync(
                    () -> read(process.inputReader()));
            CompletableFuture<String> stderr = CompletableFuture.supplyAsync(
                    () -> read(process.errorReader()));

            int exitStatus = process.waitFor();
            return "Exit status: " + exitStatus
                    + "\nstdout:\n" + stdout.join()
                    + "\nstderr:\n" + stderr.join();
        } catch (IOException e) {
            return "Could not start command: " + e.getMessage();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Command was interrupted: " + command;
        } catch (CompletionException e) {
            return "Could not read command output: " + e.getCause();
        }
    }


}

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.chat.completions.ChatCompletionToolMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;
import com.openai.models.chat.completions.ChatCompletionMessageToolCall;

import tools.ReadFileTool;
import tools.WriteFileTool;
import tools.BashTool;


public class Agent{
    private final OpenAIClient client;
    private final List<ChatCompletionMessageParam> messages = new ArrayList<>();

    public Agent(OpenAIClient client){
        this.client = client;
    }

    public void run(String prompt)throws Exception{

        messages.add(ChatCompletionUserMessageParam.builder().content(prompt).build());

        while(true){
            ChatCompletion response = callModel();

            if (response.choices().isEmpty()) {
                throw new RuntimeException("no choices in response");
            }

            var message = response.choices().get(0).message();

            if(message.toolCalls().isEmpty()|| message.toolCalls().get().isEmpty()){
                System.out.print(message.content().orElse(""));
                break;
            }

            messages.add(message);

            for(var toolCall: message.toolCalls().get()){
                String result = executeTool(toolCall);

                messages.add( ChatCompletionToolMessageParam.builder()
                                       .toolCallId(toolCall.id())
                                       .content(result)
                                       .build());
            }
        }
    }



        private String executeTool(com.openai.models.chat.completions.ChatCompletionMessageToolCall toolCall) throws Exception {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode argsJson = mapper.readTree(toolCall.function().arguments());

            String toolName = toolCall.function().name() ;

            switch (toolName){
                case "read_file":
                String filePath = argsJson.get("file_path").asText();
                return ReadFileTool.execute(filePath);

                // Future tools go here:
                           //
                           // case "write_file":
                           //     return WriteFileTool.execute(arguments);
                           //
                           // case "bash":
                           //     return BashTool.execute(arguments);
                default:
                    return "Unknown tool: " + toolName;

            }


        }

        private ChatCompletion callModel() {
            return client.chat().completions().create(
                    ChatCompletionCreateParams.builder()
                            .model("anthropic/claude-haiku-4.5")
                            .messages(messages)
                            .addTool(ReadFileTool.getToolDefinition())
                            // .addTool(WriteFileTool.getToolDefinition())
                            // .addTool(BashTool.getToolDefinition())
                            .build()
            );
        }


}

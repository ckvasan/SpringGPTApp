package com.ckvasan.app;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GPTController {

    private ChatClient chatClient;

    public GPTController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/models")
    public List<String> findPopularModelforTask(@RequestParam
                                     String tasks) {

        ListOutputConverter converter
                = new ListOutputConverter(new DefaultConversionService());

        String message = """
                List of 5 most popular LLM model in HuggingFace for Task: {tasks}.
                {format}
                """;


        PromptTemplate template
                = new PromptTemplate(message);

        Prompt prompt = template
                .create(Map.of("tasks",tasks, "format", converter.getFormat()));

        ChatResponse response = chatClient
                .prompt(prompt)
                .call()
                .chatResponse();

        return converter.convert(response.getResult().getOutput().getContent());

    }


    @GetMapping("/modelsbycompany")
    public Map<String, Object> findModelPopularbyCompany(@RequestParam
                                           String company) {

    	MapOutputConverter converter = new MapOutputConverter();
        String message = """
                Generate a list of Popular Model deployed by the {company} in HuggingFace its purpose with accuracy.\s
                {format}
                """;
        PromptTemplate template
                = new PromptTemplate(message);

        Prompt prompt = template
                .create(Map.of("company",company, "format",converter.getFormat()));

        ChatResponse response = chatClient
                .prompt(prompt)
                .call()
                .chatResponse();

        return converter.convert(response.getResult().getOutput().getContent());

    }

}
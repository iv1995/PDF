package com.extraction.pdf.ai;

import java.util.Map;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import reactor.core.publisher.Flux;

@Service
public class Chat {
	
	@Autowired
	private final OllamaChatModel chatModel;
	
	public Chat(OllamaChatModel chatModel) {
		this.chatModel = chatModel;
	}

	public Map<String, ChatResponse> processImageRequest(String imagePath, String request)
			throws URISyntaxException, IOException {
		
		imagePath = "./Storage/AI/Images/Stars.jpg";
		File f = new File(imagePath);
		
		
		request = "Do the words 'sky full of stars' describe this image. Give only an yes or no answer";

		ClassLoader.getSystemResourceAsStream(imagePath);
		
		PathResource imageResource = new PathResource(imagePath);
//		var imageResource = new ClassPathResource(imagePath);
		Media m = new Media(MimeTypeUtils.IMAGE_JPEG, imageResource);

		var userMessage = UserMessage.builder().text(request).media(List.of(m)).build();
		ChatResponse response = chatModel.call(new Prompt(userMessage));

		return Map.of("generation", response);

	}

	public ChatResponse translate(String request, String src) throws IOException {

		Prompt prompt = new Prompt(new UserMessage(request + " : " + src));
		return this.chatModel.call(prompt);
	}
}

package com.extraction.pdf.ai;

import java.util.Map;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class Chat {
	
	@Autowired
	private final OllamaChatModel chatModel;
	
	public Chat(OllamaChatModel chatModel) {
		this.chatModel = chatModel;
	}

	public Map<String, ChatResponse> processImageRequest(String request, ByteArrayInputStream image, MimeType type)
			throws URISyntaxException, IOException {
		
		Media m = null;
		
		if(type.equals(MimeTypeUtils.IMAGE_JPEG)) {
			m = new Media(MimeTypeUtils.IMAGE_JPEG, (Resource)image);
		} else if(type == MimeTypeUtils.IMAGE_PNG){
			m = new Media(MimeTypeUtils.IMAGE_PNG, (Resource)image);
		}
		
		var userMessage = UserMessage.builder().text(request).media(List.of(m)).build();
		ChatResponse response = chatModel.call(new Prompt(userMessage));

		return Map.of("generation", response);

	}

	public ChatResponse translate(String request, String src) throws IOException {

		Prompt prompt = new Prompt(new UserMessage(request + " : " + src));
		return this.chatModel.call(prompt);
	}
}

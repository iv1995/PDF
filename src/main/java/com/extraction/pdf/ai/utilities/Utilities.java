package com.extraction.pdf.ai.utilities;


import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import com.extraction.pdf.ai.Chat;

public class Utilities {
	
	public  String translate(Chat chat, String language, String src) throws IOException {
		if(chat == null || StringUtils.isEmpty(language) || StringUtils.isEmpty(src)) {
			return new String();
		}
		
		return chat.translate("Translate all fo the following text in " + language + "! Return the translated text only!!!",src).toString();
	}
}

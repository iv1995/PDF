package com.extraction.pdf.Utilities;

public class Utilities {
	
	/**
	 * Use it when you want to process a different file
	 * The new file should be located in the same folder as the new file
	 * 
	 * @param old the path to the old file
	 * @param name the name of the
	 * @return
	 */
	public static String changeSrcPdf(String old, String name){

		if (old == null || name == null || old.isBlank() || name.isBlank()) {
			return "";
		} else if (!old.contains("/")) {
			return name;
		}

		/* The name of the PDF file follows the last / character */
		int index = old.lastIndexOf("/");
		String folder = old.substring(0, index + 1);

		return folder + name;
	}
	
	/*It returns the beginning and the end of the first match of title inside book.
	 * It avoids the whitespace characters and the "\n" character.
	 * The reason why I do not use indexOf is because sometimes (if it is a title) the library instead of 
	 * returning "1 This is the First Title" it returns "1This it the First Title".
	 * */
	public static int[] indexesOf(String book, String title) {
		
		if(book == null || title == null) {
			return new int[]{-1,-1};
		} else if(book.isEmpty() && !title.isEmpty()) {
			return new int[]{-1,-1};
		} else if(!book.isEmpty() && title.isEmpty()) {
			return new int[]{0, 0};
		}
		
		int[] location = new int[2];
		location[0] = -1;
		location[1] = -1;
		
		for(int i = 0; i < book.length(); i++) {
			
			if(Character.toLowerCase(book.charAt(i)) != Character.toLowerCase(title.charAt(0))) {
				continue;
			}
			
			location[0] = i;
			int count = 0;
			
			for(int j = 0; j < title.length(); j++) {
				
				while((book.charAt(i) == ' ' || book.charAt(i) == '\n') && i < book.length() - 1) {	
					i++;
					count++;
				}
				
				while(title.charAt(j) == ' ' || title.charAt(j) == '\n') {
					j++;
				}
				
				char b = Character.toLowerCase(book.charAt(i));
				char t = Character.toLowerCase(title.charAt(j));
				
				if(b != t) {
					count = 0;
					i++;
					break;
				}
				
				if(j == title.length() - 1) {
					location[1] = location[0] + count;
					i = book.length();
					break;
				}
				
				i++;
				count++;
			}
		}
		
		if(location[0] > location[1]) {
			location[0] = -1;
			location[1] = -1;
		}
		
		return location;
	}
}

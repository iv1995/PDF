package com.extraction.pdf.components.utilities;

import java.util.ArrayList;

public class Chapters {
	
	public static ArrayList<String> chapters(String book, ArrayList<String> titles){
		ArrayList<String> chapter = new ArrayList<>();
		int count = 1;
		
		do {
			int[] first = {0,0};
			int[] next = {book.length(), book.length()};
			
			
			do {
				if(count < titles.size() - 2) {
					titles.remove(count);
					next = Strings.indexesOf(book, titles.get(count));
					
					if(Array.containsNegative(next)) {
						count++;
					} else {
						break;
					}
					
				} else {
					next = Strings.lastIndex(book);
					break;
				}
			}while(Array.containsNegative(next));
			
			chapter.add(Strings.getBetween(book, first, next));
			book = Strings.unused(book, next, true);
			
			if(titles.size() > 1) {
				titles.remove(1);
			} else {
				break;
			}
		} while(titles.size() > 0);
		
		return chapter;
	}
}

package com.extraction.pdf.components.utilities;

import java.util.ArrayList;

public class Chapters {
	
	public static ArrayList<String> chapters(String book, ArrayList<String> titles){
		ArrayList<String> chapter = new ArrayList<>();
		
		do {
			int[] first = {0,0};
			int[] next = {book.length(), book.length()};
			
			
			do {
				if(1 < titles.size() - 2) {
					next = Strings.indexesOf(book, titles.get(1));
					
					if(Array.containsNegative(next)) {
						titles.remove(1);
						continue;
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
			
			if(titles.size() > 2) {
				titles.remove(0);
			} else {
				break;
			}
		} while(titles.size() > 0);
		
		return chapter;
	}
}

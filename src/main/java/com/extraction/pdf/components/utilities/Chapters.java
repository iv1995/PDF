package com.extraction.pdf.components.utilities;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

public class Chapters {
	
	public static ArrayList<String> isolateChapters(String book, ArrayList<String> titles) throws IOException{
		if(StringUtils.isEmpty(book)) {
			return new ArrayList<String>();
		}
		
		ArrayList<String> chapters = new ArrayList<>();
		
		for (int i = 0; i < titles.size(); i++) {
			int[] first = Strings.indexesOf(book, titles.get(i));
			
			if(!Array.containsNegative(first)) {
				int[] next = new int[2];
				
				/*If there is only one title left copy until the text*/
				if(titles.size() > i + 2){
					next = Strings.indexesOf(book, titles.get(i + 1));
				} else if(titles.size() == 1 || titles.size() < i + 2) {
					next = Strings.lastIndex(book);
				}
				
				if(!Array.containsNegative(next) && !Strings.isPreceding(first, next)) {
					chapters.add(Strings.getBetween(book, first, next));
					book = Strings.unused(book, next);
					continue;
					
				} else if(titles.size() > i + 2){
					book = Strings.unused(book, first);
					titles.remove(i + 1);
					i--;
				} else {
					book = Strings.unused(book, first);
					chapters.add(book);
				}
			} else {
				titles.remove(i);
				i--;
			}
		}
		
		return chapters;
	}
}

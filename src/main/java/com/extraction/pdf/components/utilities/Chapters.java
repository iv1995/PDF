package com.extraction.pdf.components.utilities;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;

public class Chapters {
	
	public static boolean isolateChapters(String src, ArrayList<String> titles) throws IOException{
		if(StringUtils.isEmpty(src)) {
			return false;
		}
		
		ArrayList<String> chapters = new ArrayList<>();
		
		for (int i = 0; i < titles.size(); i++) {
			int[] first = Strings.indexesOf(src, titles.get(i));
			
			if(!Array.containsNegative(first)) {
				int[] next = new int[2];
				
				/*If there is only one title left copy until the text*/
				if(titles.size() > i + 2){
					next = Strings.indexesOf(src, titles.get(i + 1));
				} else if(titles.size() == 1 || titles.size() < i + 2) {
					next = Strings.lastIndex(src);
				}
				
				if(!Array.containsNegative(next) && !Strings.isPreceding(first, next)) {
					chapters.add(Strings.getBetween(src, first, next));
					src = Strings.unused(src, next);
					continue;
					
				} else if(titles.size() > i + 2){
					src = Strings.unused(src, first);
					titles.remove(i + 1);
					i--;
				} else {
					src = Strings.unused(src, first);
					chapters.add(src);
				}
			}
		}
		
		return true;
	}
}

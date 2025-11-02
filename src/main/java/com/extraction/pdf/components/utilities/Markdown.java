package com.extraction.pdf.components.utilities;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class Markdown {
	
	public static String markdown(String src, String str, String symbol) throws IOException {
		
		if(StringUtils.isEmpty(src) || StringUtils.isEmpty(str)) {
			return new String();
		}
		
		int[] index = Strings.indexesOf(src, str);
		
		String target = src;
		
		if(index == null || Array.containsNegative(index)) {
			new String();
		}
		
		int missing = 0;
		
		do {
			if(!Array.containsNegative(index)) {
				src = mark(src, Array.slide(index, missing), symbol);
				missing += Array.absoluteDistance(index);
				missing++;
			} else {
				break;
			}
			
			target = Strings.unused(target, index);
			index = Strings.indexesOf(target, str);
		} while(index != null && !Array.containsNegative(index));
		
		return src;
	}
	
	private static String mark(String str, int[] index, String before) throws IOException {
		
		if(StringUtils.isEmpty(str) || index == null) {
			return new String();
		}
		
		String left = str.substring(0, index[0]);
		String middle = before + str.substring(index[0], index[1] + 1);
		String right = str.substring(index[1] + 1, str.length());
		
		return left + middle + right;
	}
	
	private static String mark(String str, int[] index, String target, String before, String after) throws IOException {
		
		if(StringUtils.isEmpty(str) || StringUtils.isEmpty(target) || StringUtils.isEmpty(before)
									|| StringUtils.isEmpty(after)) {
			return new String();
		} else if(index == null) {
			return new String();
		}
		
		String left = str.substring(0, index[0]);
		String middle = str.substring(index[0], index[1]);
		String right = str.substring(index[1], str.length());
		
		if(!middle.toLowerCase().contains(str.toLowerCase())) {
			return new String();
		}
		
		int end = left.length() - 1;
		
		if(end >= 0 && right.length() > 0) {
			if(Character.toString(left.charAt(end)).equals(before)
					&& Character.toString(right.charAt(0)).equals(after)) {
					return new String();
			}
		}
		
		if(end > 0 && left.charAt(end) != '\n') {
			middle = "\n" + before + middle + after;
		} else {
			middle = before + middle + after;
		}
		
		if(right.charAt(0) != '\n') {
			middle += "\n";
		}
		
		return left + middle + right;
	}
}

package com.extraction.pdf.components.utilities;

import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

public class Markdown {
	
	public static String markdown(String src, String str, String symbol, boolean enclosed) throws IOException {
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
				src = mark(src, Array.slide(index, missing), symbol, enclosed);
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
	
	private static String mark(String str, int[] index, String symbol, boolean enclosed) throws IOException {
		if(StringUtils.isEmpty(str) || index == null) {
			return new String();
		}
		
		String left = str.substring(0, index[0]);
		String middle = symbol + str.substring(index[0], index[1]);
		String right = str.substring(index[1], str.length());
		
		if(enclosed) {
			middle += symbol;
		}
		
		return left + middle + right;
	}
}

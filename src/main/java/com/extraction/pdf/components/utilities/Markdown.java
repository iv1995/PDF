package com.extraction.pdf.components.utilities;

import com.extraction.pdf.components.utilities.Paths;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;

public class Markdown {
	
	public static String markdown(String src, String target, Type type) throws IOException {
		if(StringUtils.isEmpty(src) || StringUtils.isEmpty(target)) {
			return new String();
		}
		
		String marked = new String();
		
		if(type == Type.Title) {
			char c[] = {'#'};
			marked = markdown(src, target, c, false);
		} else if (type == Type.Listing){
			char c[] = {'`', '`'};
			marked = markdown(src, target, c, true);
		}
		
		return marked;
	}
	
	private static String markdown(String src, String str, char[] symbol, boolean after) throws IOException {
		
		if(StringUtils.isEmpty(src) || StringUtils.isEmpty(str)) {
			return new String();
		}
		
		String target = src;
		
		int[] index = Strings.indexesOf(target, str);
		
		if(index == null || Array.containsNegative(index)) {
			return new String();
		}
		
		String tmp = new String();
		
		int missing = 0;
		int added = 0;
		
		do {
			if(Array.containsNegative(index)) {
				break;
			}
			
			if(!after) {
				if(!(tmp = mark(src, Array.slide(index, missing), str, String.valueOf( symbol[0] ))).isEmpty()){
					src = tmp;
					tmp = new String();
					added++;
				}
			} else {
				if(!(tmp = mark(src, index, str, String.valueOf( symbol[0] ), String.valueOf( symbol[1] ))).isEmpty()) {
					src = tmp;
					tmp = new String();
					added++;
				}
			}
			
			missing += (Array.relativeDistance(index) + added + 1);
			target = Strings.unused(target, index);
			index = Strings.indexesOf(target, str);
		} while(index != null && !Array.containsNegative(index));
		
		return src;
	}
	
	private static String mark(String str, int[] indx, String target, String before) throws IOException {
		
		if(StringUtils.isEmpty(str) || indx == null) {
			return new String();
		}
		
		int[] index = new int[indx.length];
		
		for(int i = 0; i < indx.length; i++) {
			index[i] = indx[i];
		}
		
		String left = str.substring(0, index[0]);
		String middle = str.substring(index[0], index[1] + 1);
		String right = str.substring(index[1] + 1, str.length());
		
		String found = middle.toLowerCase().trim().replaceAll(" ", "");
		String expected = target.toLowerCase().trim().replaceAll(" ", "");
		
		if(!found.contains(expected)) {
			return new String();
		}
		
		int end = left.length() - 1;
		
		if(end >= 0) {
			if(String.valueOf( left.charAt(end) ).equals(before)) {
					return new String();
			}
		}
		
		if(end >= 0 && left.charAt(end) != '\n') {
			middle = "\n" + before + middle;
		} else if(end < 0){
			middle = "\n" + before + middle;
		} else if(middle.charAt(0) == '\n') {
			middle = middle.charAt(0) + before + middle.substring(1, middle.length());
		} else {
			middle = before + middle;
		}
		
		String tmp = right.replaceAll("(\s)\s*", "");
		
		if(tmp.charAt(0) != '\n') {
			middle += "\n";
		}
		
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

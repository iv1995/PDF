package com.extraction.pdf.components.utilities;

import org.apache.commons.lang3.StringUtils;

public class Paths {
	
	/**
	 * Use it when you want to process a different file
	 * The new file should be located in the same folder as the old file
	 * 
	 * @param old the path to the old file
	 * @param name the name of the
	 * @return
	 */
	public static String changePdfSrc(String old, String name){

		if (StringUtils.isEmpty(old) || StringUtils.isEmpty(name)) {
			return new String();
		} else if (!old.contains("/")) {
			return name;
		}

		/* The name of the PDF file follows the last / character */
		int index = old.lastIndexOf("/");
		String folder = old.substring(0, index + 1);

		return folder + name;
	}
}

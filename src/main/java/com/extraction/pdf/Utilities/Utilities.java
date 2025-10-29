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
}

package com.extraction.pdf.Utilities;

public class Utilities {
	
	public static String changeSrcPdf(String old, String name){

		if (old == null || name == null || old.isBlank() || name.isBlank()) {
			return "";
		}

		if (!old.contains("/")) {
			return name;
		}

		/* The name of the PDF file follows the last / character */
		int index = old.lastIndexOf("/");
		String folder = old.substring(0, index + 1);

		return folder + name;
	}
}

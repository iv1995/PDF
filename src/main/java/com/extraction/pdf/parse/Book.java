package com.extraction.pdf.parse;

import java.util.ArrayList;

public class Book {
	
	private ArrayList<Page> pages = new ArrayList<>();
	
	public boolean addPage(Page page) {
		pages.add(page);
		return true;
	}
}

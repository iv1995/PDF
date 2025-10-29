package com.extraction.pdf.parse;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Book {
	
	private ArrayList<Page> pages = new ArrayList<>();
	
	private String img = "./Storage/Tagged/Output/Images";
	private boolean tagged = false;
	
	public boolean addPage(Page page) {
		pages.add(page);
		return true;
	}
	
	public Page getPage(int number) {
		return pages.get(number);
	}
}

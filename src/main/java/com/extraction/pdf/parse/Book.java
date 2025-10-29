package com.extraction.pdf.parse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.springframework.stereotype.Service;

import com.extraction.pdf.renders.Render;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.tagging.PdfStructTreeRoot;
import com.itextpdf.layout.element.Image;
import com.extraction.pdf.Utilities.Utilities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Service
public class Book {
	
	private ArrayList<Page> pages = new ArrayList<>();

	private int imageCount = 0;

	private String img = "./Storage/Tagged/Output/Images/";
	private boolean tagged = false;
	private String language = "italian";

	public boolean addPage(Page page) {
		pages.add(page);
		return true;
	}

	public Page getPage(int number) {
		return pages.get(number);
	}

	/*
	 * Required source file. The second one is not required although if not
	 * available the images might not have as their name the associated caption
	 */
	private String src = "./Storage/pdf/src/PDF20.pdf";
	private String destCaptions = "./Storage/Tagged/Output/Figures/";

	/* Processed data can be found inside these folders */
	private String dest = "./Storage/Untagged/Output/Text/";
	private String folder = "./Storage/Untagged/Output/Images/";
	private String taggedDest = "./Storage/Tagged/Output/Text/";
	private String taggedFolder = "./Storage/Tagged/Output/Images/";
	private String taggedTitles = "./Storage/Tagged/Output/Titles/";
	private String chapters = "./Storage/Tagged/Output/Chapters/";
	private String listing = "./Storage/Tagged/Output/Listing/";
	
	/**
	 * @return
	 * @throws IOException
	 */
	public boolean processPdf() throws IOException {
		
		PdfReader reader = new PdfReader(src);
		PdfDocument pdf = new PdfDocument(reader);
		
		PdfStructTreeRoot tree = pdf.getStructTreeRoot();
		
		if (tree != null) {
			tagged = true;
		}
		
		for(int i = 32; i < 33; i++) {
			pages.add(new Page(processPage(reader, pdf, i)));
		}
		
		printPageObjects();
		
		return true;
	}
	
	/**
	 * @param reader a PdfReader instance
	 * @param pdf a PdfDocument instance
	 * @param page the number of the page
	 * @return
	 * @throws IOException
	 */
	public ArrayList<Obj> processPage(PdfReader reader, PdfDocument pdf, int page) throws IOException {

		Render render = new Render();
		PdfCanvasProcessor pcp = new PdfCanvasProcessor(render);
		
		pcp.reset();
		pcp.processPageContent(pdf.getPage(page));
		
		return render.getObjects();
	}
	
	public void printPageObjects() {
		
		ArrayList<Obj> target = pages.get(0).getObjects(); 
		
		Comparator<Obj> comparator = new Sort();
		Collections.sort(target, comparator);
	}
}

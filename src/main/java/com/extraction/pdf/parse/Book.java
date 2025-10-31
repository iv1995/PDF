package com.extraction.pdf.parse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.extraction.pdf.renders.Render;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.extraction.pdf.components.Img;
import com.extraction.pdf.components.Obj;
import com.extraction.pdf.components.Page;

import lombok.Data;

@Data
@Service
public class Book {
	
	private ArrayList<Page> pages = new ArrayList<>();
	private boolean tagged = false;
	private String language = "italian";

	private String src = "./Storage/Input/PDF/";
	
	private String text = "./Storage/Output/Text/";
	private String titles = "./Storage/Output/Titles/";
	private String chapters = "./Storage/Output/Chapters/";
	private String listing = "./Storage/Output/Listing/";
	
	private String captions = "./Storage/Output/Figures/";
	private String images = "./Storage/Output/Images/";
	
	
	public boolean addPage(Page page) {
		pages.add(page);
		return true;
	}
	
	/**
	 * 
	 * @param number it is the number of the extracted Page(starts with 0)
	 * @return
	 */
	public Page getPage(int number) {
		return pages.get(number);
	}
	
	/**
	 * It processes the entire PDF file
	 * 
	 * @param src The name of the PDF file (stored inside the
	 * 			  ./Storage/Input/PDF/
	 * 			  folder)
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean processPdf(String src) throws IOException {
		
		src = this.src + src;
		
		PdfReader reader = new PdfReader(src);
		PdfDocument pdf = new PdfDocument(reader);
		
		if (pdf.getStructTreeRoot() != null) {
			tagged = true;
		}
		
		for(int i = 1; i < pdf.getNumberOfPages(); i++) {
			Page page = new Page(processPage(reader, pdf, i, tagged));
			pages.add(page);
			for(Obj obj : page.getObjects()) {
				if(obj instanceof Img) {
					StringBuilder caption = page.getCaption((Img)obj, tagged);
				}
			}
		}
		
		return true;
	}
	
	/**
	 * @param reader a PdfReader instance
	 * @param pdf a PdfDocument instance
	 * @param page the number of the page
	 * 
	 * @return
	 * @throws IOException
	 */
	private ArrayList<Obj> processPage(PdfReader reader, PdfDocument pdf, int page, boolean tagged) throws IOException {

		Render render = new Render();
		PdfCanvasProcessor pcp = new PdfCanvasProcessor(render);
		
		pcp.reset();
		pcp.processPageContent(pdf.getPage(page));
		
		return render.getObjects();
	}
}

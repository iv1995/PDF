package com.extraction.pdf.parse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.extraction.pdf.renders.Render;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.extraction.pdf.components.Img;
import com.extraction.pdf.components.Obj;
import com.extraction.pdf.components.Page;
import com.extraction.pdf.components.Text;
import com.extraction.pdf.components.utilities.Strings;

import lombok.Data;

@Data
@Service
public class Book {
	
	private ArrayList<Page> pages = new ArrayList<>();
	private boolean tagged = false;
	private String language = "italian";

	private String src = "./Storage/Input/PDF/";
	
	private String chapters = "./Storage/Output/Chapters/";
	private String listing = "./Storage/Output/Listing/";
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
		
		
		int images = 0;
		
		for(int i = 1; i <= pdf.getNumberOfPages(); i++) {
			Page page = new Page(processPage(reader, pdf, i, tagged));
			pages.add(page);
			
			images = addCaptions(page, images);
		}
		
		for(Page page : pages) {
			for(Obj obj : page.getObjects()) {
				if(obj instanceof Img) {
					Img img = (Img)obj;
					
					File file = new File("./Storage/Output/Images/" + Strings.remove(img.getCaption().getCaption(), ".:;\"") + "." + img.getExtension());
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(img.masked());
					fos.flush();
					fos.close();
				}
			}
		}
		
		getTitles(pages);
		
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
	
	private int addCaptions(Page page, int images) throws IOException {
		
		for(Obj obj : page.getObjects()) {
			if(obj instanceof Img) {
				String caption = page.getCaption((Img)obj, tagged);
				
				if(!caption.isEmpty()) {
					Img img = (Img)obj;
					img.getCaption().setCaption(caption);
				} else {
					Img img = (Img)obj;
					img.getCaption().setCaption(String.valueOf(images++));
				}
			}
		}
		
		return images;
	}
	
	private ArrayList<String> getTitles(ArrayList<Page> pages){
		ArrayList<String> titles = new ArrayList<String>();
		StringBuilder title = new StringBuilder();
		PdfName searched = PdfName.H1;
		ArrayList<Obj> objects = new ArrayList<Obj>();
		
		for(int i = 0; i < pages.size(); i++) {
			
			objects = pages.get(i).getObjects();
			
			for(int j = 0; j < objects.size(); j++) {
				if(objects.get(j) instanceof Text) {
					Text chunk = (Text)objects.get(j);
					PdfName tag = null;
					
					if(chunk.getRi().getCanvasTagHierarchy().size() > 0) {
						tag = chunk.getRi().getCanvasTagHierarchy().get(0).getRole();
					} else {
						continue;
					}			
					
					if(tag.equals(searched)) {
						while(tag.equals(searched)) {
							if(j < objects.size()) {
								if(objects.get(j) instanceof Text) {
									chunk = (Text)objects.get(j);
									tag = chunk.getRi().getCanvasTagHierarchy().get(0).getRole();
									title.append(chunk.getChunk());
									j++;
								} else {
									j++;
								}
							} else {
								break;
							}
						}
						
						titles.add(title.toString());
						title = new StringBuilder();
					}
				}
			}
		}
		
		return titles;
	}
}

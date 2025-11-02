package com.extraction.pdf.parse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.extraction.pdf.renders.Render;
import com.itextpdf.io.source.FileChannelRandomAccessSource;
import com.itextpdf.io.source.PdfTokenizer;
import com.itextpdf.io.source.RandomAccessFileOrArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfObject;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.io.source.PdfTokenizer.TokenType;
import com.extraction.pdf.components.Img;
import com.extraction.pdf.components.Obj;
import com.extraction.pdf.components.Page;
import com.extraction.pdf.components.Text;
import com.extraction.pdf.components.utilities.Strings;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import com.itextpdf.kernel.pdf.PdfStream;

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
	 * @throws URISyntaxException 
	 */
	public void processPdf(String src) throws IOException, URISyntaxException {
		
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
		
		save(pages);
		ArrayList<String> titles = getBookTitles(pages);
		
		for(int i = 0; i < titles.size(); i++) {
			System.out.println(titles.get(i));
		}
	}
	
	/**
	 * 
	 * Saves all the text into a file
	 * Saves each image into a separate file
	 * 
	 * @param pages The pages making up the PDF file
	 * @throws IOException
	 */
	public void save(ArrayList<Page> pages) throws IOException {
		StringBuilder all = new StringBuilder();
		
		for(Page page : pages) {
			for(Obj obj : page.getObjects()) {
				if(obj instanceof Text) {
					Text chunk = (Text)obj;
					all.append(chunk.getChunk());
				} else if(obj instanceof Img) {
					Img img = (Img)obj;
					
					String name = Strings.remove(img.getCaption().getCaption(), ".:;\"") + "." + img.getExtension();
					all.append("![" + name + "](" + "./Storage/Output/Images/" + name + ")");
					
					File file = new File("./Storage/Output/Images/" + name);
					file.createNewFile();
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(img.masked());
					fos.flush();
					fos.close();
				}
			}
			
			all.append("\n");
		}
		
		File text = new File("./Storage/Output/Chapters/Book.txt");
		text.createNewFile();
		
		FileWriter writer = new FileWriter(text);
		writer.write(all.toString());
		writer.flush();
		writer.close();
	}
	
	/**
	 * @param reader a PdfReader instance
	 * @param pdf a PdfDocument instance
	 * @param page the number of the page
	 * 
	 * @return The objects making up this PDF page
	 * @throws IOException
	 */
	private ArrayList<Obj> processPage(PdfReader reader, PdfDocument pdf, int page, boolean tagged) throws IOException {

		Render render = new Render();
		PdfCanvasProcessor pcp = new PdfCanvasProcessor(render);
		
		pcp.reset();
		pcp.processPageContent(pdf.getPage(page));
		
		return render.getObjects();
	}
	
	/**
	 * 
	 * For each image tries to find its caption
	 * 
	 * @param page The page that is going to be processed
	 * @param images The image counter
	 * @return The incremented image counter
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private int addCaptions(Page page, int images) throws IOException, URISyntaxException {
		
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
	
	/**
	 * 
	 * @param pages The pages that make up the PDF file
	 * @return The titles that are part of the PDF file
	 */
	private ArrayList<String> getBookTitles(ArrayList<Page> pages){
		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<Obj> objects = new ArrayList<Obj>();
		
		for(int i = 0; i < pages.size(); i++) {
			objects = pages.get(i).getObjects();
			
			titles.addAll(getPageTitles(objects));
		}
		
		return titles;
	}
	
	/**
	 * 
	 * @param objects The objects making a PDF page
	 * @return The Chapter titles contained in this PDF page
	 */
	private ArrayList<String> getPageTitles(ArrayList<Obj> objects){
		ArrayList<String> titles = new ArrayList<String>();
		PdfName searched = PdfName.H1;
		
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
					Map<Integer, StringBuilder> chunks = getTitleChunks(objects, j, chunk);
					
					j = chunks.keySet().stream().findFirst().orElse(0);
					titles.add(chunks.get(j).toString());
				}
			}
		}
		
		return titles;
	}
	
	/**
	 * 
	 * @param objects The objects making a PDF page
	 * @param j The index of the last chunk that has been analyzed
	 * @param chunk The last chunk that has been analyzed
	 * @return A Map<Integer, StringBuilder> with only one key (which is j) 
	 * 											   and only one value which is the tile
	 */
	private Map<Integer, StringBuilder> getTitleChunks(ArrayList<Obj> objects, int j, Text chunk){
		PdfName tag = PdfName.H1;
		PdfName searched = PdfName.H1;
		
		StringBuilder title = new StringBuilder();
		
		do {
			if(j < objects.size()) {
				title.append(chunk.getChunk());
				++j;
				
				if(j < objects.size()) {
					if(objects.get(j) instanceof Text) {
						chunk = (Text)objects.get(j);
						tag = chunk.getRi().getCanvasTagHierarchy().get(0).getRole();
							if(!tag.equals(searched)) {
							break;
						} 
					}
				} else {
					break;
				}
			} else {
				break;
			}
		}while(tag.equals(searched));
		
		Map<Integer, StringBuilder> chunks = new HashMap<>();
		chunks.put(j, title);
		
		return chunks;
	}
}

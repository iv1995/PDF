package com.extraction.pdf.components;

import java.awt.image.BufferedImage;

import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import lombok.Data;

@Data
public class Img extends Obj {
	
	private ImageRenderInfo ri;
	private PdfImageXObject XObjectImage;
	private String extension;
	private Matrix ctm;
	private PdfImageXObject imageMask;
	private String path;
	private Caption caption = new Caption();
	
	private Surrounding text;
	
	public Img(IEventData data, int images) {
		if(data != null) {
			populate(data, images);
		}
	}
	
	private void populate(IEventData data, int images) {
		super.setData(data);
		ri = (ImageRenderInfo) data;
		ctm = ri.getImageCtm();
		super.setCoordinate(new Point(ctm.get(Matrix.I31), ctm.get(Matrix.I32)));
		super.setSize(getSize(ctm));
		XObjectImage = ri.getImage();
		extension = XObjectImage.identifyImageFileExtension();
		path = new String();
		caption.setNumber(images);
	}
	
	/**
	 * 
	 * @param ctm Current Transformation Matrix
	 * @return
	 */
	public Size getSize(Matrix ctm) {
		float a = ctm.get(Matrix.I11);
		float b = ctm.get(Matrix.I12);
		
		float c = ctm.get(Matrix.I21);
		float d = ctm.get(Matrix.I22);
		
		float width = (float)Math.sqrt(a*a + b*b);
		float height = (float)Math.sqrt(c*c + d*d);
		
		return new Size(height, width);
	}
	
	public BufferedImage mask(BufferedImage image, BufferedImage mask) {
		
		int width = image.getWidth();
		
		int[] img = new int[width];
		int[] msk = new int[width];

		for (int j = 0; j < image.getHeight(); j++) {
			
		    image.getRGB(0, j, width, 1, img, 0, 1);
		    mask.getRGB(0, j, width, 1, msk, 0, 1);
		    
		    
		    for (int i = 0; i < width; i++) {
		        int color = ~img[i];
		        int maskColor = msk[i];
		        
		        color &= maskColor;
		         
		        /*Getting the negation of the non alpha bits of the masked image color*/
		        int colorBody = ~(color & 0x00ffffff);
		        
		        /*Getting the alpha bits of the masked image color and mixing them with the remaining ones*/
		        color = color & 0xff000000 | colorBody;
		        
		        img[i] = color;
		    }
		    
		    image.setRGB(0, j, width, 1, img, 0, 1);
		}
		
		return image;
	}
}

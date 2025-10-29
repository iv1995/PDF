package com.extraction.pdf.parse;

import java.awt.image.BufferedImage;

import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Img extends Obj {
	
	private ImageRenderInfo ri;
	private PdfImageXObject XObjectImage;
	private Matrix ctm;
	private PdfImageXObject imageMask;
	
	public BufferedImage mask(BufferedImage image, BufferedImage mask) {
		
		int width = image.getWidth();
		
		int[] img = new int[width];
		int[] m = new int[width];

		for (int j = 0; j < image.getHeight(); j++) {
			
		    image.getRGB(0, j, width, 1, img, 0, 1);
		    mask.getRGB(0, j, width, 1, m, 0, 1);
		    
		    
		    for (int i = 0; i < width; i++) {
		        int color = ~img[i];
		        int maskColor = m[i];
		        
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

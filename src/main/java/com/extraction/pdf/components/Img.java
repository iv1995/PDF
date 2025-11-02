package com.extraction.pdf.components;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;

import lombok.Data;

@Data
public class Img extends Obj {
	
	private ImageRenderInfo ri;
	private PdfImageXObject image;
	private String extension;
	private Matrix ctm;
	private PdfImageXObject mask;
	private String path;
	private Caption caption = new Caption();
	private byte[] masked = null;
	
	private boolean saved = false;
	
	public Img(IEventData data){
		if(data != null) {
			try {
				populate(data);
			} catch (IOException e) {}
		}
	}
	
	private void populate(IEventData data) throws IOException {
		super.setData(data);
		ri = (ImageRenderInfo) data;
		ctm = ri.getImageCtm();
		super.setCoordinate(new Point(ctm.get(Matrix.I31), ctm.get(Matrix.I32)));
		super.setSize(getSize(ctm));
		
		image = ri.getImage();
		mask = getMask(image.getPdfObject());
		
		path = new String();
		
		extension = image.identifyImageFileExtension();
		caption.setNumber(hashCode());
		caption.setCaption(String.valueOf(hashCode()));
	}
	
	/**
	 * 
	 * @param ctm Current Transformation Matrix
	 * @return A new Size instance containing the height and width of the image
	 */
	public Size getSize(Matrix ctm) {
		float a = ctm.get(Matrix.I11);
		float b = ctm.get(Matrix.I12);
		float c = ctm.get(Matrix.I21);
		float d = ctm.get(Matrix.I22);
		
		return new Size((float)Math.sqrt(c*c + d*d), 
						(float)Math.sqrt(a*a + b*b));
	}
	
	/**
	 * 
	 * @param object The PdfStream from which we are going to extract the mask
	 * @return
	 */
	public PdfImageXObject getMask(PdfStream object) {
		PdfStream mask = object.getAsStream(PdfName.SMask);
		
		if(mask == null) {
			return null;
		}
		
		return new PdfImageXObject(mask);
	}
	
	public byte[] masked() throws IOException {
		if(masked != null) {
			return masked;
		} else {
			return mask();
		}
	}
	
	private byte[] mask() throws IOException {
		if(mask != null) {
			BufferedImage image = this.image.getBufferedImage(); 
	        BufferedImage mask = this.mask.getBufferedImage();
	        BufferedImage result = mask(image, mask);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(result, extension, baos);
	        masked = baos.toByteArray();
		} else {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        ImageIO.write(image.getBufferedImage(), extension, baos);
	        masked = baos.toByteArray();
		}
        
		return masked;
	}
	
	/**
	 * 
	 * @param image The image which is going to be merged with its mask
	 * @param mask The mask of the image
	 * @return The combination of the two things
	 */
	private BufferedImage mask(BufferedImage image, BufferedImage mask) {
		
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
		         
		        /*Getting the negation of the non alpha bits
		         *of the masked image color*/
		        int colorBody = ~(color & 0x00ffffff);
		        
		        /*Getting the alpha bits of the masked image color
		         *and mixing them with the remaining ones*/
		        color = color & 0xff000000 | colorBody;
		        
		        img[i] = color;
		    }
		    
		    image.setRGB(0, j, width, 1, img, 0, 1);
		}
		
		return image;
	}
}

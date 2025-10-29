package com.extraction.pdf.renders;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import javax.imageio.ImageIO;

import com.extraction.pdf.parse.Obj;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.Matrix;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfStream;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.ImageRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.CharacterRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.xobject.PdfImageXObject;
import com.itextpdf.layout.element.Image;

import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Getter
@Setter
public class Render implements IEventListener {

	private ArrayList<Obj> objects = new ArrayList<>();
	private int count = 0;
	private String dest;
	private File file;

	private boolean tagged = false;

	

	public Render() {}

	@Override
	public void eventOccurred(IEventData data, EventType type) {
			
		if (type.equals(EventType.RENDER_TEXT)) {
			
			/*We use CharacterRenderInfo because it makes one step easy for us :
			 * returns a Bounding Box*/
//			TextRenderInfo ri = (TextRenderInfo) data;
//			CharacterRenderInfo cr = new CharacterRenderInfo(ri);
			
			
			
//			Rectangle r = cr.getBoundingBox();
			
			/*In case of the y coordinate I need to add the height as well since it might decide if
			 * it comes first or not*/
//			String y = String.format(Locale.US ,"%.7f", r.getY() + r.getHeight());
//			String x = String.format(Locale.US, "%.7f", r.getX());		
		} else if (type.equals(EventType.RENDER_IMAGE)) {
			
			ImageRenderInfo ri = (ImageRenderInfo) data;
			
			/*This matrix contains the x,y,width,height values*/
			Matrix m = ri.getImageCtm();
			float x = 0, y = 0, width = 0, height = 0;
			
			PdfImageXObject XObjectImage = ri.getImage();
			
			Image image = new Image(XObjectImage);
			
			byte[] i = XObjectImage.getImageBytes(true);
			ImageData d1 = ImageDataFactory.create(i);
			
			String extension = XObjectImage.identifyImageFileExtension();
			String imageName = "";
			String imageDest = "";
			
			try {
				
				imageName = count + "." + extension;
				imageDest = dest + imageName;
				
				file = new File(imageDest);
				
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(i);
				fos.flush();
				fos.close();
			} catch(Exception e) {}
			
			try {
				PdfStream object = XObjectImage.getPdfObject();
				PdfStream mask = null;
				mask = (PdfStream)object.getAsStream(PdfName.SMask);
	            if (mask != null)
	            {
	            	PdfImageXObject imageMask = new PdfImageXObject(mask);
	            	ImageData maskData = ImageDataFactory.create(imageMask.getImageBytes());
	            	maskData.makeMask();
	            	
	            	
	            	
	                String name = count + "-mask." +  imageMask.identifyImageFileExtension();
	                
	                FileOutputStream fos1 = new FileOutputStream(dest + name);
	                
	                fos1.write(imageMask.getImageBytes());
	                fos1.flush();
	                fos1.close();
	                
	                BufferedImage biImage = ImageIO.read(new File(imageDest)); 
	                BufferedImage biMask = ImageIO.read(new File(dest + name));
	                
	                BufferedImage result = mask(biImage, biMask);
	                
	                ByteArrayOutputStream baos = new ByteArrayOutputStream();
	                ImageIO.write(result, "png", baos);
	                byte[] imageBytes = baos.toByteArray();
	                
	                Files.delete(Path.of(dest+name));
	                
	                
	                FileOutputStream fos2 = new FileOutputStream(imageDest);
	                fos2.write(imageBytes);
	                fos2.flush();
	                fos2.close();
	            }
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
			x = m.get(Matrix.I31);
			y = m.get(Matrix.I32);
			
			
			float a = m.get(Matrix.I11);
			float b = m.get(Matrix.I12);
			
			float c = m.get(Matrix.I21);
			float d = m.get(Matrix.I22);
			
			width = (float)Math.sqrt(a*a + b*b);
			height = (float)Math.sqrt(c*c + d*d);

			Rectangle r = new Rectangle(x, y, width, height);
			
			/*In case of the y coordinate I need to add the height as well since it might decide if
			 * it comes first or not*/
			String yString = String.format(Locale.US ,"%.7f", y + height);
			String xString = String.format(Locale.US, "%.7f", x);
			
			count++;
		}
	}

	@Override
	public Set<EventType> getSupportedEvents() {
		// TODO Auto-generated method stub
		return null;
	}
	
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

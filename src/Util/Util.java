package Util;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

public class Util {

	/*
	 * 任意のファイルを任意の場所にコピー
	 */

	public static void copy(String srcPath, String dstPath) 
			throws IOException {

		FileChannel srcChannel = new FileInputStream(srcPath).getChannel();
		FileChannel destChannel = new FileOutputStream(dstPath).getChannel();
		try {
			srcChannel.transferTo(0, srcChannel.size(), destChannel);
		} finally {
			srcChannel.close();
			destChannel.close();
		}

	}
	/*
	 * JPEG画像のリサイズ
	 */
	public void resize(String srcPath,String dstPath) throws IOException{
		int RESIZE_INT = 120;

		BufferedImage sourceImage = ImageIO.read(new File(srcPath));
		int height = sourceImage.getHeight();
		int width = sourceImage.getWidth();
		double resizeHeight = 0;
		double resizeWidth = 0;
		String newFileName = "111.jpg";


		if(height > RESIZE_INT && width > RESIZE_INT){
			if(height > width){
				resizeWidth = RESIZE_INT;
				resizeHeight = (int)((double)RESIZE_INT / width * (double)height);
			}else{
				resizeHeight = RESIZE_INT;
				resizeWidth = (int)((double)RESIZE_INT / height * (double)width);
			}
		}

		//リサイズ処理
		BufferedImage resizeImage = new BufferedImage(width, height, sourceImage.getType());
		AffineTransformOp ato = null;
		ato = new AffineTransformOp(
				AffineTransform.getScaleInstance((double)resizeWidth / width,
						(double) resizeHeight / height),
						AffineTransformOp.TYPE_BILINEAR);
		ato.filter(sourceImage, resizeImage);
		BufferedImage subImage =  resizeImage.getSubimage(0, 0, RESIZE_INT, RESIZE_INT);


		ImageOutputStream imageStream = ImageIO.createImageOutputStream(new File(dstPath));
		ImageWriter writer = null;
		Iterator it = (Iterator) ImageIO.getImageWritersByFormatName("jpg");
		writer = (ImageWriter) it.next();
		writer.setOutput(imageStream);
		JPEGImageWriteParam jpgWriter = new JPEGImageWriteParam(Locale.getDefault());
		jpgWriter.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		jpgWriter.setCompressionQuality(1f);
		writer.write(null, new IIOImage(subImage, null, null), jpgWriter);
		imageStream.flush();
		writer.dispose();
		imageStream.close();

	}
	public static String getSuffix(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(point + 1);
	    }
	    return fileName;
	}

}
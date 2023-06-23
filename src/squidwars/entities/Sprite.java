package squidwars.entities;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

/**
 * Ref: https://zetcode.com/javagames/movingsprites/
 *
 */
public class Sprite implements Serializable{
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean visible;
    protected BufferedImage image;
    public AffineTransformOp imgTransform;

    public Sprite(int x, int y) {

        this.x = x;
        this.y = y;
        visible = true;
    }
    
    
    //https://stackoverflow.com/questions/9417356/bufferedimage-resize
    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }  

    protected void loadImage(String imageName) {

    	try {
            image = ImageIO.read(new File(imageName)); 
            image = resize(image, 40, 40);
    	} catch (IOException ex) {
    		System.out.println("Cannot load image file.");
    	}
        
        width = image.getWidth(null);
        height = image.getHeight(null);

        // initial orientation
        rotateImage(0);
    }
    
    protected void getImageDimensions() {

        width = image.getWidth(null);
        height = image.getHeight(null);
    }    

    public BufferedImage getImage() {
        return image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getWidth() {
    	return width;
    }
    
    public int getHeight() {
    	return height;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    
    public void rotateImage(int angle) {
    	double rotationRequired = Math.toRadians (angle);

        double locationX = image.getWidth(null) / 2;
    	double locationY = image.getHeight(null) / 2;
    	AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
    	imgTransform = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
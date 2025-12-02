package resources;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Image;
import java.awt.Graphics;

public class ImagePanel extends JPanel{
    private Image image;
    public ImagePanel(String imagePath){
        //Load the food image
        try{
            ImageIcon icon = new ImageIcon(imagePath);
            this.image = icon.getImage();
        }
        catch(Exception e){
            System.out.println("Can't load the food image:"+imagePath);
            System.out.println("A Random error occured:"+e.getMessage());
            System.out.println(e.getStackTrace());
        }
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        if(image!=null){
            //Draw the image to fill the entire area of THIS panel
            //(0,0) is the top-left corner of the panel
            //getWidth() and getHeight() are the dimensions of the panel
            g.drawImage(image, 0, 0, getWidth(),getHeight(),this);
        }
    }
}
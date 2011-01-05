/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Philipp
 */
public class EditorObject extends GameObject {
    
    Point position = new Point(0, 0);
    int tile = 0;
    boolean selected = false;

    public EditorObject(GameObject go, Point position){
        super(go.name, go.objectChar);
        this.position = position;
    }
    
    public EditorObject(GameObject go, Point position, int tile){
        super(go.name, go.objectChar);
        this.position = position;
        this.tile = tile;
        try{
            image = ImageIO.read(new File(Toolbox.tilesheetTextField.getText()));
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public void draw(Graphics g, ImageObserver io){
        g.drawImage(image, position.x*16, position.y*16, position.x*16 + 16, position.y*16 + 16, 0 + 16*tile, 0, 16 + 16*tile, 16, io);
        if(selected){
            g.setColor(new Color(1,0,0,0.5f));
            g.fillRect(position.x*16, position.y*16, 16, 16);
        }
    }

    public GameObject getParent(){
        return new GameObject(super.name, super.objectChar);
    }

}

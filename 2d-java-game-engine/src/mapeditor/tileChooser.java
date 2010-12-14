/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeditor;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener.*;
/**
 *
 * @author Basti
 */
public class tileChooser extends JPanel implements Runnable {

    Thread main;
    public static Image image;

    public static int numbx=0;
    public static int numby=0;
    public static int lengthx=0;
    public static int lengthy=0;

    public tileChooser(){

        setPreferredSize(new Dimension(500,100));
        this.setDoubleBuffered(true);
        TileMouse mouse = new TileMouse();
        this.addMouseListener(mouse);
        main = new Thread(this);
        main.start();
    }

    public void run(){
        while(true){
            repaint();
            image = MapEditorView.tiles;
            try{
                main.sleep(20L);
            }
            catch(Exception e){

            }
        }
    }
    //draw Tileset
    @Override
     public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(image,0,0,null);
        g.setColor(Color.WHITE);
        g.drawRect(numbx*16, numby*16, 16+lengthx*16, 16+lengthy*16);
     }

}

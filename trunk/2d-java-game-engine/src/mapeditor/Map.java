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

public class Map extends JPanel implements Runnable {


    public static int mousex=0;
    public static int mousey=0;
    
    Thread main;
    public static Image img = MapEditorView.tiles;
    public static Image img2 = MapEditorView.sprites;
    public static Tiles[][] tile = new Tiles[100][100];
    public static Sprites[][] sprite = new Sprites[100][100];
    

    public Map(){

        for (int i=0;i<100;i++){
            for (int j=0;j<100;j++){
                tile[i][j] = new Tiles(0,0);
            }
        }
        for (int i=0;i<100;i++){
            for (int j=0;j<100;j++){
                sprite[i][j] = new Sprites(0,0);
            }
        }

        setPreferredSize(new Dimension(1000,1000));
        this.setDoubleBuffered(true);
        MouseKlick mouse = new MouseKlick();
        this.addMouseListener(mouse);
        main = new Thread(this);
        main.start();
    }

    public void run(){
        while(true){
            repaint();
            try{
                main.sleep(20L);
            }
            catch(Exception e){

            }
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillRect(0,0,1600,1600);
        for (int x=0;x<1600;x+=16){
            for (int y=0;y<1600;y+=16){
                g.drawImage(img,x,y,x+16,y+16,tile[x/16][y/16].x,tile[x/16][y/16].y,tile[x/16][y/16].x+16,tile[x/16][y/16].y+16,this);
            }
        }
        for (int x=0;x<1600;x+=16){
            for (int y=0;y<1600;y+=16){
                g.drawImage(img2,x,y,x+16,y+16,sprite[x/16][y/16].x,sprite[x/16][y/16].y,sprite[x/16][y/16].x+16,sprite[x/16][y/16].y+16,this);
            }
        }

        for (int x=0;x<1600;x+=16){
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(x,0,x,1600);
            g.drawLine(0,x,1600,x);
        }
        /*g.setColor(Color.WHITE);
        g.drawString(mousex+"|"+mousey,100,100);
        g.drawString("Button: "+MouseKlick.Button,100,120);*/
    }
}
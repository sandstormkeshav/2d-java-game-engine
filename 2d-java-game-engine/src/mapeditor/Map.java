/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeditor;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author Basti
 */

public class Map extends JPanel implements Runnable {

    Thread main;
    Image img = Toolkit.getDefaultToolkit().getImage("D:\\Programme\\NetBeansProj\\tileset3yx.gif");
    Tiles[] tile = new Tiles[100];
    

    public Map(){

        for (int i=0;i<100;i++){
            tile[i] = new Tiles((int)(Math.random()*20)+1,(int)(Math.random()*20)+1);
        }

        setPreferredSize(new Dimension(1000,1000));
        this.setDoubleBuffered(true);
        //this.addMouseListener(new MouseKlick());
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
        g.fillRect(0,0,16000,16000);
        for (int x=0;x<1600;x+=16){
            for (int y=0;y<1600;y+=16){
                g.drawImage(tile[y/16].image,x,y,x+16,y+16,tile[y/16].x,tile[y/16].y,tile[y/16].x+16,tile[y/16].y+16,this);
            }
        }
       
        
    }

}


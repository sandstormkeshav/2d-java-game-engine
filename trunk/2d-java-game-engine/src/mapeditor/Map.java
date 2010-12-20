/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener.*;

import engine.game.gameMain;

/**
 *
 * @author Basti
 */

public class Map extends JPanel implements Runnable {


    //public static int mousex=0;
    //public static int mousey=0;
    
    Thread main;
    public static Image img = MapEditorView.tiles;
    public static Image img2 = MapEditorView.sprites;
    public static Tiles[][] tile = new Tiles[100][100];
    public static int maxWidth;
    public static int maxHeight;

    MouseKlick mb = new MouseKlick();
    MouseMotion m = new MouseMotion();
    

    public Map(){

        //create (empty) Tiles and Sprites all over the map
        for (int i=0;i<100;i++){
            for (int j=0;j<100;j++){
                tile[i][j] = new Tiles(0,0);
            }
        }


        setPreferredSize(new Dimension(1000,1000));
        this.setDoubleBuffered(true);

        
        this.addMouseListener(mb);

        
        this.addMouseMotionListener(m);
        main = new Thread(this);
        main.start();
    }

    public void run(){
        while(true){
            try{
                main.sleep(20L);
            }
            catch(Exception e){

            }
            addThing();
            repaint();
        }
    }


    //add a tile/sprite if a mouseButton is pressed
    public void addThing(){
        if (Toolbox.ToolboxTab.getSelectedIndex()==0){

            if (mb.getButton()==1){
                img = MapEditorView.tiles;
                img2 = MapEditorView.sprites;
                for (int i=0;i<=tileChooser.lengthx;i++){
                    for (int j=0;j<=tileChooser.lengthy;j++){
                        tile[(int)(m.getX()/16)+i][(int)(m.getY()/16)+j].x=(tileChooser.numbx+i)*16;
                        tile[(int)(m.getX()/16)+i][(int)(m.getY()/16)+j].y=(tileChooser.numby+j)*16;
                    }
                }
            }
            else{
                if (mb.getButton()==3){
                    for (int i=0;i<=tileChooser.lengthx;i++){
                        for (int j=0;j<=tileChooser.lengthy;j++){
                            tile[(int)(m.getX()/16)+i][(int)(m.getY()/16)+j].x=(-16);
                            tile[(int)(m.getX()/16)+i][(int)(m.getY()/16)+j].y=(-16);
                        }
                    }
                }
            }
        }
        else{
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.GRAY);
        g.fillRect(0,0,1600,1600);

        //draw background layer 0
        try{
            if(MapEditorView.bgLayerCheckBox.getState() == true){
                for(int i = 0; i < 5; i++){
                    g.drawImage(gameMain.background_layer0, gameMain.background_layer0.getWidth(this)*i, 0, this);
                }
            }
        }
        catch(Exception e){
        }

        //draw background layer 1
        try{
            if(MapEditorView.bgLayerCheckBox.getState() == true){
                for(int i = 0; i < 5; i++){
                    g.drawImage(gameMain.background_layer1, gameMain.background_layer1.getWidth(this)*i, gameMain.background_layer1.getHeight(this)/2, this);
                }
            }
        }
        catch(Exception e){
        }

        //draw tiles
        for (int x=0;x<1600;x+=16){
            for (int y=0;y<1600;y+=16){
                g.drawImage(img,x,y,x+16,y+16,tile[x/16][y/16].x,tile[x/16][y/16].y,tile[x/16][y/16].x+16,tile[x/16][y/16].y+16,this);
                //g.setColor(Color.WHITE);
                //g.drawString(tile[x/16][y/16].x+"", x, y+16);
            }
        }

        //draw grid
        for (int x=0;x<1600;x+=16){
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(x,0,x,1600);
            g.drawLine(0,x,1600,x);
        }
    }

    public static void clear(){
        for (int i=0;i<100;i++){
            for (int j=0;j<100;j++){
                tile[i][j].x=-16;
            }
        }
    }

    public static int Height(){
        int h=0;

        for (int x=0;x<1600;x+=16){
            for (int y=0;y<1600;y+=16){
                if (tile[x/16][y/16].x != -16){
                    if (h<y){
                        h=y;
                    }
                }
            }
        }

        return h;
    }

    public static int Width(){
        int h=0;

        for (int x=0;x<1600;x+=16){
            for (int y=0;y<1600;y+=16){
                if (tile[x/16][y/16].x != -16){
                    if (h<x){
                        h=x;
                    }
                }
            }
        }

        return h;
    }
}
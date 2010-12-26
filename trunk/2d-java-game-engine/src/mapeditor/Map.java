/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener.*;

import engine.game.gameMain;
import java.awt.image.VolatileImage;

/**
 *
 * @author Basti
 */

public class Map extends JPanel implements Runnable {


    //public static int mousex=0;
    //public static int mousey=0;
    
    Thread main;
    public static Image img = MapEditor.tiles;
    public static Tiles[][] tile = new Tiles[999][999];
    public static int maxWidth=0;
    public static int maxHeight=0;
    public static int fillx;

    public boolean magic=true;

    MouseKlick mb = new MouseKlick();
    MouseMotion m = new MouseMotion();
    

    public Map(){

        for (int i=0;i<999;i++){
            for (int j=0;j<999;j++){
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
            if (getPreferredSize() != new Dimension(maxWidth,maxHeight)){
                setPreferredSize(new Dimension(maxWidth,maxHeight));
            }
            addThing();
            repaint();
        }
    }

    //set new MapSize
    public static void setMapSize(int a, int b){
        maxWidth = a;
        maxHeight = b;
    }

    //add a tile/sprite if a mouseButton is pressed
    public void addThing(){
        if (Toolbox.ToolboxTab.getSelectedIndex()==0){

            if (mb.getButton()==1){
                img = MapEditor.tiles;
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
        
        Graphics2D g2d = (Graphics2D)g.create();

        super.paintComponent(g2d);

        AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);

        g2d.setColor(Color.GRAY);
        g2d.fillRect(0,0,maxWidth,maxHeight);

        //draw background layer 0
        try{
            if(MapEditor.bgLayerCheckBox.getState() == true){
                for(int i = 0; i < 5; i++){
                    g2d.drawImage(gameMain.background_layer0, gameMain.background_layer0.getWidth(this)*i, 0, this);
                }
            }
        }
        catch(Exception e){
        }

        //draw background layer 1
        try{
            if(MapEditor.bgLayerCheckBox.getState() == true){
                for(int i = 0; i < 5; i++){
                    g2d.drawImage(gameMain.background_layer1, gameMain.background_layer1.getWidth(this)*i, gameMain.background_layer1.getHeight(this)/2, this);
                }
            }
        }
        catch(Exception e){
        }

        //draw tiles
        for (int x=0;x<maxWidth;x+=16){
            for (int y=0;y<maxHeight;y+=16){
                g2d.drawImage(tile[x/16][y/16].tileImage,x,y,x+16,y+16,tile[x/16][y/16].x,tile[x/16][y/16].y,tile[x/16][y/16].x+16,tile[x/16][y/16].y+16,this);
                //g2d.setColor(Color.WHITE);
                //g2d.drawString(tile[x/16][y/16].x+"", x, y+16);
            }
        }

        //draw grid
        if(MapEditor.gridCheckBox.getState() == true){
            //g2d.setColor(new Color(1f, 1f, 1f, 0.5f));
            g2d.setColor(Color.lightGray);
            for (int x=0;x<maxWidth;x+=16){
                g2d.drawLine(x,0,x,maxHeight);
            }
            for (int y=0;y<maxHeight;y+=16){
                g2d.drawLine(0,y,maxWidth,y);
            }
        }

        //draw camera
        if(MapEditor.cameraCheckBox.getState() == true){
            g2d.setColor(new Color(1.0f,0f,0f, 0.33f));
            g2d.fillRect(0, MapEditor.cameraPrefHeight - MapEditor.cameraTolerance/2, maxWidth, MapEditor.cameraTolerance);
            g2d.setColor(Color.red);
            g2d.drawLine(0, MapEditor.cameraPrefHeight, maxWidth, MapEditor.cameraPrefHeight);
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

        for (int x=0;x<maxWidth;x+=16){
            for (int y=0;y<maxHeight;y+=16){
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

        for (int x=0;x<maxWidth;x+=16){
            for (int y=0;y<maxHeight;y+=16){
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
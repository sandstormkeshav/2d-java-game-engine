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
    public static int maxWidth=0;
    public static int maxHeight=0;
    public static int fillx;
    public static EditorObject[] object = new EditorObject[99999];
    public static Image[] background_layer;
    public static EditorObject[] selectedObject;

    private boolean update = false;

    private Rectangle selectionRect = null;
    private EditorObject[] selectionObjects = null;
    private Point selectedPoint = null;
    private boolean clickingSelection = false;
    private static EditorObject[] objectTemp = null;
    private static boolean moving = false;

    public boolean magic=true;

    MouseKlick mouseClickListener = new MouseKlick();
    MouseMotion mouseMotionListener = new MouseMotion();

    VolatileImage grid = this.createVolatileImage(maxWidth, maxHeight);
    VolatileImage objectLayer = this.createVolatileImage(maxWidth, maxHeight);

    public Map(){

        setPreferredSize(new Dimension(1000,1000));
        this.setDoubleBuffered(true);

        this.addMouseListener(mouseClickListener);        
        this.addMouseMotionListener(mouseMotionListener);

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
            MouseActions(new Point((int)mouseMotionListener.getX(),(int)mouseMotionListener.getY()));
            repaint();
        }
    }

    //set new MapSize
    public void setMapSize(int a, int b){
        maxWidth = a;
        maxHeight = b;
        createGrid(a+1,b+1);
    }

    public void createGrid(int width, int height){
        // create hardware accellerated grid
        // non hw accellereated grid was drawing too slow because of transparency
        grid = this.getGraphicsConfiguration().createCompatibleVolatileImage(width, height, Transparency.TRANSLUCENT);
        Graphics2D g2d = grid.createGraphics();
        g2d.setComposite(AlphaComposite.Src);

        // Clear the image.
	g2d.setColor(new Color(0,0,0,0));
	g2d.fillRect(0, 0, grid.getWidth(), grid.getHeight()); 
        g2d.setBackground(new Color(0,0,0,0));
        
        g2d.setColor(new Color(1f, 1f, 1f, 0.5f));
        for (int x=0;x<width;x+=16){
            g2d.drawLine(x-1,0,x-1,height-1);
        }
        for (int y=0;y<height;y+=16){
            g2d.drawLine(0,y-1,width-1,y-1);
        }
    }
    
    public void renderObjectLayer(){
        
	objectLayer = this.getGraphicsConfiguration().createCompatibleVolatileImage(maxWidth*16, maxHeight*16, Transparency.TRANSLUCENT);
        Graphics2D g2d = objectLayer.createGraphics();

        // Clear the image.
        g2d.setColor(new Color(0,0,0,0));
	g2d.fillRect(0, 0, grid.getWidth(), grid.getHeight()); 
        g2d.setBackground(new Color(0,0,0,0));
        
        //draw objects
        int a = 0;
        while(object[a] != null){
            object[a].draw(g2d, this);
            a++;
        }

    }
    
    public void MouseActions(Point p){

        // Object drawing
        if (MapEditor.drawMode == MapEditor.DRAW && MapEditor.selectedObject != null){

            if (mouseClickListener.getButton() == 1){

                Point p16 = new Point(p.x/16, p.y/16);

                if(!MapEditor.selectedObject.name.equals("WorldTile")){
                    object[getObjectNumberAt(p16)] = new EditorObject(MapEditor.selectedObject, p16);
                }
                else{
                    object[getObjectNumberAt(p16)] = new EditorObject(MapEditor.selectedObject, p16, Integer.parseInt(""+MapEditor.selectedObject.objectChar));
                }

            }

        }

        // Object selection
        if(Toolbox.ToolboxTab.getSelectedIndex() == 0 && MapEditor.drawMode == MapEditor.SELECT){
            if(mouseClickListener.getButtonState(1) == true){
                // create selection if not clicking a selecion
                if(object[getObjectNumberAt(new Point(p.x/16, p.y/16))] != null){
                    if(object[getObjectNumberAt(new Point(p.x/16, p.y/16))].selected == true){
                        clickingSelection = true;
                    }
                }
                if(clickingSelection == false){
                    if(mouseClickListener.initialClick.x >= p.x && mouseClickListener.initialClick.y >= p.y)selectionRect = new Rectangle(p.x, p.y, mouseClickListener.initialClick.x - p.x , Math.abs(mouseClickListener.initialClick.y - p.y));
                    if(mouseClickListener.initialClick.x >= p.x && mouseClickListener.initialClick.y < p.y)selectionRect = new Rectangle(p.x, mouseClickListener.initialClick.y, mouseClickListener.initialClick.x - p.x ,  Math.abs(p.y - mouseClickListener.initialClick.y));
                    if(mouseClickListener.initialClick.x < p.x && mouseClickListener.initialClick.y >= p.y)selectionRect = new Rectangle(mouseClickListener.initialClick.x, p.y, p.x - mouseClickListener.initialClick.x,  Math.abs(mouseClickListener.initialClick.y - p.y));
                    if(mouseClickListener.initialClick.x < p.x && mouseClickListener.initialClick.y < p.y)selectionRect = new Rectangle(mouseClickListener.initialClick.x, mouseClickListener.initialClick.y, p.x - mouseClickListener.initialClick.x,  Math.abs(p.y - mouseClickListener.initialClick.y));

                }
                //move object if clicking a selection
                else{
                    moving = true;
                    int a = 0;
                    while(object[a] != null){
                        if(object[a].selected){
                            object[a].position = new Point(objectTemp[a].position.x + p.x/16 - mouseClickListener.initialClick.x/16, objectTemp[a].position.y - mouseClickListener.initialClick.y/16 + p.y/16);
                        }
                        a++;
                    }
                }

            }
            else{
                if(selectionRect != null){
                    
                    // set point
                    selectedPoint = new Point(selectionRect.x/16, selectionRect.y/16);
                    
                    // deselect all
                    selectionObjects = new EditorObject[99999];
                    int a = 0;
                    while(object[a] != null){
                        object[a].selected = false;
                        a++;
                    }
                    // select objects

                    // get selected fields
                    a = 0;
                    for(int x = selectionRect.x/16; x < selectionRect.x/16 + selectionRect.width/16 + 1; x++){
                        for(int y = selectionRect.y/16; y < selectionRect.y/16 + selectionRect.height/16 + 1; y++){
                            int n = getObjectNumberAt(new Point(x,y));
                            // add to selection if there is an object
                            if(object[n] != null){
                                selectionObjects[a] = object[n];
                                object[n].selected = true;
                                a++;
                            }
                        }
                    }
                    
                }
                
                objectTemp = new EditorObject[99999];
                
                int a = 0;
                while(object[a] != null){
                    objectTemp[a] = new EditorObject(new GameObject(object[a].name, object[a].objectChar), object[a].position);
                    a++;
                }
                
                selectionRect = null;
                clickingSelection = false;
                
            }
        }
        
        // Camera selection
        if(Toolbox.ToolboxTab.getSelectedIndex() == 1 && MapEditor.drawMode == MapEditor.SELECT){

            if(mouseClickListener.getButtonState(1) == true){
                
                if(new Rectangle(0, MapEditor.cameraPrefHeight - MapEditor.cameraTolerance/2, maxWidth, MapEditor.cameraTolerance).contains(p)){
                     MapEditor.cameraPrefHeight = Integer.parseInt(Toolbox.camPrefHeightSpinner.getValue().toString()) + p.y - mouseClickListener.initialClick.y;
                     update = true;
                }
            
            }
            else{
                if(update == true){
                    Toolbox.camPrefHeightSpinner.setValue(MapEditor.cameraPrefHeight);
                    update = false;
                }
            }
        }

        // Rubber mode
        if(MapEditor.drawMode == MapEditor.ERASE){
            if (mouseClickListener.getButton() == 1){
                Point p16 = new Point(p.x/16, p.y/16);
                int objectNumber = getNumberOfObjects();
                for(int i = getObjectNumberAt(p16); i < objectNumber; i++){
                    object[i] = object[i+1];
                }
                object[objectNumber] = null;
            }
        }

    }

    public int getNumberOfObjects(){
        int a = 0;
        while(object[a] != null){
            a++;
        }
        return a;
    }

    public static int getObjectNumberAt(Point p){

        int a = 0;

        while(object[a] != null){
            if(object[a].position.equals(p)){
                return a;
            }
            a++;
        }

        return a;

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
            for(int i = 0; i < background_layer.length; i ++){
                if(MapEditor.bgLayerCheckBox.getState() == true){
                    for(int w = 0; w < ((MapEditor.loadedLevel.getWidth()*16)/background_layer[i].getWidth(this))+1; w++){
                        g2d.drawImage(background_layer[i], background_layer[i].getWidth(this)*w, 0, this);
                    }
                }
            }

        }
        catch(Exception e){
        }
        
        
        //draw objects
        int a = 0;
        while(object[a] != null){
            object[a].draw(g, this);
            a++;
        }
        

        //draw grid
        if(MapEditor.gridCheckBox.getState() == true){
            g2d.drawImage(grid, 0, 0, this);
        }

        //draw selection
        if(selectionRect != null){
            g2d.setColor(new Color(0.4f,0.4f,1f));
            g2d.drawRect(selectionRect.x, selectionRect.y, selectionRect.width, selectionRect.height);
            g2d.setColor(new Color(0.2f,0.2f,1,0.25f));
            g2d.fillRect(selectionRect.x, selectionRect.y, selectionRect.width, selectionRect.height);
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
        //TODO: add new clear method for EditorObject class
    }    
}
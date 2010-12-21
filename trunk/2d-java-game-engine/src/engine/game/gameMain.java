/*
 * MainWindow.java
 *
 * Created on 07.12.2010, 02:40:11
 */

package engine.game;

import engine.game.objects.*;
import engine.game.objects.Mario;
import mapeditor.*;

import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;


/**
 *
 * @author Philipp
 */
public class gameMain extends JPanel implements Runnable {

    //Debug Options
    public static boolean showSpritePos = false;
    public static boolean showSpriteNum = false;

    //Key Mapping
    public static boolean[] keyPressed = new boolean[99999];
    public static boolean[] keyReleased = new boolean[99999];

    //Mouse
    MouseMotion mouse = new MouseMotion();
    MouseKlick button = new MouseKlick();

    //Threads
    public Thread main;

    //Images
    public static Image marioSpriteSheet;
    public static Image boxSpriteSheet;
    public static Image coinSpriteSheet;
    public static Image tileSheet;
    public static Image background_layer0;
    public static Image background_layer1;

    //All kinds of Sprites:
    public static int numberOfSprites;
    public static Sprite[] sprite = new Sprite[99999];
    public static Image[] spriteImage = new Image[99999];

    //All kinds of Tiles:
    public static int numberOfTiles;
    public static Sprite[] tile = new Sprite[99999];
    public static Image[] tileImage = new Image[99999];

    //Objects
    public static Mario mario;
    public static PopupCoin pCoin;
    public static WorldTile[] tileObject = new WorldTile[99999];
    public static ItemContainer[] box = new ItemContainer[99999];
    public static Coin[] coin = new Coin[99999];
    public static int numberOfBoxes;
    public static int numberOfCoins;
    public static int collectedCoins = 0;

    //File input stream
    StringBuffer strBuffer = new StringBuffer();

    //Window properties:
    public static int width;;
    public static int height;

    //Levels:
    public static Level loadedLevel = new Level("");
    public static Level test = new Level("test.level");

    //Cameras
    public static Camera camera;

    public gameMain(){
            this.setDoubleBuffered(true);
            this.addKeyListener(new KeyPressListener());
            this.setFocusable(true);
            main = new Thread(this);
            main.start();
    }

    public void initialize(){

        //load images:
        try{
            marioSpriteSheet = ImageIO.read(new File("mario.gif"));
            boxSpriteSheet = ImageIO.read(new File("itemContainer.png"));
            coinSpriteSheet = ImageIO.read(new File("coin.PNG"));
        }
            catch(Exception e){
        }

        //load level:
        test.levelArchive = "test.level";

        while(test.load() != true){}
        //clean up:
        test.clean();

        loadedLevel = test;

        pCoin = new PopupCoin(new Point(-80,-80));

        // -- create objects:
        //create a Mario:   (should be included in the tile/sprite loader, later)
        mario = new Mario(new Point(5, 0));

        //set up the camera:
        camera = new Camera(new Point(width/2, height/2), new Rectangle(0, 0, test.mapWidth*16, (test.mapHeight)*16 - height + 4*16));
        camera.setPrefHeight(test.mapHeight*10 + mario.sprite.size.height, 50);
        camera.position.y = test.mapHeight*10 + 50;
        camera.position.x = width/2;
        
        System.out.println();

    }

    // -- Main Loop
    public void run(){

        //called only once:
        initialize();

        //start main loop:
        while(true){

            camera.follow(mario.sprite);

            //actions for objects:
            mario.keyActions();
            pCoin.fly();

            for(int i = 0; i < numberOfBoxes; i++){
                try{
                    box[i].open();
                }
                catch(Exception e){
                    System.out.println("ERROR: " + e);
                }
            }
            for(int i = 0; i < numberOfCoins; i++){
                try{
                    coin[i].collect();
                }
                catch(Exception e){
                    System.out.println("ERROR: " + e);
                }
            }

            //reset mario if fallen off from screen:
            if(mario.sprite.posy > test.mapHeight*16 ){
                camera.position = new Point(width/2, camera.prefHeight + camera.tolerance);
                mario.sprite.setPosition(5, 0);
            }

            try{
                repaint();
                main.sleep(7L);
            }
            catch(Exception e){

            }

        }

    }


    private class KeyPressListener extends KeyAdapter {

        //key down event:
        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            if(keyPressed[key] == false){
                keyPressed[key] = true;
            }

        }

        //key up event:
        @Override
        public void keyReleased(KeyEvent e){
            int key = e.getKeyCode();
            keyReleased[key] = true;
            keyPressed[key] = false;
        }

    }

    //Draw elements:
    @Override
    public void paint(Graphics g){
        //Set up Graphics Enigne:
        Graphics2D g2d = (Graphics2D)g;

        //Enable Antialiasing:
        RenderingHints rh =
            new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                               RenderingHints.VALUE_ANTIALIAS_ON);

        rh.put(RenderingHints.KEY_RENDERING,
             RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        
        try{

            //Draw background_layer0:
            for(int i = 0; i < 5; i++){
                g2d.drawImage(background_layer0, i*background_layer0.getWidth(this) - (int)(camera.position.x * 0.25), 0 - background_layer0.getHeight(this)/8, this);
            }
            //Draw background_layer1:
            for(int i = 0; i < 5; i++){
                g2d.drawImage(background_layer1, i*background_layer1.getWidth(this) - (int)(camera.position.x * 0.5), (int)(camera.prefHeight*0.75) -(int)(camera.position.y * 0.5), this);
            }
        }

        catch(Exception e){
        }
        
        
        //Draw all kinds of Sprites:
        for(int i = 0; i < numberOfSprites; i++){

            try{
                //Play Animation for sprite:
                if(sprite[i].animation.plays == true){
                    sprite[i].getAnimation().nextFrame();
                }
                
                // -- Draw sprite:
                g2d.drawImage(sprite[i].img,
                /*X1*/sprite[i].posx + ((sprite[i].flipH - 1)/(-2))*sprite[i].size.width /*camera*/ - camera.position.x + camera.center.x,/*Y1*/ sprite[i].posy + ((sprite[i].flipV - 1)/(-2))*sprite[i].size.height /*camera*/ - camera.position.y + camera.center.y,
                /*X2*/sprite[i].posx + sprite[i].size.width*sprite[i].flipH + ((sprite[i].flipH - 1)/(-2))*sprite[i].size.width /*camera*/ - camera.position.x + camera.center.x,/*Y2*/sprite[i].posy+sprite[i].size.height*sprite[i].flipV + ((sprite[i].flipV - 1)/(-2))*sprite[i].size.height /*camera*/ - camera.position.y + camera.center.y, // destination
                sprite[i].getAnimation().col*sprite[i].size.width, sprite[i].getAnimation().row*sprite[i].size.height, // source
                (sprite[i].getAnimation().col+1)*sprite[i].size.width, (sprite[i].getAnimation().row+1)*sprite[i].size.height,
                this);

            }
            catch(Exception e){
                g2d.drawString("Error drawing a Sprite", 20, 20);
            }


        }
        //Draw Tiles:
        for(int i = 0; i < numberOfTiles; i++){
            //apply camera modifiers:

            try{

                //Draw tile:
                g2d.drawImage(tile[i].img,
                /*X1*/tile[i].posx + ((tile[i].flipH - 1)/(-2))*tile[i].size.width /*camera*/ - camera.position.x + camera.center.x,/*Y1*/tile[i].posy + ((tile[i].flipV - 1)/(-2))*tile[i].size.height /*camera*/ - camera.position.y + camera.center.y,
                /*X2*/tile[i].posx+tile[i].size.width*tile[i].flipH+((tile[i].flipH - 1)/(-2))*tile[i].size.width /*camera*/ - camera.position.x + camera.center.x,/*Y2*/tile[i].posy+tile[i].size.height*tile[i].flipV + ((tile[i].flipV - 1)/(-2))*tile[i].size.height /*camera*/ - camera.position.y + camera.center.y, // destination
                tile[i].getAnimation().col*tile[i].size.width, tile[i].getAnimation().row*tile[i].size.height, // source
                (tile[i].getAnimation().col+1)*tile[i].size.width, (tile[i].getAnimation().row+1)*tile[i].size.height,
                this);

            }
            catch(Exception e){
                g2d.drawString("Error drawing a Tile", 20, 20);
            }
        }

        //Draw "GUI":
        g2d.drawImage(coinSpriteSheet,16,16,32,32,0,48,16,64,this);
        g2d.setColor(Color.BLACK);
        g2d.drawString("x "+collectedCoins, 32, 30);
        g2d.setColor(Color.WHITE);
        g2d.drawString("x "+collectedCoins, 32, 29);
        //Debug things:
        for(int i = 0; i < numberOfSprites; i++){
                if(showSpritePos == true){
                    g2d.setColor(Color.red);
                    g2d.drawRect(/*X1*/sprite[i].posx /*camera*/ - camera.position.x + camera.center.x,/*Y1*/ sprite[i].posy /*camera*/ - camera.position.y + camera.center.y, 1, 1);
                    g2d.setColor(Color.black);
                }

                if(showSpriteNum == true){
                    g2d.setColor(Color.black);
                    g2d.drawString("" + i, /*X1*/sprite[i].posx /*camera*/ - camera.position.x + camera.center.x,/*Y1*/ sprite[i].posy /*camera*/ - camera.position.y + camera.center.y);
                    g2d.setColor(Color.white);
                    g2d.drawString("" + i, /*X1*/sprite[i].posx /*camera*/ - camera.position.x + camera.center.x,/*Y1*/ sprite[i].posy /*camera*/ - camera.position.y + camera.center.y - 1);

                }
        }
        for(int i = 0; i < numberOfTiles; i++){
                if(showSpritePos == true){
                    g2d.setColor(Color.red);
                    g2d.drawRect(/*X1*/tile[i].posx + ((tile[i].flipH - 1)/(-2))*tile[i].size.width /*camera*/ - camera.position.x + camera.center.x,/*Y1*/tile[i].posy + ((tile[i].flipV - 1)/(-2))*tile[i].size.height /*camera*/ - camera.position.y + camera.center.y, 1, 1);
                    g2d.setColor(Color.black);
                }
        }

    }
}

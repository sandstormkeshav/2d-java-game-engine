/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainWindow.java
 *
 * Created on 07.12.2010, 02:40:11
 */

package engine.game;

import engine.game.objects.*;
import engine.game.objects.Mario;

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

    //debug String
    String debug = "v. 0.01";

    private Image dbImage;
    private Graphics dbg;

    //Key Mapping
    private boolean[] keyPressed = new boolean[99999];
    private boolean[] keyReleased = new boolean[99999];

    private int keyUp = 38;
    private int keyDown = 40;
    private int keyLeft = 37;
    private int keyRight = 39;
    private int keyJump = 32;

    //Threads
    public Thread main;

    //Images
    public static Image marioSpriteSheet;
    public static Image boxSpriteSheet;
    public static Image tileSheet;
    public static Image background;

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
    public static WorldTile[] tileObject = new WorldTile[99999];
    public static ItemContainer[] box = new ItemContainer[99999];
    public static int numberOfBoxes;

    //File input stream
    StringBuffer strBuffer = new StringBuffer();
    public static String appDir;

    //Window properties:
    public static int width;;
    public static int height;

    //Map properties:
    public int MapHeight = 0;
    public int MapWidth = 0;

    //Cameras
    Camera camera;

    public gameMain(){
            this.setDoubleBuffered(true);
            this.addKeyListener(new KeyPressListener());
            this.setFocusable(true);
            main = new Thread(this);
            main.start();
    }

    public void initialize(){

        // -- load images:
        appDir = System.getProperty("user.dir");

        try{
        marioSpriteSheet = ImageIO.read(new File("mario.gif"));
        tileSheet = ImageIO.read(new File("marioworld.png"));
        boxSpriteSheet = ImageIO.read(new File("box.png"));
        background = ImageIO.read(new File("background.png"));

        }
        catch(Exception e){
        }

        //wait 'till images are loaded:
        while(marioSpriteSheet.getWidth(this) == -1){}
        while(tileSheet.getWidth(this) == -1){}
        while(background.getWidth(this) == -1){}
        while(boxSpriteSheet.getWidth(this) == -1){}

        //create world from from .level file:
        LoadTiles("test.level");

        // -- create objects:
        //create a Mario:   (should be included in the tile/sprite loader, later)
        mario = new Mario(new Point(5, 0));

        //set up the camera:
        camera = new Camera(new Point(width/2, height/2), new Rectangle(0, 0, MapWidth*16, (MapHeight)*16 - height + 4*16));
        camera.setPrefHeight(MapHeight*10 + mario.sprite.size.height, 50);
        camera.position.y = MapHeight*10 + 50;
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

            mario.sprite.setAnimation(mario.none);

            //Up Arrow Down:
            if(keyPressed[keyUp] == true){
                mario.look();
            }

            //Right Arrow Down:
            if(keyPressed[keyRight] == true && keyPressed[keyLeft] == false && (keyPressed[keyDown] == false || mario.Jumping == true || mario.canJump == false)){
                //Flip Sprite if needed:
                if(mario.sprite.flipH != 1){
                    mario.sprite.FlipHorizontal();
                }
                mario.walk();
            }

            //Left Arrow Down:
            if(keyPressed[keyLeft] == true && keyPressed[keyRight] == false && (keyPressed[keyDown] == false || mario.Jumping == true || mario.canJump == false)){
                //Flip Sprite if needed:
                if(mario.sprite.flipH != -1){
                    mario.sprite.FlipHorizontal();
                }
                mario.walk();
            }

            //Spacebar Down:
            if(keyPressed[keyJump] == true){
                mario.jump();
            }

            mario.fall();

            //Down Arrow Down:
            if(keyPressed[keyDown] == true){
                mario.duck();
            }

            if(keyReleased[keyUp] == true){
                mario.stand();
                keyReleased[keyUp] = false;
            }

            //Right/Left Arrow Up:
            if(keyReleased[keyLeft] == true){
                mario.stand();
                keyReleased[keyLeft] = false;
            }

            if(keyReleased[keyRight] == true){
                mario.stand();
                keyReleased[keyRight] = false;
            }

            if(keyReleased[keyDown] == true){
                //"reset collision size:"
                mario.stand();

                //move mario up by 4 pixel:
                if(mario.Jumping == false){
                    mario.sprite.setPosition(mario.sprite.posx, mario.sprite.posy-4);
                }

                keyReleased[keyDown] = false;
            }

            //Spacebar Up:
            if(keyReleased[keyJump]){
                mario.Jumping = false;
                //mario.canJump = true;
                keyReleased[keyJump] = false;
            }

            //Reset Mario if fallen off from screen:
            if(mario.sprite.posy > MapHeight*16 ){
                camera.position = new Point(width/2, camera.prefHeight + camera.tolerance);
                mario.sprite.setPosition(5, 0);
            }

            //Periodic Check Abilities:
            for(int i = 0; i < numberOfBoxes; i++){
                box[i].open();
            }

            //Wrap screen: (buggy with camera bounds ON
            //if(mario.sprite.posx > this.getWidth()){
            //    mario.sprite.posx -= this.getWidth() + 24;
            //}

            //if(mario.sprite.posx < -24){
            //    mario.sprite.posx += this.getWidth() + 24;
            //}

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

        //Draw background Layer:
        try{
            g2d.drawImage(background, 0 - (int)(camera.position.x * 0.25), 0 - background.getHeight(this)/8, this);
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

        //Debug: not needed anymore
        //g2d.drawString("Debug: " + debug, 5, 15);

    }

    public void LoadTiles(String tileSheet){

        // -- Load the Text-file:

        String[] readLine = new String[99999];
        int a = 0;

        try{
            FileInputStream fstream = new FileInputStream("test.level");

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            while((readLine[a] = bf.readLine()) != null){
                a++;
            }

            //close input stream:
            in.close();
        }
        catch(Exception e){
        }

        //Text file is now present in Arrays of Strings:
        //Line length represents the screen width * 16

        // -- create Tiles and sprites from Text-file:
        //Tiles:
        int[] tileNumber = new int[99999];
        for(int y = 0; y < a; y++){
            MapHeight++;
            MapWidth = 0;
            for(int x = 0; x < readLine[y].length(); x++){
                //Number entered in the position represents tileNumber;
                //position of the sprite x*16, y*16
                MapWidth ++;
                if(readLine[y].charAt(x) != ' '){
                    if((Integer.parseInt(readLine[y].charAt(x) + "")) == 9){
                        box[numberOfBoxes] = new ItemContainer(new Point(x*16, y*16));
                    }else{
                        tileObject[numberOfTiles] = new WorldTile(Integer.parseInt(readLine[y].charAt(x) + ""));
                        tileObject[numberOfTiles-1].sprite.setPosition(x*16, y*16);
                    }
                }
            }
        }
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.objects;

import java.awt.*;
import game.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Mario extends Actor implements Actable {

    // -- Variables for "Abilites":
    public static int mariosprite;

    public Point spawn;

    public int wait = 0;

    public Mario(Point position) throws IOException {
        
        super(new Sprite(ImageIO.read(new File("Mario.png")), new Dimension(24,24)));

        //set keymapping
        setKeymapping(new Keymapping(new Key[]{
            new Key("keyUp", 38),
            new Key("keyDown", 40),
            new Key("keyLeft", 37),
            new Key("keyRight", 39),
            new Key("keyJump", 32),
        }));

        //set spawn point
        spawn = new Point(position.x, position.y);

        //set position of sprite:
        sprite.posx = position.x;
        sprite.posy = position.y;

        //Sprite Properties:
        sprite.setCollisionSize(new Dimension(8,24));

        //play animation:
        sprite.animation.play();

        //create animations:
        walk = new Animation(sprite, 0, 1, 90, true);
        jump = new Animation(sprite, 1, 0, 0, true, true);
        fall = new Animation(sprite, 2, 0, 0, true, true);
        duck = new Animation(sprite, 3, 0, 0, true);
        look = new Animation(sprite, 4, 0, 0, true);

    }

    public static void newMario(Point p){
        try{
            gameMain.mario = new Mario(p);
            gameMain.sprite[gameMain.numberOfSprites] = gameMain.mario.sprite;
            gameMain.mario.mariosprite = gameMain.numberOfSprites;
            gameMain.numberOfSprites++;
        }
        catch(Exception e){
            System.out.println("ERROR creating new Mario: " + e);
        }
    }

    public void act() {

        try{

            setActiveAnimation();

            stand();
            look("keyUp");
            walk("keyLeft", "keyRight");
            jump("keyJump");
            fall();
            duck("keyDown");

            sprite.setAnimation(getActiveAnimation());

        }
        catch(Exception e){
            
        }
    }

}
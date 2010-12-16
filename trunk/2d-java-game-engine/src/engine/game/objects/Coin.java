/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package engine.game.objects;

import java.awt.*;
import engine.game.*;

/**
 *
 * @author Philipp
 */
public class Coin {

    //boolean for state of container:
    boolean opened = false;
    boolean opening = false;

    int startx;
    int starty;

    //spritesheet Image for sprite creation:
    Image coinSpriteSheet = gameMain.coinSpriteSheet;

    //spritesheet
    public Sprite sprite = new Sprite(coinSpriteSheet, new Dimension(16,16));

    //Animations
    public Animation spinning = new Animation(sprite, 0, 3, 80, true);

    //standard Animations (this should be copy-paste for each Object:
    public Animation none = new Animation(sprite, 0, 0, 0, true);

    public Coin(Point position){
        startx = sprite.posx = position.x;
        starty = sprite.posy = position.y;
        gameMain.numberOfCoins ++;

        //set the sprite up for drawing:
        sprite.setCollisionSize(new Dimension(0,0));
        gameMain.sprite[gameMain.numberOfSprites] = sprite;
        gameMain.numberOfSprites++;

        //set the animation and play it;
        sprite.animation = spinning;
        sprite.animation.play();
    }

    public void collect(){
        //check for Mario Collision:
        opening();
        if(Point.distance(sprite.posx+8,sprite.posy+8,gameMain.mario.x+12,gameMain.mario.y+12)<24){
            opening = true;
        }
    }

    // opening "animation"
    public void opening(){
        
            if (opening == true){
                if (sprite.posy==starty){
                    gameMain.collectedCoins ++;
                }
                sprite.posy --;
                sprite.posy = -80;
            }
        
    }

}
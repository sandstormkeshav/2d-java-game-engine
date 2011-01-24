/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package engine.game.objects;

import java.awt.*;
import engine.game.*;
import mapeditor.GameObject;

/**
 *
 * @author Philipp
 */
public class Coin{

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
        //set the collision properties:
        sprite.setCollisionSize(new Dimension(-1,-1));

        //set the animation and play it;
        sprite.animation = spinning;
        sprite.animation.play();

        startx = sprite.posx = position.x;
        starty = sprite.posy = position.y;
    }

    public static void newCoin(Point p){
        gameMain.coin[gameMain.numberOfCoins] = new Coin(p);
        gameMain.sprite[gameMain.numberOfSprites] = gameMain.coin[gameMain.numberOfCoins].sprite;
        gameMain.numberOfSprites++;
        gameMain.numberOfCoins ++;
    }

    public void collect(){
        //check for Mario Collision:
        opening();
        if(Point.distance(sprite.posx+8,sprite.posy+8,gameMain.mario.sprite.posx+12,gameMain.mario.sprite.posy+12)<16){
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
                if (starty-sprite.posy>16){
                    sprite.posy = -80;
                }
            }
        
    }

}
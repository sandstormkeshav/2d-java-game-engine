/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game.objects;

import java.awt.*;
import game.*;

/**
 *
 * @author Philipp
 */
public class ItemContainer {

    //boolean for state of container:
    boolean opened = false;
    boolean opening = false;

    int startx;
    int starty;

    //spritesheet Image for sprite creation:
    Image boxSpriteSheet = gameMain.boxSpriteSheet;

    //spritesheet
    public Sprite sprite = new Sprite(boxSpriteSheet, new Dimension(16,16));

    //Animations
    public Animation empty = new Animation(sprite, 1, 0, 0, true);
    public Animation full = new Animation(sprite, 0, 3, 80, true);

    //standard Animations (this should be copy-paste for each Object:
    public Animation none = new Animation(sprite, 0, 0, 0, true);

    public ItemContainer(Point position){
        startx = sprite.posx = position.x;
        starty = sprite.posy = position.y;

        //set the animation and play it;
        sprite.animation = full;
        sprite.animation.play();
    }

    public static void newItemContainer(Point p){

        gameMain.box[gameMain.numberOfBoxes] = new ItemContainer(p);
        gameMain.sprite[gameMain.numberOfSprites] = gameMain.box[gameMain.numberOfBoxes].sprite;
        gameMain.numberOfSprites++;
        gameMain.numberOfBoxes++;

    }

    public void open(){
        //check for Mario Collision:
        opening();
        if(sprite.bottomCollision(gameMain.sprite[Mario.mariosprite])){
            if ((starty-sprite.posy)<8){
            opening = true;
            if (opened == false){
                gameMain.pCoin.set(sprite.posx, sprite.posy-16);
                gameMain.collectedCoins ++;
                sprite.animation = empty;
                opened = true;
            }
        }
        }
    }

    // opening "animation"
    public void opening(){
        if ((opening == false)&&(starty>sprite.posy)){
            sprite.posy ++;
        }
        else{
            if (opening == true){
                sprite.posy --;
                if ((starty-sprite.posy)>=8){
                    opening = false;
                }
            }
        }
    }

}

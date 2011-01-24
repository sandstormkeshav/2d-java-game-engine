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
public class PopupCoin {

    //spritesheet Image for sprite creation:
    Image coinSpriteSheet = gameMain.coinSpriteSheet;

    int startx;
    int starty;

    //spritesheet
    public Sprite sprite = new Sprite(coinSpriteSheet, new Dimension(16,16));

    //Animations
    public Animation spinning = new Animation(sprite, 0, 3, 80, true);

    //standard Animations (this should be copy-paste for each Object:
    public Animation none = new Animation(sprite, 0, 0, 0, true);

    public PopupCoin(Point position){
        startx = sprite.posx = position.x;
        starty = sprite.posy = position.y;

        //set the sprite up for drawing:
        gameMain.sprite[gameMain.numberOfSprites] = sprite;
        gameMain.numberOfSprites++;

        //set the animation and play it;
        sprite.animation = spinning;
        sprite.animation.play();
    }

    public static void newPopupCoin(Point p){
        gameMain.pCoin = new PopupCoin(p);
    }

    public void set(int x, int y){
        startx = sprite.posx = x;
        starty = sprite.posy = y;
    }

    public void fly(){
        sprite.posy --;
        if ((starty-sprite.posy)>32){
            sprite.posy = -80;
        }
    }
    
}

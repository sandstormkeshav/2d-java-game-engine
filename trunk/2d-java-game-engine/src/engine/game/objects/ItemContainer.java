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
public class ItemContainer {

    //spritesheet Image for sprite creation:
    Image boxSpriteSheet = gameMain.marioSpriteSheet;

    //spritesheet
    Sprite sprite = new Sprite(boxSpriteSheet, new Dimension(24,24));

    //standard Animations (this should be copy-paste for each Object:
    public Animation none = new Animation(sprite, 0, 0, 0, false);

    public ItemContainer(Point position){

    }

}

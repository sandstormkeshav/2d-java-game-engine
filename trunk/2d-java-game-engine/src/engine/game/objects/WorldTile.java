package engine.game.objects;

import java.awt.*;
import engine.game.*;
import java.applet.Applet;

/**
 *
 * @author Philipp
 */
public class WorldTile extends Applet {

    //Image
    Image tileSheet = gameMain.tileSheet;

    //Actual tile/sprite:
    public Sprite sprite = new Sprite(tileSheet, new Dimension(16,16));;

    //Animations:
    public Animation[] tile = new Animation[9999];

    public WorldTile(int a){
        for(int i = 0; i < tileSheet.getWidth(this)/16 ; i++){
            tile[i] = new Animation(sprite, i, 0, 0, true);
        }
        sprite.setAnimation(tile[a]);

        //set the sprite up for drawing:
        gameMain.tile[gameMain.numberOfTiles] = sprite;
        gameMain.numberOfTiles++;
    }

    public static void newWorldTile(Point p){
    }

     public WorldTile(int a, int x, int y){
        //apply animation:
        for(int i = 0; i < 8; i++){
            tile[i] = new Animation(sprite, i, 0, 0, true);
        }
        sprite.setAnimation(tile[a]);
        
        //apply position:
        sprite.setPosition(x, y);

        //set the sprite up for drawing:
        gameMain.tile[gameMain.numberOfTiles] = sprite;
        gameMain.numberOfTiles++;
    }

    public Sprite getSprite(){
        return sprite;
    }
}

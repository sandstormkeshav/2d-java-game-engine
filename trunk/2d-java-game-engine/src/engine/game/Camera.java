package engine.game;

import java.awt.*;

/**
 *
 * @author Philipp
 */

public class Camera{

    Point center;
    Point position = new Point(0,0);

    Rectangle bounds;

    int prefHeight = 0;
    int tolerance = 0;

    double zoom = 1;

    public Camera(Point center, Rectangle bounds){
        //center should be screen center
        this.center = center;
        this.bounds = bounds;
    }

    public void setPrefHeight(int prefHeight, int tolerance){
        this.prefHeight = prefHeight;
        this.tolerance = tolerance;
    }

    public void setCenter(Point newCenter){
        center = newCenter;
    }

    public void setPosition(Point newPosition){
        position = newPosition;
    }

    public void follow(Sprite sprite){
        if(sprite.posx + sprite.size.width/2 - center.x >= bounds.getMinX() && sprite.posx + sprite.size.width/2 + center.x <= bounds.getMaxX()){
            position.x = sprite.posx + sprite.size.width/2;
        }
        //First check if inside of camera bounds:
        if(sprite.posy + sprite.size.height/2 + center.y >= bounds.getMinY() && sprite.posy + sprite.size.height/2 - center.y <= bounds.getMaxY()){
            //check distance from preferred height:
            if(sprite.posy + sprite.size.height/2 - tolerance > prefHeight){
                position.y = sprite.posy + sprite.size.height/2 - tolerance;
            }
            if(sprite.posy + sprite.size.height/2 + tolerance < prefHeight){
                position.y = sprite.posy + sprite.size.height/2 + tolerance;
            }
        }

    }

}

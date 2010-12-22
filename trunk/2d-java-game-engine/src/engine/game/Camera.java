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

        // check if inside of camera bounds:
        if(newPosition.x - center.x >= bounds.getMinX() && newPosition.x + center.x <= bounds.getMaxX()){
            position.x = newPosition.x;
        }
        else{
            if(newPosition.x - center.x < bounds.getMinX()){
                position.x = (int)bounds.getMinX() + center.x;
            }
            if(newPosition.x + center.x > bounds.getMaxX()){
                position.x = (int)bounds.getMaxX() - center.x;
            }
        }

        // check if inside of camera bounds:
        if( newPosition.y + center.y >= bounds.getMinY() && newPosition.y - center.y <= bounds.getMaxY()){

            // check distance from preferred height:
            if(newPosition.y - tolerance > prefHeight){
                position.y = newPosition.y - tolerance;
            }
            if(newPosition.y + tolerance < prefHeight){
                position.y = newPosition.y + tolerance;
            }
        }
    }

    public void follow(Sprite sprite){

        setPosition(new Point(sprite.posx + sprite.size.width/2, sprite.posy + sprite.size.height/2));

    }

}

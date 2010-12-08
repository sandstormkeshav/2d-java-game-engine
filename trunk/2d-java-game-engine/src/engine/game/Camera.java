/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

    double zoom = 1;

    public Camera(Point center, Rectangle bounds){
        //center should be screen center
        this.center = center;
        this.bounds = bounds;
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
        if(sprite.posy + sprite.size.height/2 - center.y >= bounds.getMinY() && sprite.posy + sprite.size.height/2 + center.y <= bounds.getMaxY()){
        //    position.y = sprite.posy + sprite.size.height/2;
        }

    }

}

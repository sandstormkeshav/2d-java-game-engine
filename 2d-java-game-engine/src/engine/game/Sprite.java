package engine.game;

import java.awt.*;

public class Sprite{

    public int posx = 0;
    public int posy = 0;

    public int flipH = 1;
    public int flipV = 1;

    Image img;
    public Animation animation = new Animation(this, 0, 0, 0, false);

    //Sprite Size:
    Dimension size;
    Dimension collisionSize;

    //Sheet Size:
    Dimension sheetSize = new Dimension(24, 24);

    public Sprite(Image image, Dimension Size){
        size = Size;
        img = image;
        collisionSize = size;
    }

    public void setCollisionSize(Dimension dxy){
        collisionSize = dxy;
    }

    public void setPosition(int x, int y){
        posx = x;
        posy = y;
    }

    public boolean collision(Sprite sprite2){
        Rectangle collisionBoundsSprite1 = new Rectangle(new Point(this.posx + this.size.width/2 - this.collisionSize.width/2, this.posy + this.size.height/2 - this.collisionSize.height/2), new Dimension(this.collisionSize.width+1, this.collisionSize.height+1));
        Rectangle collisionBoundsSprite2 = new Rectangle(new Point(sprite2.posx, sprite2.posy), new Dimension(sprite2.collisionSize.width+1, sprite2.collisionSize.height+1));

        if (collisionSize.height>0 && collisionSize.width>0 && sprite2.collisionSize.height>0 && sprite2.collisionSize.width>0){
            return collisionBoundsSprite1.intersects(collisionBoundsSprite2);
        }
        else{
            return false;
        }

    }

    public boolean topCollision(Sprite sprite2){
        Rectangle collisionBoundsSprite2 = new Rectangle(new Point(sprite2.posx, sprite2.posy), new Dimension(sprite2.collisionSize.width+1, sprite2.collisionSize.height+1));
        Rectangle topBounds = new Rectangle(new Point(this.posx + ((this.size.width-2)/2 - this.collisionSize.width/2), this.posy + this.size.height/2 - this.collisionSize.height/2 - 2), new Dimension(this.collisionSize.width+2,1));

        return topBounds.intersects(collisionBoundsSprite2);
    }

    public boolean bottomCollision(Sprite sprite2){
        Rectangle collisionBoundsSprite2 = new Rectangle(new Point(sprite2.posx, sprite2.posy), new Dimension(sprite2.collisionSize.width+1, sprite2.collisionSize.height+1));
        Rectangle bottomBounds = new Rectangle(new Point(this.posx + ((this.size.width-2)/2 - this.collisionSize.width/2), this.posy + this.size.height/2 - this.collisionSize.height/2 + + this.collisionSize.height+2), new Dimension(this.collisionSize.width/2,1));

        return bottomBounds.intersects(collisionBoundsSprite2);
    }

    public boolean leftCollision(Sprite sprite2){
        
        Rectangle collisionBoundsSprite2 = new Rectangle(new Point(sprite2.posx, sprite2.posy), new Dimension(sprite2.collisionSize.width+1, sprite2.collisionSize.height+1));
        Rectangle leftBounds = new Rectangle(new Point(this.posx + (this.size.width-2)/2 - this.collisionSize.width/2 - 1, this.posy + this.size.height/2 - this.collisionSize.height/2), new Dimension(1,this.collisionSize.height-2));
        
        return leftBounds.intersects(collisionBoundsSprite2);

    }

    public boolean rightCollision(Sprite sprite2){

        Rectangle collisionBoundsSprite2 = new Rectangle(new Point(sprite2.posx, sprite2.posy), new Dimension(sprite2.collisionSize.width+1, sprite2.collisionSize.height+1));
        Rectangle rightBounds = new Rectangle(new Point(this.posx + (this.size.width-2)/2 - this.collisionSize.width/2 + this.collisionSize.width+2, this.posy + this.size.height/2 - this.collisionSize.height/2), new Dimension(1,this.collisionSize.height-2));

        return rightBounds.intersects(collisionBoundsSprite2);

    }

    public void FlipHorizontal(){
        if(flipH == 1){
            flipH = -1;
        }else{
            flipH = 1;
        }
    }

    public void FlipVertical(){
        if(flipV == 1){
            flipV = -1;
        }else{
            flipV = 1;
        }
    }

    public void setAnimation(Animation animation){
        this.animation = animation;
    }

    public Animation getAnimation(){
        return animation;
    }

}
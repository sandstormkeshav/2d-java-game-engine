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

public class Zero{

    // -- Variables for "Abilites":
    
    // jump()
        public boolean canJump = true;
        public boolean Jumping = false;
        int maxJumpHeight = 60;
        int startJumpHeight;
        int jumpHeight = 0;
        public static int x;
        public static int y;

        public static int mariosprite;

    //Spritesheet Image for Sprite creation:
    Image zeroSpritesheet = gameMain.marioSpriteSheet;

    //Actual Sprite for Mario:
    public Sprite sprite = new Sprite(zeroSpritesheet, new Dimension(48,48));;

    // -- Animations:
    //Animations for Mario:
    public Animation stand = new Animation(sprite, 0, 5, 90, true);
    public Animation walk = new Animation(sprite, 1, 10, 0, true, true);
    //public Animation fall = new Animation(sprite, 2, 0, 0, true, true);

    //standart animation for all objects:
    public Animation none = new Animation(sprite, 0, 5, 90, false);

    //Key Mapping:
    public Keymapping keymapping = new Keymapping(new Key[]{
            new Key("keyUp", 38),
            new Key("keyDown", 40),
            new Key("keyLeft", 37),
            new Key("keyRight", 39),
            new Key("keyJump", 32),
            });

    public Zero(Point position){
        //set position of sprite:
        x=sprite.posx = position.x;
        y=sprite.posy = position.y;

        //set the sprite up for drawing:
        gameMain.sprite[gameMain.numberOfSprites] = sprite;
        mariosprite = gameMain.numberOfSprites;
        gameMain.numberOfSprites++;

        //Sprite Properties:
        sprite.setCollisionSize(new Dimension(24,47));

        //set default animation:
        sprite.animation = none;

        //play animation:
        sprite.animation.play();
    }

    public static void newZero(Point p){
    }

    public void fall(){
        boolean verticalCollision = false;
        
        for(int i = 0; i < gameMain.numberOfTiles; i++){
            if(sprite.collision(gameMain.tileObject[i].sprite) == true){
                verticalCollision = true;
            }
        }

        for(int i = 0; i < gameMain.numberOfSprites; i++){
            if(sprite.collision(gameMain.sprite[i]) == true && gameMain.sprite[i] != sprite){
                verticalCollision = true;
            }
        }

        if(Jumping == false && verticalCollision == false){
            //set animation:
            //sprite.setAnimation(fall);
            //change vertical position:
            sprite.setPosition(sprite.posx, sprite.posy + 1);
            //disable Jump:
            canJump = false;
        }

        if(Jumping == false && verticalCollision == true){
            canJump = true;
        }
    }

   public void jump(){

        //collision boolean:
        boolean topCollision = false;

        //set animation:
        //sprite.animation =  jump;
        

        if(Jumping == false && canJump == true){
            Jumping = true;
            canJump = false;
            startJumpHeight = 0;
            jumpHeight = 0;
        }
        
        if(Jumping == true){
            jumpHeight++;
            if(startJumpHeight - sprite.posy < maxJumpHeight){
                for(int i = 0; i < gameMain.numberOfTiles; i++){
                    if(sprite.topCollision(gameMain.tileObject[i].sprite) == true){
                        topCollision = true;
                    }
                }
                for(int i = 0; i < gameMain.numberOfSprites; i++){
                    if(sprite.topCollision(gameMain.sprite[i]) == true && gameMain.sprite[i] != sprite){
                        topCollision = true;
                    }
                }
                if(topCollision == false){
                    sprite.posy = sprite.posy-(int)(1 - jumpHeight/46);
                }
                else{
                    Jumping = false;
                    canJump = false;
                }

            }
            else{
                Jumping = false;
            }
            if(jumpHeight == maxJumpHeight){
                Jumping = false;
            }
        }

    }

    public void walk(){
        //collision boolean:
        boolean leftCollision = false;
        boolean rightCollision = false;

        //set animation
        sprite.animation = walk;
        
        sprite.animation.play();
        
        //check for collision:
        for(int i = 0; i < gameMain.numberOfTiles; i++){
            if(sprite.leftCollision(gameMain.tileObject[i].sprite) == true){
                leftCollision = true;
            }
        }
        for(int i = 0; i < gameMain.numberOfSprites; i++){
            if(sprite.leftCollision(gameMain.sprite[i]) == true && gameMain.sprite[i] != sprite){
                leftCollision = true;
            }
        }
        for(int i = 0; i < gameMain.numberOfTiles; i++){
            if(sprite.rightCollision(gameMain.tileObject[i].sprite) == true){
                rightCollision = true;
            }
        }
        for(int i = 0; i < gameMain.numberOfSprites; i++){
            if(sprite.rightCollision(gameMain.sprite[i]) == true && gameMain.sprite[i] != sprite){
                rightCollision = true;
            }
        }
        //change sprite position:
        if(leftCollision == false && sprite.flipH == -1){
            sprite.setPosition(sprite.posx + sprite.flipH, sprite.posy);
        }

        if(rightCollision == false && sprite.flipH == 1){
            sprite.setPosition(sprite.posx + sprite.flipH, sprite.posy);
        }
    }

    public void stand(){
        //set animation
        sprite.animation = none;

        //reset collision size:
        //sprite.setCollisionSize(new Dimension(8, 24));
    }

    public void keyActions(){

        //reset animation:
        sprite.setAnimation(none);

        //Up Arrow Down:
        if(keymapping.keyPressed("keyUp") == true){
            //look();
        }

        //Right Arrow Down:
        if(keymapping.keyPressed("keyRight") == true && keymapping.keyPressed("keyLeft") == false && (keymapping.keyPressed("keyDown") == false || Jumping == true || canJump == false)){
            //Flip Sprite if needed:
            if(sprite.flipH != 1){
                sprite.FlipHorizontal();
            }
            walk();
        }

        //Left Arrow Down:
        if(keymapping.keyPressed("keyLeft") == true && keymapping.keyPressed("keyRight") == false && (keymapping.keyPressed("keyDown") == false || Jumping == true || canJump == false)){
            //Flip Sprite if needed:
            if(sprite.flipH != -1){
                sprite.FlipHorizontal();
            }
            walk();
        }

        //Spacebar Down:
        if(keymapping.keyPressed("keyJump") == true){
            jump();
        }

        fall();

        //Down Arrow Down:
        if(keymapping.keyPressed("keyDown") == true){
            //duck();
        }

        if(keymapping.keyReleased("keyUp") == true){
            stand();
            gameMain.keyReleased[keymapping.getKey("keyUp")] = false;
        }

        //Right/Left Arrow Up:
        if(keymapping.keyReleased("keyLeft") == true){
            stand();
            gameMain.keyReleased[keymapping.getKey("keyLeft")] = false;
        }

        if(keymapping.keyReleased("keyRight") == true){
            stand();
            gameMain.keyReleased[keymapping.getKey("keyRight")] = false;
        }

        if(keymapping.keyReleased("keyDown") == true){
            //"reset collision size:"
            stand();

            //move mario up by 4 pixel:
            if(Jumping == false){
                sprite.setPosition(sprite.posx, sprite.posy-4);
            }

            gameMain.keyReleased[keymapping.getKey("keyDown")] = false;
        }

        //Spacebar Up:
        if(keymapping.keyReleased("keyJump")){
            Jumping = false;
            //mario.canJump = true;
            gameMain.keyReleased[keymapping.getKey("keyJump")] = false;
        }

        x = sprite.posx;
        y = sprite.posy;
    }

}
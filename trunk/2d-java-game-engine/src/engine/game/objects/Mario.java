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

public class Mario{

    // -- Variables for "Abilites":
    
    // jump()
        public boolean canJump = true;
        public boolean Jumping = false;
        int maxJumpHeight = 60;
        int startJumpHeight;
        int jumpHeight = 0;

    //Spritesheet Image for Sprite creation:
    Image marioSpritesheet = gameMain.marioSpriteSheet;

    //Actual Sprite for Mario:
    public Sprite sprite = new Sprite(marioSpritesheet, new Dimension(24,24));;

    // -- Animations:
    //Animations for Mario:
    public Animation walk = new Animation(sprite, 0, 1, 90, true);
    public Animation jump = new Animation(sprite, 1, 0, 0, true, true);
    public Animation fall = new Animation(sprite, 2, 0, 0, true, true);
    public Animation duck = new Animation(sprite, 3, 0, 0, true);
    public Animation look = new Animation(sprite, 4, 0, 0, true);

    //standard Animations (this should be copy-paste for each Object:
    public Animation none = new Animation(sprite, 0, 0, 0, false);

    public Mario(Point position){
        //set position of sprite:
        sprite.posx = position.x;
        sprite.posy = position.y;

        //set the sprite up for drawing:
        gameMain.sprite[gameMain.numberOfSprites] = sprite;
        gameMain.numberOfSprites++;

        //Sprite Properties:
        sprite.setCollisionSize(new Dimension(8,24));

        //set default animation:
        sprite.animation = none;

        //play animation:
        sprite.animation.play();
    }

    public void duck(){
        //set animation:
        sprite.animation = duck;

        //change collision size:
        sprite.setCollisionSize(new Dimension(8, 16));
    }

    public void look(){
        //set animation:
        sprite.animation = look;

        //this "ability" does nothing... yet
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
            sprite.setAnimation(fall);
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
        sprite.animation =  jump;
        

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
        sprite.setCollisionSize(new Dimension(8, 24));
    }

    public Sprite getSprite(){
        return sprite;
    }

}
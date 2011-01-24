package game.objects;

import game.Animation;
import game.Keymapping;
import game.Sprite;
import game.gameMain;
import java.awt.Dimension;

public class Actor extends StaticGameObject {

    //animations:
    public Animation walk = null;
    public Animation jump = null;
    public Animation fall = null;
    public Animation duck = null;
    public Animation look = null;

    //key mapping:
    private Keymapping keymapping = null;

    public Actor(Sprite sprite){
        super(sprite);
        addActor();
    }

    private void addActor(){
        //add this actor to the actor list:
        int a = 0;

        while(gameMain.actor[a] != null){
            a++;
        }

        gameMain.actor[a] = this;
    }

    public void setKeymapping(Keymapping keymapping){
        this.keymapping = keymapping;
    }

    public Keymapping getKeymapping(){
        return keymapping;
    }

    //fields for jump() and fall();
    private boolean canJump = true;
    private boolean Jumping = false;
    private int maxJumpHeight = 80;
    private int startJumpHeight;
    private int jumpHeight = 0;

    public void fall(){

        if(fall == null) System.out.println("falling animation is null");

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
            setActiveAnimation(fall);
            //change vertical position:
            sprite.setPosition(sprite.posx, sprite.posy + 1);
            //disable Jump:
            canJump = false;
        }

        if(Jumping == false && verticalCollision == true && keymapping.keyPressed("keyJump") == false){
            canJump = true;
        }
    }

    public void jump(String keyName) throws Exception {

        if(jump == null){
            throw new Exception("jumping animation is null");
        }
        
        if(getKeymapping().keyPressed(keyName) == true){
            
            //collision boolean:
            boolean topCollision = false;

            if(Jumping == false && canJump == true){
                Jumping = true;
                canJump = false;
                startJumpHeight = 0;
                jumpHeight = 0;
            }

            if(Jumping == true){

                //set animation:
                setActiveAnimation(jump);

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
        else{
            Jumping = false;
        }

    }
    
    //fields for duck()
    boolean ducking = false;

    public void duck(String keyName) throws Exception {
        
        if(duck == null) throw new Exception("ducking animation is null");

        if(getKeymapping().keyPressed(keyName) == true){
            //set animation:
            setActiveAnimation(duck);

            //change collision size:
            sprite.setCollisionSize(new Dimension(8, 16));

            //set ducking true:
            ducking = true;
        }

        if(getKeymapping().keyReleased(keyName) == true && getKeymapping().keyPressed(keyName) == false && ducking == true && Jumping == false){
            sprite.setPosition(sprite.posx, sprite.posy-4);
            ducking = false;
        }
        
    }

    public void look(String keyName) throws Exception {
        
        if(look == null) throw new Exception("looking animation is null");
        
        if(getKeymapping().keyPressed(keyName) == true){
            //set animation:
            setActiveAnimation(look);
        }

    }

    public void walk(String keyNameLeft, String keyNameRight) throws Exception {
        
        if(walk == null) throw new Exception("walking animation is null");
        
        //collision boolean:
        boolean leftCollision = false;
        boolean rightCollision = false;

        if(getKeymapping().keyPressed(keyNameLeft) == true || getKeymapping().keyPressed(keyNameRight) == true){
            
            //set animation:
            setActiveAnimation(walk);
            //play animation:
            sprite.animation.play();

        }

        if(getKeymapping().keyPressed(keyNameLeft) == true){
            
            //flip sprite:
            while(sprite.flipH != -1){
                sprite.FlipHorizontal();
            }

            //check for collision:
            leftCollision = leftCollision();

            //change sprite position:
            if(leftCollision == false && sprite.flipH == -1 && getKeymapping().keyPressed("KeyDown") == false){
                sprite.setPosition(sprite.posx -1 , sprite.posy);
            }
        }
        
        if(getKeymapping().keyPressed(keyNameRight) == true){

            //flip sprite:
            while(sprite.flipH != 1){
                sprite.FlipHorizontal();
            }

            //check for collision:
            rightCollision = rightCollision();

            //change sprite position:
            if(rightCollision == false && getKeymapping().keyPressed("KeyDown") == false){
                sprite.setPosition(sprite.posx +1 , sprite.posy);
            }

        }
    }

    public void stand(){

        //set animation
        setActiveAnimation();

        //reset collision size:
        sprite.setCollisionSize(new Dimension(8, 24));
    }

    private boolean leftCollision(){

        boolean collision = false;

        int a;

        a = 0;
        while(gameMain.tileObject[a] != null){
            if(sprite.leftCollision(gameMain.tileObject[a].sprite) == true){
                collision = true;
            }
            a++;
        }

        a = 0;
        while(gameMain.sprite[a] != null){
            if(sprite.leftCollision(gameMain.sprite[a]) == true && gameMain.sprite[a] != sprite){
                collision = true;
            }
            a++;
        }

        return collision;
    }

    private boolean rightCollision(){

        boolean collision = false;

        int a;

        a = 0;
        while(gameMain.tileObject[a] != null){
            if(sprite.rightCollision(gameMain.tileObject[a].sprite) == true){
                collision = true;
            }
            a++;
        }

        a = 0;
        while(gameMain.sprite[a] != null){
            if(sprite.rightCollision(gameMain.sprite[a]) == true && gameMain.sprite[a] != sprite){
                collision = true;
            }
            a++;
        }

        return collision;
    }

}
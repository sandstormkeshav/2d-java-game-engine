package engine.game;

import java.awt.*;

public class Animation{

    private Image sheet;
    private int frame = 0;

    Sprite sprite;

    int animCol;
    int maxRow;

    int col;
    int row;

    int waitTime;
    int waitCounter;

    boolean loop;
    public boolean flying = false;  //Dont change animation to "none" or "stand" if this is true;
    boolean plays;

    public Animation(Sprite sprite, int animCol, int FrameCount, int speed, boolean loop){
        this.sprite = sprite;
        this.col = animCol;
        this.loop = loop;
        waitTime = 100-speed;
        maxRow = FrameCount;
    }

    public Animation(Sprite sprite, int animCol, int FrameCount, int speed, boolean loop, boolean flying){
        this.sprite = sprite;
        this.col = animCol;
        this.loop = loop;
        waitTime = 100-speed;
        this.flying = flying;

        maxRow = FrameCount;
    }

    public void nextFrame(){

        if(waitCounter == 0){
            if(row < maxRow){
                row += 1;
            }
            else{
                row = 0;
            }
            waitCounter = waitTime;
        }
        else{
            waitCounter--;
        }
    }

    public void play(){
        plays = true;
    }

    public void stop(){
        plays = false;
    }

}

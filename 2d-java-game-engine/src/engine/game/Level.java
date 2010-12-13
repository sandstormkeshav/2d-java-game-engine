package engine.game;

/**
 *
 * @author Philipp
 */

import java.io.*;
import java.awt.*;
import engine.game.objects.*;

public class Level {

    public int mapHeight;
    public int mapWidth;

    public String levelTXT;

    public Level(String levelTXT){
        this.levelTXT = levelTXT;
    }

    //Basic loader:
    //TODO change level file format.
    public boolean load(){

        //Reset numberOf ...
        gameMain.numberOfBoxes = 0;
        gameMain.numberOfSprites = 0;
        gameMain.numberOfTiles = 0;

        //Reset map properties:
        mapHeight = 0;
        mapWidth = 0;

        // -- Read the Text-file:
        String[] readLine = new String[99999];
        int a = 0;

        try{
            FileInputStream fstream = new FileInputStream(levelTXT);

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            while((readLine[a] = bf.readLine()) != null){
                a++;
            }

            //close input stream:
            in.close();
        }
        catch(Exception e){
        }

        //Text file is now present in Arrays of Strings:
        //Line length represents the screen width * 16

        // -- create Tiles and sprites from Text-file:
        //Tiles:
        int[] tileNumber = new int[99999];
        for(int y = 0; y < a; y++){
            mapHeight++;
            mapWidth = 0;
            for(int x = 0; x < readLine[y].length(); x++){
                //Number entered in the position represents tileNumber;
                //position of the sprite x*16, y*16
                mapWidth ++;
                if(readLine[y].charAt(x) != ' '){
                    if((Integer.parseInt(readLine[y].charAt(x) + "")) == 9){
                        gameMain.box[gameMain.numberOfBoxes] = new ItemContainer(new Point(x*16, y*16));
                    }else{
                        gameMain.tileObject[gameMain.numberOfTiles] = new WorldTile(Integer.parseInt(readLine[y].charAt(x) + ""));
                        gameMain.tileObject[gameMain.numberOfTiles-1].sprite.setPosition(x*16, y*16);
                    }
                }
            }
        }

    return true;

    }
}

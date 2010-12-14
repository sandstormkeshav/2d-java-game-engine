package engine.game;

/**
 *
 * @author Philipp
 */

import java.io.*;
import java.awt.*;
import engine.game.objects.*;
import java.util.regex.*;

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
        /*
        Pattern valuePattern = Pattern.compile("[0-9]+");
        Pattern tilePattern = Pattern.compile("<tile>");

        Matcher matchTile = tilePattern.matcher(levelTXT);
        Matcher matchValue = valuePattern.matcher(levelTXT);

        int b = 0;
        int c = 0;
        int[][] value = new int[9999][4];

        while(matchValue.find()){
            if(b > 3){
                b = 0;
                c++;
            }
            value[c][b] = Integer.parseInt(matchValue.group(0));
            System.out.println(value[c][b]);
            b++;
        }
        */
        
        for(int y = 0; y < a; y++){
            mapHeight++;
            mapWidth = 0;
            for(int x = 0; x < readLine[y].length(); x++){
                //Number entered in the position represents tileNumber;
                //position of the sprite x*16, y*16
                mapWidth ++;
                if(readLine[y].charAt(x) != ' '){
                    if(((int)(readLine[y].charAt(x))) == 57){
                        gameMain.box[gameMain.numberOfBoxes] = new ItemContainer(new Point(x*16, y*16));
                    }else{
                        if(((int)(readLine[y].charAt(x))) == 58){
                            gameMain.coin[gameMain.numberOfCoins] = new Coin(new Point(x*16, y*16));
                        }else{
                            gameMain.tileObject[gameMain.numberOfTiles] = new WorldTile(Integer.parseInt(readLine[y].charAt(x) + ""));
                            gameMain.tileObject[gameMain.numberOfTiles-1].sprite.setPosition(x*16, y*16);
                        }
                    }
                }
            }
        }
        

    return true;

    }
}

package engine.game;

/**
 *
 * @author Philipp
 */

import java.io.*;
import java.awt.*;
import engine.game.objects.*;
import java.util.zip.*;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.*;
import mapeditor.*;

public class Level {

    public int mapHeight;
    public int mapWidth;

    public String levelArchive;

    public Level(String levelArchive){
        this.levelArchive = levelArchive;
    }

    public void extractArchive(File archive, File destDir) throws Exception {
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        ZipFile zipFile = new ZipFile(archive);
        Enumeration entries = zipFile.entries();

        byte[] buffer = new byte[16384];
        int len;
        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();

            String entryFileName = entry.getName();

            File dir = dir = buildDirectoryHierarchyFor(entryFileName, destDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            if (!entry.isDirectory()) {
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(new File(destDir, entryFileName)));

                BufferedInputStream bis = new BufferedInputStream(zipFile
                        .getInputStream(entry));

                while ((len = bis.read(buffer)) > 0) {
                    bos.write(buffer, 0, len);
                }

                bos.flush();
                bos.close();
                bis.close();
            }
        }
                zipFile.close();
    }

    private File buildDirectoryHierarchyFor(String entryName, File destDir) {
        int lastIndex = entryName.lastIndexOf('/');
        String entryFileName = entryName.substring(lastIndex + 1);
        String internalPathToEntry = entryName.substring(0, lastIndex + 1);
        return new File(destDir, internalPathToEntry);
    }

    public boolean load() throws Exception{

        //clean up old loads:
        clean();

        //Reset numberOf ...
        gameMain.numberOfBoxes = 0;
        gameMain.numberOfSprites = 0;
        gameMain.numberOfTiles = 0;

        //Reset map properties:
        mapHeight = 0;
        mapWidth = 0;

        //Unpack the level archive:
        try{
            extractArchive(new File(levelArchive), new File("."));
        }
        catch(Exception e){
            throw e;
        }

        try{
            gameMain.tileSheet = ImageIO.read(new File("tilesheet.png"));
            gameMain.background_layer0 = ImageIO.read(new File("bg0.png"));
            gameMain.background_layer1 = ImageIO.read(new File("bg1.png"));
        }
        catch(Exception e){
            throw new Exception("ERROR loading images: " + e);
        }

        // -- Read the level file
        String[] readLine = new String[99999];
        int a = 0;

        try{
            FileInputStream fstream = new FileInputStream("level");

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            while((readLine[a] = bf.readLine()) != null){
                a++;
            }

            //close input stream:
            in.close();
        }
        catch(Exception e){
            throw new Exception("ERROR reading level: " + e);
        }
        //Level file is now present in Arrays of Strings:
        //Line length represents the map width / 16

        for(int y = 0; y < a; y++){
            mapHeight++;
            mapWidth = 0;
            for(int x = 0; x < readLine[y].length(); x++){
                //Number entered in the position represents tileNumber;
                //position of the sprite x*16, y*16
                mapWidth ++;
                if(readLine[y].charAt(x) != ' '){

                    //Load the map into editor:
                    try{
                        Map.tile[x][y].x = (((int)(readLine[y].charAt(x)))-48)*16;
                    }
                    catch(Exception e){
                    }
                    //Load the map into the engine/game
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

        // -- Read the properties file
            String properties = "";

            try {
                BufferedReader in = new BufferedReader(new FileReader("properties"));
                String str;
                while ((str = in.readLine()) != null) {
                    properties += str + "\n";
                }
                in.close();
            }
            catch(Exception e){
                throw new Exception("ERROR reading poperties: " + e);
            }

            //Initialize camera
            gameMain.camera = new Camera(new Point(gameMain.width/2, gameMain.height/2), new Rectangle(0, 0, mapWidth*16, (mapHeight)*16 - gameMain.height));

            //create regex pattern/matcher to parse the string:
            Pattern cameraTolerancePattern = Pattern.compile("cameraTolerance ?= ?[0-9]+");
            Pattern cameraPrefHeightPattern = Pattern.compile("cameraPrefHeight ?= ?[0-9]+");
            Pattern number = Pattern.compile("[0-9]+");

            Matcher cameraToleranceMatcher = cameraTolerancePattern.matcher(properties);
            Matcher cameraPrefHeightMatcher = cameraPrefHeightPattern.matcher(properties);

            String found = "";

            //set camera tolerance
            if(cameraToleranceMatcher.find()){
                found = properties.substring(cameraToleranceMatcher.start(), cameraToleranceMatcher.end());
                Matcher numberMatcher = number.matcher(found);
                if(numberMatcher.find()){
                    try{
                        Toolbox.camToleranceSpinner.setValue(Integer.parseInt(numberMatcher.group(0)));
                    }
                    catch(Exception e){
                    }
                    try{
                        gameMain.camera.tolerance = Integer.parseInt(numberMatcher.group(0));
                    }
                    catch(Exception e){
                    }                   
                }
            }
            else{
                throw new Exception("ERROR camera tolerance not found");
            }

            //set camera preferred height
            if(cameraPrefHeightMatcher.find()){
                found = properties.substring(cameraPrefHeightMatcher.start(), cameraPrefHeightMatcher.end());
                Matcher numberMatcher = number.matcher(found);
                if(numberMatcher.find()){
                    try{
                        gameMain.camera.prefHeight = Integer.parseInt(numberMatcher.group(0));
                    }
                    catch(Exception e){
                    }   
                    try{
                        Toolbox.camPrefHeightSpinner.setValue(Integer.parseInt(numberMatcher.group(0)));
                    }
                    catch(Exception e){
                    }
                }
            }
            else{
                throw new Exception("ERROR cameraPrefHeight not found");
            }

        //Set editor window properties
        try{
            Map.maxWidth = mapWidth*16;
            Map.maxHeight = mapHeight*16;
            MapEditor.MapScrollPane.setPreferredSize(new Dimension(Map.maxWidth,Map.maxHeight));
            Toolbox.mapWidthSpinner.setValue(mapWidth);
            Toolbox.mapHeightSpinner.setValue(mapHeight);
            MapEditor.mapEdit.setSize(MapEditor.maxSize);
        }
        catch(Exception e){
            throw new Exception("ERROR setting-up the editor: "+e);
        }
        
    return true;

    }

    public void clean(){
        new File("bg0.png").delete();
        new File("bg1.png").delete();
        new File("tilesheet.png").delete();
        new File("level").delete();
        new File("properties").delete();
    }
}

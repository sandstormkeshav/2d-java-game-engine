package engine.game;

/**
 *
 * @author Philipp
 */

import java.io.*;
import java.awt.*;
import engine.game.objects.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.zip.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.*;
import mapeditor.*;

public class Level {

    public String levelArchive;
    
    private String properties = "";
    private String objects = "";
    public String level = "";
    
    public Level(String levelArchive){
        this.levelArchive = levelArchive;
        
        //Unpack the level archive:
        try{
            extractArchive(new File(levelArchive), new File("."));
            properties = readTextFile("properties");
            objects = readTextFile("objects");
            level = readTextFile("level");
        }
        catch(Exception e){
            if(e.toString().substring(0, 4).equals("ERROR")){
                System.out.println(e);
            }
        }
    }

    private void extractArchive(File archive, File destDir) throws Exception {
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
        
        System.out.println("this loading method is legacy... use a combination of the other methods instead");
        
        //clean up old loads:
        clean();

        //Reset numberOf ...
        gameMain.numberOfBoxes = 0;
        gameMain.numberOfSprites = 0;
        gameMain.numberOfTiles = 0;

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

        // -- read the object database
        String objectsDB = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader("objects"));
            String str;
            while ((str = in.readLine()) != null) {
                objectsDB += str;
            }
            in.close();
        }
        catch (Exception e) {
            throw new Exception("ERROR reading objects db: " + e);
        }

        String[] tempList = new String[99999];
        int numberOfEntries = 0;

        // search objects db for objects and write them to list:
        Pattern objectNamePattern = Pattern.compile("[a-zA-Z]+");
        Matcher objectNameMatcher = objectNamePattern.matcher(objectsDB);

        while(objectNameMatcher.find()){
            tempList[numberOfEntries] = objectNameMatcher.group();
            numberOfEntries++;
        }
        String[] objectList = new String[numberOfEntries];
        for(int i = 0; i < numberOfEntries; i++){
            objectList[i] = tempList[i];
        }
        
        // create regex pattern to read the db
        Pattern[] objectPattern = new Pattern[objectList.length];
        Matcher[] objectMatcher = new Matcher[objectList.length];
        int[] objectNumber = new int[objectList.length];

        for(int i = 0; i < objectList.length; i++){
            objectPattern[i] = Pattern.compile(objectList[i] + " ?= ?[0-9]+");
            objectMatcher[i] = objectPattern[i].matcher(objectsDB);
            if(objectMatcher[i].find()){
                objectNumber[i] = Integer.parseInt(objectMatcher[i].group(0).replaceAll(objectList[i] + " ?= ?", ""));
            }
        }

        //create gameObjects for the MapEditor:
        GameObject[] gameObjects = new GameObject[objectList.length];

        for(int i = 0; i < objectList.length; i++){
            gameObjects[i] = new GameObject(objectList[i], objectNumber[i]);
        }

        // -- read the level file
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
            for(int x = 0; x < readLine[y].length(); x++){
                //Number entered in the position represents tileNumber;
                //position of the sprite x*16, y*16
                if(readLine[y].charAt(x) != ' '){

                    // Load objects into editor:
                    for(int i = 0; i < objectList.length; i++){
                        if(readLine[y].charAt(x) == (char)objectNumber[i]){
                            try{
                                Map.tile[x][y] = gameObjects[i].getTile();
                            }
                            catch(Exception e){
                                System.out.println("ERROR trying to create object for editor: " + e);
                            }
                        }
                    }

                    // Load tiles into editor:
                    if(((int)(readLine[y].charAt(x))) >= 48 && ((int)(readLine[y].charAt(x))) <= 57){
                        try{
                            Map.tile[x][y].x = (((int)(readLine[y].charAt(x)))-48)*16;
                            Map.tile[x][y].y += 16;
                        }
                        catch(Exception e){
                        }
                    }

                    // Load objects into the engine/game
                    for(int i = 0; i < objectList.length; i++){
                        if(readLine[y].charAt(x) == (char)objectNumber[i]){
                            try{
                                invoke("engine.game.objects." + objectList[i], "new"+objectList[i], new Class[] { Point.class }, new Object[] { new Point (x*16, y*16) });
                            }
                            catch(Exception e){
                                System.out.println("ERROR trying to invoke method: " + e);
                            }
                        }
                    }
                    // Load tiles into engine/game
                    // 48 = '0' , 57 = '9'
                    if( ((int)(readLine[y].charAt(x))) >= 48 && ((int)(readLine[y].charAt(x))) <= 57 ){
                        gameMain.tileObject[gameMain.numberOfTiles] = new WorldTile(Integer.parseInt(readLine[y].charAt(x) + ""));
                        gameMain.tileObject[gameMain.numberOfTiles-1].sprite.setPosition(x*16, y*16);
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
        gameMain.camera = new Camera(new Point(gameMain.width/2, gameMain.height/2), new Rectangle(0, 0, getWidth()*16, (getHeight())*16 - gameMain.height));

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
            Map.maxWidth = getWidth()*16;
            Map.maxHeight = getHeight()*16;
            MapEditor.MapScrollPane.setPreferredSize(new Dimension(Map.maxWidth,Map.maxHeight));
            Toolbox.mapWidthSpinner.setValue(getWidth());
            Toolbox.mapHeightSpinner.setValue(getHeight());
            MapEditor.mapEdit.setSize(MapEditor.maxSize);
        }
        catch(Exception e){
            throw new Exception("ERROR setting-up the editor: "+e);
        }
        
    return true;
        
    }
    
    private String readTextFile(String fileName) throws Exception{
        // Check if file exists:
        if(!(new File(fileName).exists())){
            throw new Exception("ERROR trying to read text: File "+ fileName +" does not exists");
        }
        
        String read = "";
        
        // Read the text:
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            while ((str = in.readLine()) != null) {
                read += str + "\n";
            }
            in.close();
        }
        catch(Exception e){
            throw new Exception("ERROR reading text: " + e);
        }
        
        return read;
    }
    
    public int getWidth(){

        int width = 0;

        while(level.charAt(width) != '\n'){
            width++;
        }

        return width + 1;
    }
    
    public int getHeight(){
        // + 1 iss needed because a line ends always with '\n'
        return level.length()/(getWidth());
    }
    
    public GameObject[] getGameObjects(){

        String[] tempList = new String[99999];
        int numberOfEntries = 0;

        // search objects db for objects and write them to list:
        Pattern objectNamePattern = Pattern.compile("[a-zA-Z]+");
        Matcher objectNameMatcher = objectNamePattern.matcher(objects);

        while(objectNameMatcher.find()){
            tempList[numberOfEntries] = objectNameMatcher.group();
            numberOfEntries++;
        }
        String[] objectList = new String[numberOfEntries];
        for(int i = 0; i < numberOfEntries; i++){
            objectList[i] = tempList[i];
        }
        
        // create regex pattern to read the db
        Pattern[] objectPattern = new Pattern[objectList.length];
        Matcher[] objectMatcher = new Matcher[objectList.length];
        int[] objectNumber = new int[objectList.length];

        for(int i = 0; i < objectList.length; i++){
            objectPattern[i] = Pattern.compile(objectList[i] + " ?= ?[0-9]+");
            objectMatcher[i] = objectPattern[i].matcher(objects);
            if(objectMatcher[i].find()){
                objectNumber[i] = Integer.parseInt(objectMatcher[i].group(0).replaceAll(objectList[i] + " ?= ?", ""));
            }
        }

        //create GameObjects
        GameObject[] gameObjects = new GameObject[objectList.length];

        for(int i = 0; i < objectList.length; i++){
            gameObjects[i] = new GameObject(objectList[i], objectNumber[i]);
        }
        
        return gameObjects;
    }
    
    public Camera getCamera () throws Exception {
        //Initialize camera
        Camera camera = new Camera(new Point(gameMain.width/2, gameMain.height/2), new Rectangle(0, 0, getWidth()*16, (getHeight())*16 - gameMain.height));

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
                camera.tolerance = Integer.parseInt(numberMatcher.group(0));            
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
                camera.prefHeight = Integer.parseInt(numberMatcher.group(0));
            }
        }
        else{
            throw new Exception("ERROR cameraPrefHeight not found");
        }
        
        return camera;     
        
    }
    
    public static void invoke(String aClass, String aMethod, Class[] params, Object[] args) throws Exception {
        Class c = Class.forName(aClass);
        Constructor constructor = c.getConstructor(params);
        Method m = c.getDeclaredMethod(aMethod, params);
        Object i = constructor.newInstance(args);
        Object r = m.invoke(i, args);
    }

    public void clean(){
        new File("bg0.png").delete();
        new File("bg1.png").delete();
        new File("tilesheet.png").delete();
        new File("level").delete();
        new File("properties").delete();
    }

    public static String[] getObjectClasses(){
        String[] classes = new String[9999];
        String jarName = new String();
        String[] ret = new String[0];
        try{
            jarName = new File(Level.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getName();
        }
        catch(Exception e){
        }
        int numberOfClasses = 0;

        String packageName = "engine/game/objects/";
            try{
                JarInputStream jarFile = new JarInputStream(new FileInputStream (jarName));
                JarEntry jarEntry;

                while(true) {
                    jarEntry=jarFile.getNextJarEntry ();
                    if(jarEntry == null){
                        break;
                    }
                    if((jarEntry.getName().startsWith (packageName)) && (jarEntry.getName ().endsWith (".class")) && !(jarEntry.getName().replaceAll(packageName, "").replaceAll(".class", "").equals("WorldTile"))) {
                        classes[numberOfClasses] = jarEntry.getName().replaceAll(packageName, "").replaceAll(".class", "");
                        numberOfClasses++;
                    }
                }
                ret = new String[numberOfClasses];
                for(int i = 0; i < numberOfClasses; i++){
                    ret[i] = classes[i];
                }
            }
            catch( Exception e){
                e.printStackTrace ();
            }
       return ret;
    }

}

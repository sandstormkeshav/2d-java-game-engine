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

        //Unpack the loadedLevel archive:
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

        // search object db for object and write them to list:
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
        Camera camera = new Camera(new Point(gameMain.resolution.width/2, gameMain.resolution.height/2), new Rectangle(0, 0, getWidth()*16, (getHeight())*16 - gameMain.resolution.height));

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

    public static void clean(){
        new File("bg0.png").delete();
        new File("bg1.png").delete();
        new File("tilesheet.png").delete();
        new File("level").delete();
        new File("properties").delete();
        new File("objects").delete();
    }

    public static String[] getObjectClasses() throws Exception{
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
                throw new Exception("ERROR trying to read object list from JAR: " + "Not packed in .jar-file");
            }
       return ret;
    }

}

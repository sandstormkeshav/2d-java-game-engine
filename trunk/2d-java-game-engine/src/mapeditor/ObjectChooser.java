/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeditor;


import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener.*;
import java.io.File;
import javax.imageio.ImageIO;

public class ObjectChooser extends JPanel implements Runnable {

    Thread main;

    private Point selection = new Point(0, 0);
    private Point oldSelection = new Point(0, 0);

    private TileMouse mouse = new TileMouse();

    public GameObject[] objectList = new GameObject[1];
    public EditorObject[] editorObject = new EditorObject[1];

    public boolean noneSelected = true;

    private static int selectedObjectNumber = -1;
    public static GameObject selectedObject = null;

    public ObjectChooser(){

        objectList[0] = new GameObject("",(char)2);
        editorObject[0] = new EditorObject(objectList[0],new Point(0,0));

        setPreferredSize(new Dimension(500,100));
        this.setDoubleBuffered(true);
        this.addMouseListener(mouse);
        main = new Thread(this);
        main.start();
    }

    public void run(){
        while(true){

            oldSelection = selection;
            selection = mouse.MouseLocation;

            if(!oldSelection.equals(selection)){
                getSelectedObject();
            }

            try{
                main.sleep(20L);
            }
            catch(Exception e){
            }

            repaint();
        }
    }

    public void getSelectedObject(){
        if((int)(selection.x/16) < editorObject.length && (int)(selection.y/16) == 0){
            Toolbox.ObjectCharTextField.setText(editorObject[(int)(selection.x/16)].objectChar + "");
            Toolbox.ObjectNameTextField.setText(editorObject[(int)(selection.x/16)].name);
            MapEditor.selectedObject = editorObject[(int)selection.x/16];
            selectedObjectNumber = (int)selection.x/16;
            selectedObject = editorObject[(int)selection.x/16];
            noneSelected = false;
        }
        else{
            Toolbox.ObjectCharTextField.setText("");
            Toolbox.ObjectNameTextField.setText("");
            MapEditor.selectedObject = null;
            selectedObjectNumber = -1;
            selectedObject = null;
            noneSelected = true;
        }
        
    }

    public static int getSelectedObjectNumber(){
        return selectedObjectNumber;
    }

    public void updateObjectChooser() throws IOException{
        
        editorObject = new EditorObject[objectList.length];

        for(int i = 0; i < objectList.length; i++){
            editorObject[i] = new EditorObject(objectList[i], new Point(i, 0));
        }

    }

    public void updateTileChooser() throws IOException{

        Image tilesheet = null;
        try{
            tilesheet = ImageIO.read(new File(Toolbox.tilesheetTextField.getText()));
        }
        catch(Exception e){
            System.out.println("ERROR no tilesheet found");
        }

        editorObject = new EditorObject[tilesheet.getWidth(this)/16];

        for(int i = 0; i < editorObject.length; i++){
            editorObject[i] = new EditorObject(new GameObject("WorldTile", new String(i+"").charAt(0) ), new Point(i, 0), i);
        }

    }

    //draw Tileset
    @Override
     public void paintComponent(Graphics g){
        super.paintComponent(g);
        //draw Objects:
        for(int i = 0; i < editorObject.length; i++){
            editorObject[i].draw(g, this);
        }

        //draw selection if this is clicked
        if(mouse.clickedComponent == this && noneSelected == false){
            g.setColor(new Color(0,0,1,0.25f)); //transparent blue
            g.fillRect((int)(selection.x/16)*16, (int)(selection.y/16)*16, 16, 16);
        }
        
    }

}

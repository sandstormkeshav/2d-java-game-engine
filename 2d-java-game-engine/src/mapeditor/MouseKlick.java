/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeditor;


import java.awt.event.*;
/**
 *
 * @author Basti
 */
public class MouseKlick extends MouseAdapter implements MouseListener{

    public static int Button =0;

    @Override
    public void mousePressed(MouseEvent me){

        Map.img = MapEditorView.tiles;
        Map.img2 = MapEditorView.sprites;

        Button = me.getButton();
        Map.mousex = me.getPoint().x;
        Map.mousey = me.getPoint().y;

        if (MapEditorView.jTabbedPane1.getSelectedIndex()==0){

            if (Button==1){
                for (int i=0;i<=tileChooser.lengthx;i++){
                    for (int j=0;j<=tileChooser.lengthy;j++){
                        Map.tile[(int)(Map.mousex/16)+i][(int)(Map.mousey/16)+j].x=(tileChooser.numbx+i)*16;
                        Map.tile[(int)(Map.mousex/16)+i][(int)(Map.mousey/16)+j].y=(tileChooser.numby+j)*16;
                    }
                }
            }
            else{
                if (Button==3){
                    for (int i=0;i<=tileChooser.lengthx;i++){
                        for (int j=0;j<=tileChooser.lengthy;j++){
                            Map.tile[(int)(Map.mousex/16)+i][(int)(Map.mousey/16)+j].x=(-16);
                            Map.tile[(int)(Map.mousex/16)+i][(int)(Map.mousey/16)+j].y=(-16);
                        }
                    }
                }
            }
        }
        else{

            if (Button==1){
                for (int i=0;i<=spriteChooser.lengthx;i++){
                    for (int j=0;j<=spriteChooser.lengthy;j++){
                        Map.sprite[(int)(Map.mousex/16)+i][(int)(Map.mousey/16)+j].x=(spriteChooser.numbx+i)*16;
                        Map.sprite[(int)(Map.mousex/16)+i][(int)(Map.mousey/16)+j].y=(spriteChooser.numby+j)*16;
                    }
                }
            }
            else{
                if (Button==3){
                    for (int i=0;i<=spriteChooser.lengthx;i++){
                        for (int j=0;j<=spriteChooser.lengthy;j++){
                            Map.sprite[(int)(Map.mousex/16)+i][(int)(Map.mousey/16)+j].x=(-16);
                            Map.sprite[(int)(Map.mousex/16)+i][(int)(Map.mousey/16)+j].y=(-16);
                        }
                    }
                }
            }
        
        }
    }

    @Override
    public void mouseReleased(MouseEvent me){
        Button = 0;
    }
}

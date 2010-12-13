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
public class MouseMove extends MouseMotionAdapter implements MouseMotionListener{
    @Override
    public void mouseMoved(MouseEvent me) {
        Map.mousex = me.getPoint().x;
        Map.mousey = me.getPoint().y;

        if (MouseKlick.Button==1){
            for (int i=0;i<=tileChooser.lengthx;i++){
                for (int j=0;j<=tileChooser.lengthy;j++){
                    Map.tile[(int)(Map.mousex/16)+i][(int)(Map.mousey/16)+j].x=(tileChooser.numbx+i)*16;
                    Map.tile[(int)(Map.mousex/16)+i][(int)(Map.mousey/16)+j].y=(tileChooser.numby+j)*16;
                }
            }
        }
        else{
            if (MouseKlick.Button==3){
                for (int i=0;i<=tileChooser.lengthx;i++){
                    for (int j=0;j<=tileChooser.lengthy;j++){
                        Map.tile[(int)(Map.mousex/16)+i][(int)(Map.mousey/16)+j].x=(-16);
                        Map.tile[(int)(Map.mousex/16)+i][(int)(Map.mousey/16)+j].y=(-16);
                    }
                }
            }
        }
    }
}

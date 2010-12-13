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
public class TileMouse extends MouseAdapter implements MouseListener{

    @Override
    public void mousePressed(MouseEvent me) {
        int mousex = me.getPoint().x;
        int mousey = me.getPoint().y;
        tileChooser.numbx = (int)(mousex/16);
        tileChooser.numby = (int)(mousey/16);
        tileChooser.lengthx = 0;
        tileChooser.lengthy = 0;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        int mousex = me.getPoint().x;
        int mousey = me.getPoint().y;
        tileChooser.lengthx = (int)(mousex/16)-tileChooser.numbx;
        tileChooser.lengthy = (int)(mousey/16)-tileChooser.numby;

        if (tileChooser.lengthx<0){
            tileChooser.numbx += tileChooser.lengthx;
            tileChooser.lengthx = (-tileChooser.lengthx);
        }
        if (tileChooser.lengthy<0){
            tileChooser.numby += tileChooser.lengthy;
            tileChooser.lengthy = (-tileChooser.lengthy);
        }
    }
}
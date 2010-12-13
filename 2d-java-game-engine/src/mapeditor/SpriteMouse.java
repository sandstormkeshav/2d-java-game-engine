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
public class SpriteMouse extends MouseAdapter implements MouseListener{

    @Override
    public void mousePressed(MouseEvent me) {
        int mousex = me.getPoint().x;
        int mousey = me.getPoint().y;
        spriteChooser.numbx = (int)(mousex/16);
        spriteChooser.numby = (int)(mousey/16);
        spriteChooser.lengthx = 0;
        spriteChooser.lengthy = 0;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        int mousex = me.getPoint().x;
        int mousey = me.getPoint().y;
        spriteChooser.lengthx = (int)(mousex/16)-spriteChooser.numbx;
        spriteChooser.lengthy = (int)(mousey/16)-spriteChooser.numby;

        if (spriteChooser.lengthx<0){
            spriteChooser.numbx += spriteChooser.lengthx;
            spriteChooser.lengthx = (-spriteChooser.lengthx);
        }
        if (spriteChooser.lengthy<0){
            spriteChooser.numby += spriteChooser.lengthy;
            spriteChooser.lengthy = (-spriteChooser.lengthy);
        }
    }
}
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
public class MouseMotion extends MouseAdapter implements MouseMotionListener{

    public int mousex,x;
    public int mousey,y;
    public boolean inside;

    @Override
    public void mouseDragged(MouseEvent e){
        x=mousex = e.getPoint().x;
        y=mousey = e.getPoint().y;
    }


    @Override
    public void mouseMoved(MouseEvent e){
        x=mousex = e.getPoint().x;
        y=mousey = e.getPoint().y;
    }

    @Override
    public void mouseEntered(MouseEvent e){
        inside = true;
    }

    @Override
    public void mouseExited(MouseEvent e){
        inside = false;
    }

    public int getX(){
        return mousex;
    }
    public int getY(){
        return mousey;
    }
}

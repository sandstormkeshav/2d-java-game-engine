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

    int mousex;
    int mousey;
    boolean inside;

    @Override
    public void mouseDragged(MouseEvent e){
        mousex = e.getPoint().x;
        mousey = e.getPoint().y;
    }


    @Override
    public void mouseMoved(MouseEvent e){
        mousex = e.getPoint().x;
        mousey = e.getPoint().y;
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

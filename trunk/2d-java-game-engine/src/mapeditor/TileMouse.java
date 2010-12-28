/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeditor;


import java.awt.Component;
import java.awt.Point;
import java.awt.event.*;
/**
 *
 * @author Basti
 */
public class TileMouse extends MouseAdapter implements MouseListener{
    
    public Point MouseLocation = new Point(0, 0);
    public static Component clickedComponent = null;

    @Override
    public  void mousePressed(MouseEvent me) {

        MouseLocation = me.getPoint();
        clickedComponent = me.getComponent();

    }

}
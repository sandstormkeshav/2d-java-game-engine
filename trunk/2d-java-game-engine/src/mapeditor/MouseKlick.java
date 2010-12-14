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
        Button = me.getButton();
    }

    @Override
    public void mouseReleased(MouseEvent me){
        Button = 0;
    }

    public int getButton(){
        return Button;
    }
}

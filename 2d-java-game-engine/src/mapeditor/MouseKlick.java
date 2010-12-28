/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeditor;


import java.awt.Point;
import java.awt.event.*;
/**
 *
 * @author Basti
 */
public class MouseKlick extends MouseAdapter implements MouseListener{

    public int Button = 0;
    
    private boolean[] buttonState = new boolean[99];
    
    public Point initialClick = null;

    @Override
    public void mousePressed(MouseEvent me){
        Button = me.getButton();
        initialClick = me.getPoint();
        buttonState[Button] = true;
    }

    @Override
    public void mouseReleased(MouseEvent me){
        
        initialClick = null;
        buttonState[Button] = false;
        Button = 0;

    }

    public int getButton(){
        return Button;
    }
    
    public boolean getButtonState(int buttonNumber){
        return buttonState[buttonNumber];
    }
}

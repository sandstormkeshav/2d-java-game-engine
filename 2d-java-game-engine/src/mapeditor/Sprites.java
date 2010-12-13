/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mapeditor;

/**
 *
 * @author Basti
 */
public class Sprites {

        int x=0;
        int y=0;

    public Sprites(int a, int b){
        x = 16*(a-1);
        y = 16*(b-1);
    }

    public void setPart(int c, int r){
        x = 16*(c-1);
        y = 16*(r-1);
    }

}

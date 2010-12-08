package engine.game;

import java.awt.Color;
import javax.swing.JFrame;


public class Main extends JFrame{
    
    public Main(){

        add(new gameMain());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(16*23, 16*19);
        gameMain.width = 16*23;
        gameMain.height = 16*19;
        setLocationRelativeTo(null);
        setTitle("2D Game Engine");
        setVisible(true);
        setResizable(false);
        this.setBackground(Color.black);

    }

    public static void main(String[] args){
        new Main();
    }

}

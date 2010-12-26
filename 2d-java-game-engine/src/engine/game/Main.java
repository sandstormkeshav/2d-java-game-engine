package engine.game;

import java.awt.Color;
import javax.swing.JFrame;


public class Main extends JFrame{

    public static gameMain GAME;

    public Main(){

        GAME = new gameMain();

        add(GAME);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(16*30, 16*28);
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

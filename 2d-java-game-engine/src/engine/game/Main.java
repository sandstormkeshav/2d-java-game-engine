package engine.game;

import java.awt.Color;
import javax.swing.JFrame;


public class Main extends JFrame{

    public static gameMain GAME;

    public Main(){

        gameMain.width = 16*30;
        gameMain.height = 16*28;
        
        GAME = new gameMain();

        add(GAME);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(16*30, 16*28);
        setLocationRelativeTo(null);
        setTitle("2D Game Engine");
        setVisible(true);
        setResizable(false);
        this.setBackground(Color.black);

    }

    public Main(String level){

        gameMain.width = 16*30;
        gameMain.height = 16*28;

        GAME = new gameMain(level);

        add(GAME);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(16*30, 16*28);
        setLocationRelativeTo(null);
        setTitle("2D Game Engine");
        setVisible(true);
        setResizable(false);
        this.setBackground(Color.black);

    }

    public Main(String level, int width, int height){

        gameMain.width = width;
        gameMain.height = height;

        GAME = new gameMain(level);

        add(GAME);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);
        setTitle("2D Game Engine");
        setVisible(true);
        setResizable(false);
        this.setBackground(Color.black);

    }

    public Main (int width, int height){

        gameMain.width = width;
        gameMain.height = height;

        GAME = new gameMain();

        add(GAME);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(width, height);
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

package game;

import java.awt.Color;
import javax.swing.JFrame;


public class Main extends JFrame{

    public static gameMain GAME;

    public Main(){

        GAME = new gameMain();

        add(GAME);
        
        gameMain.width = 640;
        gameMain.height = 480;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setSize(640, 480);

        setLocationRelativeTo(null);
        setTitle("2D Game Engine");
        setVisible(true);
        setResizable(false);
        this.setBackground(Color.black);

    }

    public Main(boolean open){

        GAME = new gameMain(open);

        add(GAME);

        gameMain.width = 400;
        gameMain.height = 300;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(400, 300);

        setLocationRelativeTo(null);
        setTitle("2D Game Engine");
        setVisible(true);
        setResizable(false);
        this.setBackground(Color.black);

    }

    public Main(String level){

        gameMain.width = 400;
        gameMain.height = 300;

        GAME = new gameMain(level);

        add(GAME);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
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

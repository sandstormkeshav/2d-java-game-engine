
package engine.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;

/**
 *
 * @author Philipp Jean-Jacques
 */

public class MainFullScreen extends Frame implements KeyListener{

    private static int counter = 0;
    private static final int MAX = 9999999;
    private static VolatileImage renderImage = null;
    private static long[] fps = new long[99999];
    private static Dimension resolution = null;
    private static Dimension draw = null;

    // FPS things
    private static int countFrames = 0;
    
    private static Frame frame;
    
    public MainFullScreen(){
        super();
        setUndecorated(true);
        setIgnoreRepaint(true);
        addKeyListener(this);
        setFrame();
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        MODES = get32BitDisplayModes(graphicsDevice.getDisplayModes());
    }
    
    
    
    public MainFullScreen(int reswidth, int resheight, int depth){
        super();
        setUndecorated(true);
        setIgnoreRepaint(true);
        addKeyListener(this);
        MODES = new DisplayMode[] { new DisplayMode(reswidth, resheight, depth, 0) };
        setFrame();
    }
    
    public static DisplayMode[] get32BitDisplayModes(DisplayMode[] inputMode){
        DisplayMode[] modes = new DisplayMode[99999];
        int n = 0;

        for(int i = 0; i < inputMode.length; i++){
            if(inputMode[i].getBitDepth() == 32){
                modes[n] = inputMode[i];
                n++;
            }
        }

        DisplayMode[] returnModes = new DisplayMode[n];

        for(int i = 0; i < n; i++){
            returnModes[i] = modes[i];
        }

        return returnModes;
    }

    public void setFrame(){
        frame = this;
    }

    //key down event:
    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        if(gameMain.keyPressed[key] == false){
            gameMain.keyPressed[key] = true;
        }

    }

    //key up event:
    @Override
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        gameMain.keyReleased[key] = true;
        gameMain.keyPressed[key] = false;
    }

    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    private static DisplayMode MODES[] = new DisplayMode[] {

        new DisplayMode(640, 480, 32, 0), new DisplayMode(640, 480, 16, 0),
        new DisplayMode(640, 480, 8, 0)

    };
    
    
    // pick the mode with the best refresh rate.
    private static DisplayMode getBestDisplayMode(GraphicsDevice device) {

        DisplayMode pick = MODES[0];

        for (int x = 0, xn = MODES.length; x < xn; x++) {
            
            DisplayMode[] modes = device.getDisplayModes();

            for (int i = 0, in = modes.length; i < in; i++) {

                //Fullscreen only supports 32 bit depth!
                if(modes[i].getBitDepth() == 32){
                    if (modes[i].getWidth() == MODES[x].getWidth() && modes[i].getHeight() == MODES[x].getHeight() && modes[i].getBitDepth() == MODES[x].getBitDepth()) {
                        pick = MODES[x];
                    }
                }
            }
            
            
        }

        return pick;
        
    }
    
    public static void printDisplayModes(){
        for(int i = 0; i < MODES.length; i ++){
            if(MODES[i].getBitDepth() == 32)System.out.println(MODES[i].getWidth() + ", " + MODES[i].getHeight() + ", " + MODES[i].getRefreshRate());
        }
    }

    public static void main(String args[]) {

        new MainFullScreen(1680, 1050, 32);
        printDisplayModes();
        GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
        DisplayMode originalDisplayMode = graphicsDevice.getDisplayMode();
        DisplayMode bestDisplayMode = getBestDisplayMode(graphicsDevice);

        System.out.println();
        
        ImageObserver io = new ImageObserver() {

            public boolean imageUpdate(Image image, int flags, int x, int y, int width, int height) {

                if ((flags & ABORT) != 0) System.out.println("Image load aborted...");
                return true;

            }
        };

        Main game = new Main("test4.level", bestDisplayMode.getWidth(), bestDisplayMode.getHeight());
        gameMain.fullscreen = true;

        try {
            
            graphicsDevice.setFullScreenWindow(frame);

            if (graphicsDevice.isDisplayChangeSupported()) {
                graphicsDevice.setDisplayMode(bestDisplayMode);
            }

            resolution = new Dimension(graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight());
            draw = new Dimension((int)((double)((double)resolution.width/(double)resolution.height) * 300), 300);

            frame.createBufferStrategy(2); // 2 buffers
            Rectangle bounds = frame.getBounds();
            BufferStrategy bufferStrategy = frame.getBufferStrategy();

            while (!done()) {

                Graphics g = null;

                try {

                    g = bufferStrategy.getDrawGraphics();
                    Graphics2D g2d = (Graphics2D)g;

                    /*
                    // Antialiasing:
                    RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                    g2d.setRenderingHints(rh);
                    */

                    if ((counter <= 2)) { // 2 buffers
                        g2d.setColor(Color.BLACK);
                        g2d.fillRect(0, 0, bounds.width, bounds.height);
                    }

                    // render
                    renderImage = game.GAME.render();

                    g2d.drawImage(gameMain.renderImage, 0, 0, resolution.width, resolution.height, 0, 0, draw.width, draw.height, null);

                    bufferStrategy.show();

                }
                finally {

                    if (g != null) {
                        g.dispose();
                    }

                }
            }
        }
        finally {
            graphicsDevice.setDisplayMode(originalDisplayMode);
            graphicsDevice.setFullScreenWindow(null);
        }
        gameMain.loadedLevel.clean();
        System.exit(0);
    }

    private static boolean done() {
        return (counter++ == MAX);
    }

    public static void getFPS(long time){


        if(System.currentTimeMillis() != time)fps[countFrames] = 1000/(System.currentTimeMillis() - time);
        countFrames++;

        if(countFrames == 64){

            double meanFPS = 0;

            // get mean score fps
            for(int i = 0; i < countFrames; i++){
                meanFPS += fps[i];
            }
            meanFPS /= (double)countFrames;

            // print out fps
            System.out.println(meanFPS);

            // reset frame counter
            countFrames = 0;

        }
    }

    public static void exit(){
        if(gameMain.keyPressed[27] == true){
            gameMain.loadedLevel.clean();
            System.exit(0);
        }
    }
    
    private class KeyPressListener extends KeyAdapter {
        private boolean[] keyPressed;
        private boolean[] keyReleased;

        

    }

}

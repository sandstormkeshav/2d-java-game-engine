/*
 * MapEditorApp.java
 */

package mapeditor;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class MapEditorApp extends SingleFrameApplication {

    public static javax.swing.JFrame toolbox;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new MapEditorView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of MapEditorApp
     */
    public static MapEditorApp getApplication() {
        return Application.getInstance(MapEditorApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               toolbox = new Toolbox();
               toolbox.setVisible(true);
            }
        });
        
        launch(MapEditorApp.class, args);
    }
}

/*
 * MainDebugMode.java
 *
 * Created on 12.12.2010, 03:45:47
 */

package engine.game;

import javax.swing.*;
import java.io.*;
import engine.game.objects.*;
import java.awt.*;

/**
 *
 * @author Philipp
 */
public class MainDebugMode extends javax.swing.JFrame {

    /** Creates new form MainDebugMode */
    public MainDebugMode() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new gameMain();
        gameMain.width = 16*23;
        gameMain.height = 16*19;
        setLocationRelativeTo(null);
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        OpenMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        resetMarioMenuItem = new javax.swing.JMenuItem();
        resetLevelMenuItem = new javax.swing.JMenuItem();
        spritePosCheckBox = new javax.swing.JCheckBoxMenuItem();
        spriteNumCheckBox = new javax.swing.JCheckBoxMenuItem();
        cameraCheckBox = new javax.swing.JCheckBoxMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("2D Game Engine (Debug Mode)");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setName("jPanel1"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 286, Short.MAX_VALUE)
        );

        jMenuBar1.setName("jMenuBar1"); // NOI18N

        jMenu1.setText("File");
        jMenu1.setName("jMenu1"); // NOI18N

        OpenMenuItem.setText("Open ...");
        OpenMenuItem.setToolTipText("Open a .level file");
        OpenMenuItem.setName("OpenMenuItem"); // NOI18N
        OpenMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OpenMenuItemMouseClicked(evt);
            }
        });
        OpenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(OpenMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Engine");
        jMenu2.setName("jMenu2"); // NOI18N

        resetMarioMenuItem.setText("Reset Mario");
        resetMarioMenuItem.setName("resetMarioMenuItem"); // NOI18N
        resetMarioMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetMarioMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(resetMarioMenuItem);

        resetLevelMenuItem.setText("Reset Level");
        resetLevelMenuItem.setName("resetLevelMenuItem"); // NOI18N
        resetLevelMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetLevelMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(resetLevelMenuItem);

        spritePosCheckBox.setText("Display sprite positions");
        spritePosCheckBox.setName("SpritePosMenuCheck"); // NOI18N
        spritePosCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spritePosCheckBoxActionPerformed(evt);
            }
        });
        spritePosCheckBox.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                spritePosCheckBoxPropertyChange(evt);
            }
        });
        jMenu2.add(spritePosCheckBox);

        spriteNumCheckBox.setText("Display sprite numbers");
        spriteNumCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                spriteNumCheckBoxActionPerformed(evt);
            }
        });
        jMenu2.add(spriteNumCheckBox);

        cameraCheckBox.setText("Display camera");
        cameraCheckBox.setName("cameraCheckBox"); // NOI18N
        cameraCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cameraCheckBoxActionPerformed(evt);
            }
        });
        jMenu2.add(cameraCheckBox);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OpenMenuItemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OpenMenuItemMouseClicked
    }//GEN-LAST:event_OpenMenuItemMouseClicked

    private void OpenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenMenuItemActionPerformed
        gameMain.loadLevel();
    }//GEN-LAST:event_OpenMenuItemActionPerformed

    private void resetMarioMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetMarioMenuItemActionPerformed
        gameMain.mario.sprite.setPosition(gameMain.mario.spawn);
        System.out.println("resetting mario to spawn point: " + gameMain.mario.spawn.x + ", " + gameMain.mario.spawn.y);
        gameMain.camera.forceSetPosition(gameMain.mario.spawn);
    }//GEN-LAST:event_resetMarioMenuItemActionPerformed

    private void spritePosCheckBoxPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_spritePosCheckBoxPropertyChange
    }//GEN-LAST:event_spritePosCheckBoxPropertyChange

    private void spritePosCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spritePosCheckBoxActionPerformed
        gameMain.showSpritePos = spritePosCheckBox.getState();
    }//GEN-LAST:event_spritePosCheckBoxActionPerformed

    private void spriteNumCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_spriteNumCheckBoxActionPerformed
        gameMain.showSpriteNum = spriteNumCheckBox.getState();
    }//GEN-LAST:event_spriteNumCheckBoxActionPerformed

    private void resetLevelMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetLevelMenuItemActionPerformed
        // -- load .level file again:
        Level level = new Level(gameMain.loadedLevel.levelArchive);
        try{
            level.load();
        }
        catch(Exception e){
        }
        gameMain.loadedLevel = level;

        gameMain.camera.forceSetPosition(gameMain.mario.spawn);
    }//GEN-LAST:event_resetLevelMenuItemActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
    }//GEN-LAST:event_formWindowClosing

    private void cameraCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cameraCheckBoxActionPerformed
        gameMain.showCamera = cameraCheckBox.getState();
    }//GEN-LAST:event_cameraCheckBoxActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainDebugMode().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem OpenMenuItem;
    public static javax.swing.JCheckBoxMenuItem cameraCheckBox;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenuItem resetLevelMenuItem;
    private javax.swing.JMenuItem resetMarioMenuItem;
    private javax.swing.JCheckBoxMenuItem spriteNumCheckBox;
    private javax.swing.JCheckBoxMenuItem spritePosCheckBox;
    // End of variables declaration//GEN-END:variables

}
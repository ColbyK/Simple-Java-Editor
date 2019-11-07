package com.javaedit;


import java.awt.Dimension;
import javax.swing.JFrame;

// Application class//
public class JavaEditor {

    public JavaEditor() {
        ExtendedJFrame frame = new ExtendedJFrame();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 400)); 
        frame.pack();
        frame.setVisible(true);
    }//

    public static void main(String[] args) {
        new JavaEditor();
    }
}
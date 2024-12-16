/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.GAMEPLAY;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionListener;
import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;

/**
 *
 * @author asus
 */
public class River extends JFrame {

    private Animator animator;

    String name1, name2;
    boolean singlePlayer;
    int difficulity;

    public River(String name1, String name2, boolean singlePlayer, int difficulity) {

        this.name1 = name1;
        this.name2 = name2;
        this.singlePlayer = singlePlayer;
        this.difficulity = difficulity;

        GLCanvas glcanvas;

        RiverListener listener = new RiverGLEventListener();
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
//        glcanvas.addMouseListener((MouseListener) listener);
//        glcanvas.addMouseMotionListener((MouseMotionListener) listener);
        getContentPane().add(glcanvas, BorderLayout.CENTER);

        animator = new FPSAnimator(glcanvas, 15);
        animator.start();

        setTitle("River Raid");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }

}

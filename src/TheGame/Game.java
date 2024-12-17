package TheGame;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import Models.GameState;

import javax.media.opengl.GLCanvas;
import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {

    private final RiverRaidGLEventListener listener;
    private final GLCanvas glcanvas;
    private Animator animator;

    public Game(String userName, double speedFactor, String secondPlayerName) {
        GameState gameState = new GameState();
        gameState.firstPlayerName = userName;
        gameState.setSpeed(speedFactor);
        if (secondPlayerName != null) {
            gameState.secondPlayerName = secondPlayerName;
            gameState.isMultipalyer = true;
        }
        listener = new RiverRaidGLEventListener(this, gameState);
        glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(listener);
        glcanvas.addKeyListener(listener);
        setupFrame();
    }

    public Game(String userName, double speedFactor) {
        this(userName, speedFactor, null);
    }

    public Game(String firstPlayerName, String secondPlayerName) {
        this(firstPlayerName, 1.0, secondPlayerName);
    }

    private void setupFrame() {
        getContentPane().add(glcanvas, BorderLayout.CENTER);
        animator = new FPSAnimator(15);
        animator.add(glcanvas);
        animator.start();
        setTitle("River Raid");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        glcanvas.requestFocus();
    }
}

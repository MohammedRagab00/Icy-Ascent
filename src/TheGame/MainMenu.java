package TheGame;

import Screens.HighScores;
import Screens.Info;
import Screens.Instructions;
import Screens.Settings;
import com.sun.opengl.util.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class MainMenu extends JFrame implements ActionListener {

    public static AudioManager audioManager = new AudioManager();

    public static void main(String[] args) {

        audioManager.playBGM();

        new MainMenu();
    }

    JButton START, CREATORS, HOW_TO_PLAY, HIGH_SCORE, EXIT, SETTINGS;

    public MainMenu() {

        Animator animator;
        animator = new FPSAnimator(25);
        animator.start();

        // The Buttons:
        START = new JButton("START");
        CREATORS = new JButton("CREATORS");
        HOW_TO_PLAY = new JButton("INSTRUCTIONS");
        HIGH_SCORE = new JButton("HIGH SCORE");
        EXIT = new JButton("EXIT");
        SETTINGS = new JButton("SETTINGS");

        Font F = new Font("Arial", Font.PLAIN, 20);
        START.setFont(F);
        CREATORS.setFont(F);
        HOW_TO_PLAY.setFont(F);
        HIGH_SCORE.setFont(F);
        EXIT.setFont(F);
        SETTINGS.setFont(F);

        // The Buttons Background:
        START.setBackground(new Color(0, 128, 255)); // Bright blue
        CREATORS.setBackground(new Color(255, 99, 71)); // Tomato red
        HOW_TO_PLAY.setBackground(new Color(50, 205, 50));
        HIGH_SCORE.setBackground(new Color(255, 165, 0));
        EXIT.setBackground(new Color(220, 20, 60));
        SETTINGS.setBackground(new Color(120, 100, 0));

        // The Buttons Text Color:
        START.setForeground(Color.WHITE);
        CREATORS.setForeground(Color.WHITE);
        HOW_TO_PLAY.setForeground(Color.WHITE);
        HIGH_SCORE.setForeground(Color.WHITE);
        EXIT.setForeground(Color.WHITE);
        SETTINGS.setForeground(Color.WHITE);

        // The Buttons Position & Size:
        START.setBounds(450, 100, 200, 85);
        CREATORS.setBounds(450, 200, 200, 85);
        HOW_TO_PLAY.setBounds(450, 300, 200, 85);
        HIGH_SCORE.setBounds(450, 400, 200, 85);
        EXIT.setBounds(450, 600, 200, 85);
        SETTINGS.setBounds(450, 500, 200, 85);

        // The Buttons Border:
        START.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
        CREATORS.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
        HOW_TO_PLAY.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
        HIGH_SCORE.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
        EXIT.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
        SETTINGS.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));

        setLayout(null);
        ImageIcon backgroundIcon = new ImageIcon("src/Assets//Bground.png");
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setBounds(0, 0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
        add(backgroundLabel);

        backgroundLabel.add(START);
        backgroundLabel.add(CREATORS);
        backgroundLabel.add(HOW_TO_PLAY);
        backgroundLabel.add(EXIT);
        backgroundLabel.add(HIGH_SCORE);
        backgroundLabel.add(SETTINGS);

        START.addActionListener(this);
        START.setFocusable(false);
        CREATORS.addActionListener(this);
        CREATORS.setFocusable(false);
        HOW_TO_PLAY.addActionListener(this);
        HOW_TO_PLAY.setFocusable(false);
        EXIT.addActionListener(this);
        EXIT.setFocusable(false);
        HIGH_SCORE.addActionListener(this);
        HIGH_SCORE.setFocusable(false);
        SETTINGS.addActionListener(this);
        SETTINGS.setFocusable(false);

        setTitle("River Raid Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        setFocusable(true);
        this.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().equals(START)) {
            new LevelsScreen();
            this.dispose();
        }
        if (e.getSource().equals(CREATORS)) {
            new Info().setVisible(true);
            this.dispose();
        }
        if (e.getSource().equals(HOW_TO_PLAY)) {
            new Instructions().setVisible(true);
            this.dispose();
        }
        if (e.getSource().equals(SETTINGS)) {
            new Settings().setVisible(true);
            this.dispose();
        }

        if (e.getSource().equals(HIGH_SCORE)) {
            // HIGH SCORE DASHBOARD:
            java.util.List<String> highScores = new ArrayList<>();

            int i = 1;
            boolean isData = false;
            try {
                File file = new File("src/Assets/high_score.txt");
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        if (line.isEmpty()) {
                            break;
                        }
                        isData = true;
                        highScores.add(line);
                        i++;
                    }
                }
                if (!isData) {
                    highScores.add("NO ONE HAS PLAYED YET!");
                }
                while (highScores.size() < 8) {
                    highScores.add("");
                }

                new HighScores(highScores).setVisible(true);
                this.dispose();

            } catch (FileNotFoundException err) {
                err.printStackTrace();
            }

        }

        if (e.getSource().equals(EXIT)) {
            System.exit(0);
        }

    }
}

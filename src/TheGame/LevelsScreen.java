package TheGame;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;

public class LevelsScreen extends JFrame implements ActionListener {

    String FirstPlayerName;
    String SecondPlayerName;
    JButton EASY, NORMAL, HARD, BACK, MULTIPLAYER;

    public LevelsScreen() {

        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 700, 800, 30, 30));

        // Custom background gradient
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 60, 114),
                        0, getHeight(), new Color(89, 133, 204));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        });

        // Create custom buttons with improved aesthetics
        createStyledButtons();

        // Layout setup
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add buttons with better spacing
        add(EASY, gbc);
        add(NORMAL, gbc);
        add(HARD, gbc);

        // Create a panel for bottom buttons
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.add(BACK);
        bottomPanel.add(MULTIPLAYER);

        gbc.anchor = GridBagConstraints.PAGE_END;
        gbc.weighty = 1.0;
        add(bottomPanel, gbc);

        // Frame configuration
        setTitle("River Raid - Levels");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createStyledButtons() {
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 18);
        Font bottomButtonFont = new Font("Segoe UI", Font.BOLD, 14);

        // Custom button creator with hover effects
        EASY = createButton("EASY", buttonFont, new Color(41, 128, 185), Color.WHITE);
        NORMAL = createButton("NORMAL", buttonFont, new Color(52, 152, 219), Color.WHITE);
        HARD = createButton("HARD", buttonFont, new Color(192, 57, 43), Color.WHITE);

        BACK = createButton("BACK", bottomButtonFont, new Color(211, 84, 0), Color.WHITE);
        MULTIPLAYER = createButton("MULTIPLAYER", bottomButtonFont, new Color(39, 174, 96), Color.WHITE);
    }

    private JButton createButton(String text, Font font, Color bgColor, Color fgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(bgColor.brighter());
                } else {
                    g2d.setColor(bgColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(fgColor);
                g2d.setFont(font);

                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }
        };

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(font);
        button.setPreferredSize(new Dimension(250, 70));
        button.addActionListener(this);

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(EASY)) {
            GetGameFromDifficulty(0.90);
        }
        if (e.getSource().equals(NORMAL)) {
            GetGameFromDifficulty(1);
        }
        if (e.getSource().equals(HARD)) {
            GetGameFromDifficulty(1.2);
        }
        if (e.getSource().equals(BACK)) {
            new MainMenu();
            setVisible(false);
        }
        if (e.getSource().equals(MULTIPLAYER)) {
            GetMultiplayerGame();
        }
    }

    public void GetGameFromDifficulty(double speedFactor) {
        // Create custom input dialog
        CustomInputDialog nameDialog = new CustomInputDialog(this, "Player Name", "Enter Your Name");
        nameDialog.setVisible(true);

        // Check if name was entered
        if (nameDialog.isConfirmed()) {
            FirstPlayerName = nameDialog.getPlayerName();

            // Show custom confirmation dialog
            boolean isReady = CustomInputDialog.showConfirmDialog(this, FirstPlayerName);

            if (isReady) {
                new Game(FirstPlayerName, speedFactor);
                setVisible(false);
            }
        }
    }

    public void GetMultiplayerGame() {
        // First player name
        CustomInputDialog firstPlayerDialog = new CustomInputDialog(this, "First Player", "Enter First Player Name");
        firstPlayerDialog.setVisible(true);

        // Check first player name
        if (firstPlayerDialog.isConfirmed()) {
            FirstPlayerName = firstPlayerDialog.getPlayerName();

            // Second player name
            CustomInputDialog secondPlayerDialog = new CustomInputDialog(this, "Second Player", "Enter Second Player Name");
            secondPlayerDialog.setVisible(true);

            // Check second player name
            if (secondPlayerDialog.isConfirmed()) {
                SecondPlayerName = secondPlayerDialog.getPlayerName();

                // Show confirmation dialog
                boolean isReady = CustomInputDialog.showConfirmDialog(this, FirstPlayerName);

                if (isReady) {
                    new Game(FirstPlayerName, SecondPlayerName);
                    setVisible(false);
                }
            }
        }
    }
}

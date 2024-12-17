package TheGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomInputDialog extends JDialog {

    private JTextField nameField;
    private String playerName = null;
    private boolean confirmed = false;

    public CustomInputDialog(JFrame parent, String title, String message) {
        super(parent, title, true);

        // Set up dialog with rounded corners
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 400, 250, 30, 30));

        // Custom background
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

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Message Label
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.weighty = 0.3;
        add(messageLabel, gbc);

        // Name Input Field
        nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nameField.setPreferredSize(new Dimension(300, 40));
        nameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.weighty = 0.3;
        add(nameField, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);

        // OK Button
        JButton okButton = createStyledButton("OK", new Color(39, 174, 96));
        okButton.addActionListener(e -> {
            if (!nameField.getText().trim().isEmpty()) {
                playerName = nameField.getText().trim();
                confirmed = true;
                dispose();
            } else {
                // Highlight empty field
                nameField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.RED, 2, true),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });

        // Cancel Button
        JButton cancelButton = createStyledButton("Cancel", new Color(192, 57, 43));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.weighty = 0.4;
        add(buttonPanel, gbc);

        // Add key listener for Enter and Escape
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    okButton.doClick();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    dispose();
                }
            }
        });

        // Final setup
        setSize(400, 250);
        setLocationRelativeTo(parent);
    }

    private JButton createStyledButton(String text, Color bgColor) {
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
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));

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
        button.setPreferredSize(new Dimension(150, 50));

        return button;
    }

    // Confirmation Dialog
    public static boolean showConfirmDialog(JFrame parent, String playerName) {
        JDialog confirmDialog = new JDialog(parent, "Confirmation", true);
        confirmDialog.setUndecorated(true);
        confirmDialog.setShape(new RoundRectangle2D.Double(0, 0, 400, 250, 30, 30));

        confirmDialog.setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 60, 114),
                        0, getHeight(), new Color(89, 133, 204));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        });

        confirmDialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Message
        JLabel messageLabel = new JLabel(playerName + ", Are You Ready?", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.weighty = 0.5;
        confirmDialog.add(messageLabel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);

        AtomicBoolean result = new AtomicBoolean(false);

        // Yes Button
        JButton yesButton = new JButton("Yes") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(39, 174, 96).darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(39, 174, 96).brighter());
                } else {
                    g2d.setColor(new Color(39, 174, 96));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));

                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }
        };
        yesButton.setContentAreaFilled(false);
        yesButton.setBorderPainted(false);
        yesButton.setFocusPainted(false);
        yesButton.setPreferredSize(new Dimension(150, 50));
        yesButton.addActionListener(e -> {
            result.set(true);
            confirmDialog.dispose();
        });

        // No Button
        JButton noButton = new JButton("No") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(new Color(192, 57, 43).darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(192, 57, 43).brighter());
                } else {
                    g2d.setColor(new Color(192, 57, 43));
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 14));

                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }
        };
        noButton.setContentAreaFilled(false);
        noButton.setBorderPainted(false);
        noButton.setFocusPainted(false);
        noButton.setPreferredSize(new Dimension(150, 50));
        noButton.addActionListener(e -> confirmDialog.dispose());

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        gbc.weighty = 0.5;
        confirmDialog.add(buttonPanel, gbc);

        confirmDialog.setSize(400, 250);
        confirmDialog.setLocationRelativeTo(parent);
        confirmDialog.setVisible(true);

        return result.get();
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

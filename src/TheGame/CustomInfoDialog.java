package TheGame;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CustomInfoDialog extends JDialog {

    public CustomInfoDialog(JFrame parent, String title, String message) {
        super(parent, title, true);

        // Set up dialog with rounded corners
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 400, 30, 30));

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

        // Title Label with black text
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        gbc.weighty = 0.2;
        add(titleLabel, gbc);

        // Message Area with black text
        JTextArea messageArea = new JTextArea(message);
        messageArea.setEditable(false);
        messageArea.setBackground(Color.WHITE);
        messageArea.setForeground(Color.BLACK);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(messageArea);
        scrollPane.setOpaque(true);
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        gbc.weighty = 0.6;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        add(scrollPane, gbc);

        // Close Button
        JButton closeButton = new JButton("Close") {
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

                // Black text with white glow for contrast
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));

                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }
        };

        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(150, 50));
        closeButton.addActionListener(e -> dispose());

        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(closeButton, gbc);
        // Final setup
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}

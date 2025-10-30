package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import controller.*;

public class HomeGUI {
    private JFrame frame;
    private JButton loginButton;
    private JButton subscribeButton;

    public HomeGUI() {
        Controller controller = new Controller();

        frame = new JFrame("Benvenut3 in HackathON!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);

        // ====== PANEL PRINCIPALE CON SFONDO GRADIENT ======
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(230, 240, 255);
                Color color2 = new Color(255, 255, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(new BorderLayout());

        // ====== TITOLO ======
        JLabel title = new JLabel("Benvenut3 in HackathON!", SwingConstants.CENTER);
        title.setFont(new Font("Poppins", Font.BOLD, 38));
        title.setForeground(new Color(45, 60, 90));
        title.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 10));
        gradientPanel.add(title, BorderLayout.NORTH);

        // ====== PANEL CENTRALE ======
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Sottotitolo
        JLabel subtitle = new JLabel("Collabora. Crea. Innova.", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.ITALIC, 22));
        subtitle.setForeground(new Color(70, 70, 70));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subtitle);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // ====== PANEL BOTTONI ======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        buttonPanel.setOpaque(false);

        // Pulsante Login
        loginButton = createStyledButton("Login", new Color(65, 105, 225), new Color(40, 75, 190));
        loginButton.addActionListener(e -> {
            frame.setVisible(false);
            new LoginGUI(frame);
        });

        // Pulsante Registrati
        subscribeButton = createStyledButton("Registrati", new Color(220, 20, 60), new Color(180, 0, 50));
        subscribeButton.addActionListener(e -> {
            frame.setVisible(false);
            new SubscribeGUI(frame);
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(subscribeButton);
        centerPanel.add(buttonPanel);

        gradientPanel.add(centerPanel, BorderLayout.CENTER);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // ====== IMMAGINE ======
//      ImageIcon imageIcon = new ImageIcon("C:\\Users\\TrialCock\\IdeaProjects\\Hackathon\\Object Orientation\\src\\main\\java\\gui\\unina-logo.jpg");
//      Image image = imageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
//      imageIcon = new ImageIcon(image);
//
//      JLabel imageLabel = new JLabel(imageIcon);
//      imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//      centerPanel.add(imageLabel);
//      TODO choosing image

        // ====== PANEL INFERIORE ======
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JButton closeButton = new JButton("Chiudi");
        closeButton.setPreferredSize(new Dimension(120, 35));
        closeButton.setBackground(new Color(180, 0, 50));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                    frame,
                    "Sei sicur3 di voler chiudere l'applicazione?",
                    "Conferma chiusura",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (response == JOptionPane.YES_OPTION) System.exit(0);
        });
        bottomPanel.add(closeButton);

        gradientPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(gradientPanel);
        frame.setVisible(true);
    }

    // ====== METODO PER BOTTONI CON HOVER ======
    private JButton createStyledButton(String text, Color baseColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(160, 50));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });

        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        button.setOpaque(true);
        button.setFocusPainted(false);
        return button;
    }

}

package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controller.*;

public class HomeGUI {
    private JFrame frame;
    private JPanel homePanel;
    private JButton loginButton;
    private JButton subscribeButton;
    private JLabel title;
    private JLabel image;

    public HomeGUI() {

        Controller controller = new Controller();

        frame = new JFrame("Benvenut3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);

        homePanel = new JPanel();
        homePanel.setLayout(new BorderLayout());
        homePanel.setBackground(new Color(240, 240, 245));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(240, 240, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        title = new JLabel("Benvenuto in HackathON!");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(50, 50, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(title);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel subtitle = new JLabel();
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(new Color(100, 100, 100));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(subtitle);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        image = new JLabel("", SwingConstants.CENTER);
//        Image img = new ImageIcon(System.getProperty("user.dir"+"\\src\\main\\java\\gui\\homeTick.png")).getImage();
//        image.setIcon(new ImageIcon(img));
//        image.setBounds(100, 100, 100, 100);
//        frame.add(image);TODO
        image.setFont(new Font("Arial", Font.PLAIN, 80));
        image.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(image);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(240, 240, 245));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(150, 45));
        loginButton.setFocusPainted(false);
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new LoginGUI(controller, frame);
            }
        });
        buttonPanel.add(loginButton);

        subscribeButton = new JButton("Registrati");
        subscribeButton.setFont(new Font("Arial", Font.BOLD, 16));
        subscribeButton.setPreferredSize(new Dimension(150, 45));
        subscribeButton.setFocusPainted(false);
        subscribeButton.setBackground(new Color(220, 20, 60)); // crimson
        subscribeButton.setForeground(Color.WHITE);
        subscribeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new SubscribeGUI(controller, frame);
            }
        });
        buttonPanel.add(subscribeButton);

        centerPanel.add(buttonPanel);
        homePanel.add(centerPanel, BorderLayout.CENTER);

        // Panel per il bottone Indietro in basso a sinistra
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JButton backBtn = new JButton("Chiudi");
        backBtn.setPreferredSize(new Dimension(120, 35));
        backBtn.setBackground(new Color(220, 20, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(
                        frame,
                        "Sei sicuro di voler chiudere l'applicazione?",
                        "Conferma chiusura",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        bottomPanel.add(backBtn, BorderLayout.WEST);

        homePanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(homePanel);
        frame.setVisible(true);
    }
}
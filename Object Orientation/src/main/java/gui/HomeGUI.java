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

        frame = new JFrame("Benvenuto");
        frame.setContentPane(homePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        loginButton.setFocusPainted(false);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                new LoginGUI(controller, frame);
            }
        });

        subscribeButton.setFocusPainted(false);
        subscribeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
    }

}

package gui;

import javax.swing.*;
import controller.*;

public class LoginGUI {
    private JFrame frame;
    private JPanel loginPanel;

    public LoginGUI(Controller controller, JFrame callerFrame) {
        frame = new JFrame("Login");
        frame.setContentPane(loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

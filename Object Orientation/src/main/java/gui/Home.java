package gui;

import javax.swing.*;

public class Home {
    private JPanel homePanel;
    private JTable hackathonsTable;
    private JPanel hackathonsPanel;
    private JScrollPane hackathonsTablePanel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Registrazione");
        frame.setContentPane(new Home().homePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

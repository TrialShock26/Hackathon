package gui;

import javax.swing.*;
import java.awt.*;
import controller.Controller;

public class SubscribeGUI {
    private JFrame homeFrame;
    private JFrame frame;
    private Controller controller;

    public SubscribeGUI(Controller controller, JFrame homeFrame) {
        this.controller = controller;
        this.homeFrame = homeFrame;

        frame = new JFrame("Registrazione Utente - HackathON");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 250));

        // ===== TITOLO =====
        JLabel titleLabel = new JLabel("Iscriviti alla piattaforma", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 250));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 60, 40, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JTextField nomeField = addLabeledField(formPanel, gbc, 0, "Nome:");
        JTextField cognomeField = addLabeledField(formPanel, gbc, 1, "Cognome:");
        JTextField usernameField = addLabeledField(formPanel, gbc, 2, "Username:");
        JPasswordField passField = (JPasswordField) addLabeledField(formPanel, gbc, 3, "Password:", true);

        // ===== BOTTONE REGISTRA =====
        JButton registerBtn = new JButton("Conferma");
        styleButton(registerBtn, new Color(220, 20, 60), 150, 40);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(registerBtn, gbc);

        registerBtn.addActionListener(e -> {
            String nome = nomeField.getText();
            String cognome = cognomeField.getText();
            String username = usernameField.getText();
            String password = new String(passField.getPassword());

            if (nome.isEmpty() || cognome.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Compila tutti i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = controller.newUser(username, nome, cognome, password);
            if (success) {
                JOptionPane.showMessageDialog(frame, "Registrazione completata!");
                frame.dispose();
                new HubGUI(controller, frame);
            } else {
                JOptionPane.showMessageDialog(frame, "Errore!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // ===== BOTTONE INDIETRO =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(245, 245, 250));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JButton backBtn = new JButton("Indietro");
        styleButton(backBtn, new Color(150, 150, 150), 120, 35);
        backBtn.addActionListener(e -> {
            frame.dispose();
            homeFrame.setVisible(true);
        });
        bottomPanel.add(backBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    // ===== METODI DI SUPPORTO =====
    private JTextField addLabeledField(JPanel panel, GridBagConstraints gbc, int y, String label) {
        return (JTextField) addLabeledField(panel, gbc, y, label, false);
    }

    private JComponent addLabeledField(JPanel panel, GridBagConstraints gbc, int y, String label, boolean isPassword) {
        gbc.gridx = 0;
        gbc.gridy = y;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Poppins", Font.PLAIN, 16));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        JComponent field = isPassword ? new JPasswordField(20) : new JTextField(20);
        field.setPreferredSize(new Dimension(250, 28));
        panel.add(field, gbc);
        return field;
    }

    private void styleButton(JButton button, Color bgColor, int width, int height) {
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Poppins", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}

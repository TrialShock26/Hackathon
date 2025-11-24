package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import controller.*;

/**
 * Interfaccia grafica per il login degli utenti.
 * Permette agli utenti di inserire le proprie credenziali
 * per accedere all'applicazione. In caso di autenticazione riuscita,
 * l'utente viene reindirizzato alla schermata principale.
 */
public class LoginGUI {
    private JPanel mainPanel;
    private JFrame frame;

    /**
     * Crea e inizializza l'interfaccia grafica per il login.
     * Configura tutti i componenti grafici necessari per l'inserimento
     * delle credenziali, inclusi i campi per username e password,
     * il pulsante di conferma per l'autenticazione e il pulsante
     * per tornare alla schermata home. Gestisce anche la validazione
     * dei campi e l'interazione con il controller per verificare le credenziali.
     */
    public LoginGUI() {
        Controller controller = new Controller();

        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 240, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Accedi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(titleLabel, gbc);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(userLabel, gbc);

        JTextField userField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        centerPanel.add(passLabel, gbc);

        JPasswordField passField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(passField, gbc);

        JButton loginBtn = new JButton("Conferma");
        loginBtn.setPreferredSize(new Dimension(120, 35));
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(new ActionListener() {
            /**
             * Gestisce l'azione del pulsante "Conferma" per il login.
             * Verifica che i campi username e password siano compilati,
             * effettua l'autenticazione tramite il controller e, in caso
             * di successo, mostra un messaggio di benvenuto e apre la schermata
             * di hub. In caso di credenziali errate o campi vuoti,
             * mostra un messaggio di errore appropriato.
             *
             * @param e l'evento di azione generato dal click sul pulsante
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Compila tutti i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean success = controller.login(username, password);
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Benvenut3 " +
                                    controller.getUser().getName() + " " + controller.getUser().getSurname(),
                                    "Ciao!", JOptionPane.INFORMATION_MESSAGE);
                    frame.dispose();
                    new HubGUI(controller);
                } else {
                    JOptionPane.showMessageDialog(frame, "Credenziali errate!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(loginBtn, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        bottomPanel.setBackground(new Color(240, 240, 245));

        JButton backBtn = new JButton("Indietro");
        backBtn.setPreferredSize(new Dimension(120, 35));
        backBtn.setBackground(new Color(150, 150, 150));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(new ActionListener() {
            /**
             * Gestisce l'azione del pulsante "Indietro".
             * Chiude la finestra di login corrente e torna alla schermata
             * iniziale dell'applicazione.
             *
             * @param e l'evento di azione generato dal click sul pulsante
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new HomeGUI();
            }
        });
        bottomPanel.add(backBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
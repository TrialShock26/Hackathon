package gui;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import controller.Controller;

/**
 * Interfaccia grafica per la creazione di un nuovo hackathon.
 * Questa classe permette agli utenti di inserire tutti i dati necessari per creare
 * un hackathon, inclusi titolo, sede, date, limiti di partecipazione e selezione dei giudici.
 * Fornisce validazione dei campi e gestione degli errori tramite dialog modali.
 */
public class CreateGUI {
    private JFrame frame;
    private Controller controller;
    private JTextField titoloField;
    private JTextField sedeField;
    private JTextField dataInizioField;
    private JTextField dataFineField;
    private JTextField dataAperturaIscrizioniField;
    private JTextField dataChiusuraIscrizioniField;
    private JTextField maxIscrittiField;
    private JTextField maxDimTeamField;

    private ArrayList<String> utenti = new ArrayList<>();
    private ArrayList<String> giudiciSelezionati = new ArrayList<>();

    /**
     * Inizializza l'interfaccia grafica per la creazione di un hackathon,
     * configurando tutti i componenti visivi, i campi per i dati e i pulsanti di controllo.
     *
     * @param controller  il controller principale dell'applicazione
     * @param callerFrame il frame chiamante a cui tornare dopo la creazione o l'annullamento
     */
    public CreateGUI(Controller controller, JFrame callerFrame) {
        this.controller = controller;
        frame = new JFrame("Crea Hackathon");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ===== HEADER =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("Crea un Hackathon", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ===== FORM =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(240, 240, 245));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        int y = 0;

        titoloField = creaCampo(formPanel, gbc, y++, "Titolo:");
        sedeField = creaCampo(formPanel, gbc, y++, "Sede:");
        dataInizioField = creaCampo(formPanel, gbc, y++, "Data Inizio (YYYY-MM-DD):");
        dataFineField = creaCampo(formPanel, gbc, y++, "Data Fine (YYYY-MM-DD):");
        dataAperturaIscrizioniField = creaCampo(formPanel, gbc, y++, "Apertura Iscrizioni (YYYY-MM-DD):");
        dataChiusuraIscrizioniField = creaCampo(formPanel, gbc, y++, "Chiusura Iscrizioni (YYYY-MM-DD):");
        maxIscrittiField = creaCampo(formPanel, gbc, y++, "Max Iscritti:");
        maxDimTeamField = creaCampo(formPanel, gbc, y++, "Max Dimensione Team:");

        gbc.gridx = 0; gbc.gridy = y;
        JLabel giudiciLabel = new JLabel("Giudici:");
        giudiciLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(giudiciLabel, gbc);

        JButton selezionaButton = creaPulsante("Seleziona", new Color(70, 130, 180));
        gbc.gridx = 1;
        formPanel.add(selezionaButton, gbc);
        y++;

        selezionaButton.addActionListener(e -> mostraDialogSelezioneGiudici());

        // ===== SCROLL =====
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== BOTTOM =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(240, 240, 245));

        JButton indietroButton = creaPulsante("Indietro", new Color(150, 150, 150));
        indietroButton.addActionListener(e -> {
            frame.dispose();
            callerFrame.setVisible(true);
        });
        leftPanel.add(indietroButton);
        bottomPanel.add(leftPanel, BorderLayout.WEST);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(240, 240, 245));

        JButton salvaButton = creaPulsante("Salva", new Color(34, 139, 34));
        salvaButton.addActionListener(e -> salvaHackathon(callerFrame));
        centerPanel.add(salvaButton);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Crea un campo di input con etichetta e lo aggiunge al pannello specificato.
     * Metodo di supporto per la costruzione dell'interfaccia grafica.
     *
     * @param panel il pannello a cui aggiungere il campo
     * @param gbc   le impostazioni per il layout {@link GridBagConstraints}
     * @param y     la posizione verticale nel layout
     * @param label il testo dell'etichetta del campo
     * @return il {@link JTextField} di testo creato
     */
    private JTextField creaCampo(JPanel panel, GridBagConstraints gbc, int y, String label) {
        gbc.gridx = 0; gbc.gridy = y;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        JTextField field = new JTextField(20);
        field.setPreferredSize(new Dimension(220, 28));
        panel.add(field, gbc);
        return field;
    }

    /**
     * Metodo di suppoto che crea un pulsante stilizzato con il testo e il colore specificati.
     * Applica uno stile uniforme a tutti i pulsanti dell'interfaccia.
     *
     * @param text      il testo da visualizzare sul pulsante
     * @param baseColor il colore di sfondo del pulsante
     * @return il {@link JButton} creato e stilizzato
     */
    private JButton creaPulsante(String text, Color baseColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(120, 35));
        btn.setBackground(baseColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Metodo di supporto che valida i dati inseriti e salva l'hackathon.
     * Controlla che tutti i campi siano compilati, che siano stati selezionati
     * almeno 2 giudici e che le date siano nel formato corretto.
     * In caso di successo chiude la finestra e ritorna al frame chiamante.
     *
     * @param callerFrame il frame chiamante a cui tornare dopo il salvataggio
     */
    private void salvaHackathon(JFrame callerFrame) {
        if (titoloField.getText().isEmpty() ||
                sedeField.getText().isEmpty() ||
                dataInizioField.getText().isEmpty() ||
                dataFineField.getText().isEmpty() ||
                dataAperturaIscrizioniField.getText().isEmpty() ||
                dataChiusuraIscrizioniField.getText().isEmpty() ||
                maxIscrittiField.getText().isEmpty() ||
                maxDimTeamField.getText().isEmpty()) {

            JOptionPane.showMessageDialog(frame, "Compila tutti i campi!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (giudiciSelezionati.size() < 2) {
            JOptionPane.showMessageDialog(frame, "Seleziona almeno 2 giudici!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Date startDate = Date.valueOf(dataInizioField.getText().trim());
            Date endDate = Date.valueOf(dataFineField.getText().trim());
            Date startSubDate = Date.valueOf(dataAperturaIscrizioniField.getText().trim());
            Date endSubDate = Date.valueOf(dataChiusuraIscrizioniField.getText().trim());

            controller.getControllerPlanner().controllerOpenHackathon(
                    titoloField.getText().trim(),
                    sedeField.getText().trim(),
                    startDate, endDate,
                    startSubDate, endSubDate,
                    Integer.parseInt(maxIscrittiField.getText().trim()),
                    Integer.parseInt(maxDimTeamField.getText().trim()),
                    controller.getUser().getUsername(),
                    // Formattazione coerente con la procedure add_hackathon del db
                    String.join(",", giudiciSelezionati) + ","
            );

            JOptionPane.showMessageDialog(frame, "Hackathon creato con successo!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
            callerFrame.setVisible(true);

        } catch (SQLException ex) {
            String error = ex.getMessage();
            int idx = error.indexOf("\n");
            if (idx > 0) {
                error = error.substring(0, idx);
            }
            JOptionPane.showMessageDialog(frame,
                    "C'è stato un errore!\n" + error,
                    "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Inserisci un numero valido.", "Errore", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, "Formato data non valido. Usa YYYY-MM-DD.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo di supporto che mostra un dialog modale per la selezione dei giudici dell'hackathon.
     * Recupera la lista degli utenti disponibili e permette la selezione
     * tramite {@link JCheckBox}. Richiede la selezione di almeno 2 giudici.
     * Alla conferma, aggiorna la lista dei giudici selezionati e mostra un riepilogo.
     */
    private void mostraDialogSelezioneGiudici() {

        try{
            controller.getControllerPlanner().controllerGetUsers(controller.getUser().getUsername(), utenti);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }

        JDialog dialog = new JDialog(frame, "Seleziona Giudici", true);
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout(10, 10));

        JLabel infoLabel = new JLabel("Seleziona almeno 2 giudici:");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        dialog.add(infoLabel, BorderLayout.NORTH);

        JPanel checkPanel = new JPanel();
        checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));
        checkPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JCheckBox[] checkBoxes = new JCheckBox[utenti.size()];
        for (int i = 0; i < utenti.size(); i++) {
            checkBoxes[i] = new JCheckBox(utenti.get(i));
            checkBoxes[i].setBackground(Color.WHITE);
            checkPanel.add(checkBoxes[i]);
        }

        JScrollPane scrollPane = new JScrollPane(checkPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(280, 150));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        JButton confermaBtn = creaPulsante("Conferma", new Color(70, 130, 180));
        JButton annullaBtn = creaPulsante("Annulla", new Color(150, 150, 150));
        buttonsPanel.add(confermaBtn);
        buttonsPanel.add(annullaBtn);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        confermaBtn.addActionListener(e -> {
            giudiciSelezionati.clear();
            for (JCheckBox cb : checkBoxes)
                if (cb.isSelected()) giudiciSelezionati.add(cb.getText());

            if (giudiciSelezionati.size() < 2) {
                JOptionPane.showMessageDialog(dialog, "Devi selezionare almeno 2 giudici!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                dialog.dispose();
                JOptionPane.showMessageDialog(frame,
                        "Giudici selezionati:\n" + String.join("\n", giudiciSelezionati),
                        "Riepilogo Giudici",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        annullaBtn.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }
}
package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import javax.swing.*;
import controller.*;

/**
 * Interfaccia grafica per i giudici degli hackathon.
 * Permette di visualizzare gli hackathon in cui sono coinvolti,
 * consultare i problemi assegnati e accedere alla valutazione dei team partecipanti.
 * L'interfaccia presenta una lista di hackathon selezionabili tramite {@link JRadioButton}
 * e un pulsante per accedere al problema posto nell'hackathon.
 */
public class JudgeGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private ButtonGroup hackathonGroup;
    private JPanel listPanel;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> locations = new ArrayList<>();
    private ArrayList<String> problemDescriptions = new ArrayList<>();

    /**
     * Crea e inizializza l'interfaccia grafica.
     * Recupera la lista degli hackathon in cui l'utente è giudice
     * e configura tutti i componenti grafici per la selezione e gestione.
     * Se il giudice non è assegnato ad alcun hackathon, mostra un messaggio di errore e
     * chiude la finestra, tornando alla schermata chiamante.
     *
     * @param controller  il controller principale dell'applicazione
     * @param callerFrame la finestra chiamante a cui tornare dopo l'operazione
     */
    public JudgeGUI(Controller controller, JFrame callerFrame) {
        frame = new JFrame("Valuta");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 245));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("Valuta Hackathon", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // ====== DATI HACKATHON ======
        try {
            controller.getControllerJudge().controllerGetHackathons(controller.getUser().getUsername(), titles,
                    locations, problemDescriptions, false);
        } catch (SQLException | IllegalAccessException e) {
            String error = e.getMessage();
            int idx = error.indexOf("\n");
            error = error.substring(0, idx);
            JOptionPane.showMessageDialog(frame,
                    "C'è stato un errore!\n" + error,
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }

        // ====== LISTA HACKATHON CON RADIOBUTTON ======
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        listPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        listPanel.setMaximumSize(new Dimension(700, listPanel.getPreferredSize().height));

        hackathonGroup = new ButtonGroup();
        createPanel();

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ====== PANEL INFERIORE ======
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        // Bottone "Indietro" a sinistra
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(240, 240, 245));

        JButton backBtn = new JButton("Indietro");
        backBtn.setPreferredSize(new Dimension(120, 35));
        backBtn.setBackground(new Color(150, 150, 150));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> {
            frame.dispose();
            callerFrame.setVisible(true);
        });
        leftPanel.add(backBtn);
        bottomPanel.add(leftPanel, BorderLayout.WEST);

        // Bottone "Problema" al centro sinistra
        JPanel centerLeftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerLeftPanel.setBackground(new Color(240, 240, 245));

        JButton problemBtn = new JButton("Problema");
        problemBtn.setPreferredSize(new Dimension(120, 35));
        problemBtn.setBackground(new Color(220, 120, 60)); // Colore arancione per distinguerlo
        problemBtn.setForeground(Color.WHITE);
        problemBtn.setFocusPainted(false);
        problemBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        problemBtn.addActionListener(e -> {
            String selectedHackathon = getSelectedHackathon();
            if (selectedHackathon == null) {
                JOptionPane.showMessageDialog(frame, "Seleziona un hackathon!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                frame.setVisible(false); // nascondi JudgeGUI
                new ProblemGUI(controller, frame, selectedHackathon, locations.get(titles.indexOf(selectedHackathon)),
                        problemDescriptions.get(titles.indexOf(selectedHackathon)));
            }
        });
        centerLeftPanel.add(problemBtn);
        bottomPanel.add(centerLeftPanel, BorderLayout.CENTER);

        // Bottone "Apri" a destra
        JPanel centerRightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerRightPanel.setBackground(new Color(240, 240, 245));

        JButton openBtn = new JButton("Apri");
        openBtn.setPreferredSize(new Dimension(120, 35));
        openBtn.setBackground(new Color(70, 130, 180));
        openBtn.setForeground(Color.WHITE);
        openBtn.setFocusPainted(false);
        openBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        openBtn.addActionListener(e -> {
            String selectedHackathon = getSelectedHackathon();
            if (selectedHackathon == null) {
                JOptionPane.showMessageDialog(frame, "Seleziona un hackathon!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                frame.setVisible(false);
                new TeamsGUI(controller, frame, selectedHackathon, locations.get(titles.indexOf(selectedHackathon)));
            }
        });

        centerRightPanel.add(openBtn);
        bottomPanel.add(centerRightPanel, BorderLayout.EAST);

        // ====== BOTTONE AGGIORNA ======
        JButton refreshBtn = new JButton("Aggiorna");
        refreshBtn.setPreferredSize(new Dimension(120, 35));
        refreshBtn.setBackground(new Color(70, 130, 180));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> {
            preparePanel();
            try {
                controller.getControllerJudge().controllerGetHackathons(controller.getUser().getUsername(), titles,
                        locations, problemDescriptions, true);
            } catch (SQLException | IllegalAccessException ex) {
                String error = ex.getMessage();
                int idx = error.indexOf("\n");
                error = error.substring(0, idx);
                JOptionPane.showMessageDialog(frame,
                        "C'è stato un errore!\n" + error,
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
            createPanel();
            listPanel.revalidate();
            listPanel.repaint();
        });
        headerPanel.add(refreshBtn, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        try {
            frame.setContentPane(mainPanel);
            frame.setVisible(true);
            titles.getFirst();
        } catch (NoSuchElementException e) {
            JOptionPane.showMessageDialog(frame,
                    "Non sei Giudice in nessuna competizione!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            frame.dispose();
            callerFrame.setVisible(true);
        }
    }

    /**
     * Crea una card per rappresentare graficamente un hackathon.
     * Il pannello contiene un {@link JRadioButton} con il nome dell'hackathon.
     *
     * @param hackathonName il nome dell'hackathon da visualizzare
     * @return un JPanel contenente il {@link JRadioButton} dell'hackathon
     */
    private JPanel createHackathonCard(String hackathonName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(650, 60));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JRadioButton radio = new JRadioButton(hackathonName);
        radio.setBackground(Color.WHITE);
        radio.setFont(new Font("Arial", Font.PLAIN, 16));
        hackathonGroup.add(radio);

        card.add(radio, BorderLayout.CENTER);
        card.setMaximumSize(new Dimension(700, 60));
        card.setAlignmentX(Component.CENTER_ALIGNMENT);

        return card;
    }

    /**
     * Determina quale hackathon è stato selezionato dall'utente.
     * Scorre tutti i {@link JRadioButton} del gruppo e restituisce il testo
     * del bottone selezionato.
     *
     * @return il nome dell'hackathon selezionato, oppure null se nessun hackathon è selezionato
     */
    private String getSelectedHackathon() {
        for (Enumeration<AbstractButton> buttons = hackathonGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) return button.getText();
        }
        return null;
    }

    /**
     * Prepara il pannello per un aggiornamento rimuovendo tutti i componenti
     * e svuotando le liste dei dati degli hackathon.
     * Questo metodo viene chiamato prima di ricaricare i dati aggiornati dal database.
     */
    private void preparePanel() {
        listPanel.removeAll();
        titles.clear();
        locations.clear();
        problemDescriptions.clear();
    }

    /**
     * Popola il pannello con le card degli hackathon disponibili.
     * Per ogni hackathon presente nella lista dei titoli, crea una card
     * e la aggiunge al pannello con la relativa spaziatura.
     */
    private void createPanel() {
        for (String hackathon : titles) {
            JPanel card = createHackathonCard(hackathon);
            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }
}
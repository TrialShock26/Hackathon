package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import javax.swing.*;
import controller.*;

/**
 * Interfaccia grafica per la valutazione dei documenti di un team da parte di un giudice.
 * Questa classe permette ai giudici di visualizzare i documenti di progresso pubblicati
 * da un team specifico, commentarli e assegnare una valutazione complessiva al team.
 * L'interfaccia mostra una lista di documenti con anteprime, consente di aprirli
 * per la lettura completa e l'inserimento di commenti, e fornisce un sistema
 * di valutazione tramite voti da 0 a 10.
 */
public class ExAndVoteGUI {
    private JFrame frame;
    private Controller controller;
    private String teamName;
    private String title;
    private String location;
    private ButtonGroup documentGroup;
    private JRadioButton[] documentButtons;
    private ArrayList<String> documentTitles;
    private ArrayList<String> documentContents;
    private ArrayList<String> documentComments;
    private ArrayList<String> documentPreviews;

    /**
     * Costruisce l'interfaccia grafica per esaminare e valutare un team.
     * Inizializza la finestra principale, carica i documenti del team dal database o dalla memoria
     * e configura tutti i componenti grafici necessari per visualizzare,
     * commentare e valutare i documenti. Se non ci sono documenti disponibili,
     * mostra un messaggio di errore e ritorna alla schermata precedente.
     *
     * @param controller  il controller principale
     * @param callerFrame il frame chiamante a cui ritornare quando si preme "Indietro"
     * @param teamName    il nome del team da esaminare
     * @param title       il titolo dell'hackathon
     * @param location    la sede dell'hackathon
     */
    public ExAndVoteGUI(Controller controller, JFrame callerFrame, String teamName, String title, String location) {
        this.teamName = teamName;
        this.title = title;
        this.location = location;
        this.controller = controller;

        frame = new JFrame("Esamina Team - " + teamName);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ===== HEADER =====
        JLabel titleLabel = new JLabel("Team: " + teamName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ===== DATI =====
        documentTitles = new ArrayList<>();
        documentContents = new ArrayList<>();
        documentComments = new ArrayList<>();
        try {
            controller.getControllerTeam().controllerGetDocuments(teamName, title, location, documentTitles, documentContents, documentComments, false);
        } catch (SQLException e) {
            String error = e.getMessage();
            int idx = error.indexOf("\n");
            error = error.substring(0, idx);
            JOptionPane.showMessageDialog(frame,
                    "C'è stato un errore!\n" + error,
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }

        // Crea anteprime documento
        documentPreviews = new ArrayList<>();
        for (String documentContent : documentContents) {
            if (documentContent.length() >= 50) {
                documentPreviews.add(documentContent.substring(0, 50) + "...");
            } else {
                documentPreviews.add(documentContent);
            }
        }

        // ===== LISTA DOCUMENTI =====
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        documentGroup = new ButtonGroup();
        documentButtons = new JRadioButton[documentTitles.size()];

        for (int i = 0; i < documentTitles.size(); i++) {
            JPanel card = createDocumentCard(documentTitles.get(i), documentPreviews.get(i), i);

            // Limita altezza della card
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
            card.setAlignmentX(Component.LEFT_ALIGNMENT);

            listPanel.add(card);
            listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== PANEL INFERIORE =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        // --- Bottone "Indietro" ---
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(240, 240, 245));

        JButton backBtn = new JButton("Indietro");
        backBtn.setPreferredSize(new Dimension(120, 35));
        backBtn.setBackground(new Color(150, 150, 150));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            frame.dispose();
            callerFrame.setVisible(true);
        });
        leftPanel.add(backBtn);
        bottomPanel.add(leftPanel, BorderLayout.WEST);

        // --- Pulsanti centrali ---
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        centerPanel.setBackground(new Color(240, 240, 245));

        JButton examineBtn = new JButton("Esamina");
        examineBtn.setPreferredSize(new Dimension(120, 35));
        examineBtn.setBackground(new Color(70, 130, 180));
        examineBtn.setForeground(Color.WHITE);
        examineBtn.setFocusPainted(false);
        examineBtn.addActionListener(e -> openSelectedDocument());

        JButton rateBtn = new JButton("Valuta");
        rateBtn.setPreferredSize(new Dimension(120, 35));
        rateBtn.setBackground(new Color(34, 139, 34));
        rateBtn.setForeground(Color.WHITE);
        rateBtn.setFocusPainted(false);
        rateBtn.addActionListener(e -> openVoteDialog());

        centerPanel.add(examineBtn);
        centerPanel.add(rateBtn);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        try {
            frame.setContentPane(mainPanel);
            frame.setVisible(true);
            documentTitles.get(0); // controlla se ci sono documenti
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(frame,
                    "Non ci sono documenti da esaminare!",
                    "Errore", JOptionPane.ERROR_MESSAGE);
            frame.dispose();
            callerFrame.setVisible(true);
        }
    }

    /**
     * Crea una card grafica per rappresentare un documento nella lista.
     * La card mostra il titolo del documento, un'anteprima del contenuto
     * e include un {@link JRadioButton} per la selezione. L'intera card è cliccabile
     * per selezionare il documento corrispondente.
     *
     * @param docName il titolo del documento
     * @param preview l'anteprima del contenuto del documento (primi 50 caratteri)
     * @param index   l'indice del documento nell'array dei documenti
     * @return il pannello JPanel configurato come card del documento
     */
    private JPanel createDocumentCard(String docName, String preview, int index) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        // --- RadioButton a sinistra ---
        JRadioButton radio = new JRadioButton();
        radio.setBackground(Color.WHITE);
        radio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        documentButtons[index] = radio;
        documentGroup.add(radio);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(radio);
        card.add(leftPanel, BorderLayout.WEST);

        // --- Pannello centrale con nome e anteprima ---
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);

        JLabel nameLabel = new JLabel(docName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(nameLabel);

        JLabel previewLabel = new JLabel("<html><i>" + preview + "</i></html>");
        previewLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoPanel.add(previewLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        // --- Rendi cliccabile tutta la card ---
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                radio.setSelected(true);
            }
        });

        return card;
    }

    /**
     * Apre il popup di dettaglio per il documento selezionato.
     * Verifica quale {@link JRadioButton} è selezionato e apre il corrispondente
     * documento in un popup modale. Se nessun documento è selezionato,
     * mostra un messaggio di avviso.
     */
    private void openSelectedDocument() {
        for (int i = 0; i < documentButtons.length; i++) {
            if (documentButtons[i].isSelected()) {
                openDocumentPopup(documentTitles.get(i), documentContents.get(i), documentComments.get(i));
                return;
            }
        }
        JOptionPane.showMessageDialog(frame,
                "Seleziona un documento prima di procedere.",
                "Nessun documento selezionato",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Apre un popup modale per visualizzare e commentare un documento.
     * Il popup mostra il contenuto completo del documento in una sezione
     * e i commenti esistenti con la possibilità di aggiungerne di nuovi
     * in un'altra sezione. Il giudice può inserire il proprio commento
     * e salvarlo.
     *
     * @param docName il titolo del documento
     * @param content il contenuto completo del documento
     * @param comment i commenti precedenti sul documento
     */
    private void openDocumentPopup(String docName, String content, String comment) {
        JDialog dialog = new JDialog(frame, "Documento - " + docName, true);
        dialog.setSize(800, 500);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(245, 245, 250));

        JPanel mainContent = new JPanel(new GridLayout(1, 2, 10, 0));
        mainContent.setBackground(new Color(245, 245, 250));
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea contentArea = new JTextArea();
        contentArea.setText(content);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 15));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setCaretPosition(0);

        JScrollPane contentScroll = new JScrollPane(contentArea);
        contentScroll.setBorder(BorderFactory.createTitledBorder("Contenuto documento"));
        contentScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        JTextArea commentArea = new JTextArea();
        commentArea.setText(comment + "\n" + controller.getUser().getName() + " " + controller.getUser().getSurname() + ":\n");
        commentArea.setFont(new Font("Arial", Font.PLAIN, 15));
        commentArea.setLineWrap(true);
        commentArea.setWrapStyleWord(true);

        JScrollPane commentScroll = new JScrollPane(commentArea);
        commentScroll.setBorder(BorderFactory.createTitledBorder("Commento attuale"));
        commentScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainContent.add(contentScroll);
        mainContent.add(commentScroll);
        dialog.add(mainContent, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton commentBtn = new JButton("Commenta");
        commentBtn.setBackground(new Color(70, 130, 180));
        commentBtn.setForeground(Color.WHITE);
        commentBtn.setPreferredSize(new Dimension(120, 35));
        commentBtn.setFocusPainted(false);
        commentBtn.addActionListener(e -> {
            String newComment = commentArea.getText();
            try {
                controller.getControllerJudge().controllerExamineDocument(controller.getUser().getUsername(),
                        docName, content, comment, teamName, title, location, newComment);
                JOptionPane.showMessageDialog(frame,
                        "Commento inserito correttamente!\n",
                        "Successo", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (SQLException ex) {
                String error = ex.getMessage();
                int idx = error.indexOf("\n");
                error = error.substring(0, idx);
                JOptionPane.showMessageDialog(frame,
                        "C'è stato un errore!\n" + error,
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton closeBtn = new JButton("Chiudi");
        closeBtn.setBackground(new Color(150, 150, 150));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setPreferredSize(new Dimension(120, 35));
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(commentBtn);
        buttonPanel.add(closeBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    /**
     * Apre un popup modale per assegnare una valutazione al team.
     * Il popup permette di selezionare un voto da 0 a 10 tramite
     * un menù a tendina e di confermarlo. Il voto viene salvato
     * e associato al team per l'hackathon corrente.
     */
    private void openVoteDialog() {
        JDialog dialog = new JDialog(frame, "Assegna Voto", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(frame);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(245, 245, 250));

        JLabel label = new JLabel("Seleziona un voto da 0 a 10", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        dialog.add(label, BorderLayout.NORTH);

        Integer[] votes = new Integer[11];
        for (int i = 0; i <= 10; i++) votes[i] = i;
        JComboBox<Integer> voteCombo = new JComboBox<>(votes);
        voteCombo.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel comboPanel = new JPanel();
        comboPanel.setBackground(new Color(245, 245, 250));
        comboPanel.add(voteCombo);
        dialog.add(comboPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton okBtn = new JButton("Conferma");
        okBtn.setBackground(new Color(70, 130, 180));
        okBtn.setForeground(Color.WHITE);
        okBtn.setPreferredSize(new Dimension(120, 35));
        okBtn.setFocusPainted(false);
        okBtn.addActionListener(e -> {
            try {
                int selectedVote = (int) voteCombo.getSelectedItem();
                controller.getControllerJudge().controllerGradeTeam(controller.getUser().getUsername(),
                        teamName, title, location, selectedVote);
                JOptionPane.showMessageDialog(frame,
                        "Hai assegnato il voto " + selectedVote + " al team \"" + teamName + "\".",
                        "Voto assegnato",
                        JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (SQLException ex) {
                String error = ex.getMessage();
                int idx = error.indexOf("\n");
                error = error.substring(0, idx);
                JOptionPane.showMessageDialog(frame,
                        "C'è stato un errore!\n" + error,
                        "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelBtn = new JButton("Annulla");
        cancelBtn.setBackground(new Color(150, 150, 150));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setPreferredSize(new Dimension(120, 35));
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> dialog.dispose());

        buttonPanel.add(okBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
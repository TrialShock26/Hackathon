package gui;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.*;
import controller.*;

/**
 * Interfaccia grafica per il cambio di team di un giocatore.
 * Permette di visualizzare i team disponibili nell'hackathon di riferimento,
 * e consente all'utente di selezionare e confermare il passaggio a un nuovo team.
 * L'interfaccia presenta una lista di team selezionabili tramite {@link JRadioButton}
 * e pulsanti per confermare o annullare l'operazione.
 */
public class JoinGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private ButtonGroup teamGroup;

    private ArrayList<String> teamList = new ArrayList<>();

    /**
     * Crea e inizializza l'interfaccia grafica per il cambio team.
     * Recupera la lista dei team disponibili, escludendo quello corrente,
     * e configura tutti i componenti grafici necessari per la selezione.
     *
     * @param controller      il controller principale dell'applicazione
     * @param callerFrame     la finestra chiamante a cui tornare dopo l'operazione
     * @param currentTeam     il nome del team corrente del giocatore
     * @param currentTitle    il titolo dell'hackathon
     * @param currentLocation la sede dell'hackathon
     */
    public JoinGUI(Controller controller, JFrame callerFrame, String currentTeam, String currentTitle,String currentLocation) {
        frame = new JFrame("Cambia Team");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ====== HEADER ======
        JLabel titleLabel = new JLabel("Cambia Team - Attualmente in: " + currentTeam, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ====== LISTA TEAM CON RADIOBUTTON ======
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(240, 240, 245));
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));

        try{
            controller.getControllerPlayer().controllerGetOtherTeams(controller.getUser().getUsername(),currentTitle,currentLocation,teamList);
        } catch (SQLException ex) {
            String error = ex.getMessage();
            int idx = error.indexOf("\n");
            error = error.substring(0, idx);
            JOptionPane.showMessageDialog(frame,
                    "C'è stato un errore!\n" + error,
                    "Errore", JOptionPane.ERROR_MESSAGE);
        }

        teamGroup = new ButtonGroup();
        for (String team : teamList) {
            // Evita di mostrare il team corrente nella lista
            if (!team.equals(currentTeam)) {
                JPanel card = createTeamCard(team);
                listPanel.add(card);
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        // Forza il preferred size del listPanel in base al numero di elementi
        int cardHeight = 50;
        int gap = 10;
        int totalHeight = (teamList.size() - 1) * (cardHeight + gap) + 20; // -1 perché escludiamo il team corrente
        listPanel.setPreferredSize(new Dimension(600, Math.max(totalHeight, 300)));

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

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(240, 240, 245));

        JButton changeBtn = new JButton("Cambia Team");
        changeBtn.setPreferredSize(new Dimension(140, 35));
        changeBtn.setBackground(new Color(220, 120, 60));
        changeBtn.setForeground(Color.WHITE);
        changeBtn.setFocusPainted(false);
        changeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changeBtn.addActionListener(e -> {
            String selectedTeam = getSelectedTeam();
            if (selectedTeam == null) {
                JOptionPane.showMessageDialog(frame, "Seleziona un team!", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                int response = JOptionPane.showConfirmDialog(
                        frame,
                        "Sei sicuro di voler cambiare team?\nDa: " + currentTeam + "\nA: " + selectedTeam,
                        "Conferma Cambio Team",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION) {
                    try{
                        controller.getControllerPlayer().controllerJoinTeam(controller.getUser().getUsername(),selectedTeam,currentTitle,currentLocation);

                        JOptionPane.showMessageDialog(frame,
                                "Team cambiato con successo!\nNuovo team: " + selectedTeam,
                                "Successo",
                                JOptionPane.INFORMATION_MESSAGE);

                        frame.dispose();
                        callerFrame.setVisible(true);
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(frame,ex.getMessage() , "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        centerPanel.add(changeBtn);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    /**
     * Crea una card per rappresentare graficamente un team.
     * Il pannello contiene un {@link JRadioButton} con il nome del team.
     *
     * @param teamName il nome del team da visualizzare
     * @return un JPanel contenente il {@link JRadioButton} del team
     */
    private JPanel createTeamCard(String teamName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        JRadioButton radio = new JRadioButton(teamName);
        radio.setBackground(Color.WHITE);
        radio.setFont(new Font("Arial", Font.PLAIN, 16));
        teamGroup.add(radio);

        card.add(radio, BorderLayout.CENTER);

        return card;
    }

    /**
     * Determina quale team è stato selezionato dall'utente.
     * Scorre tutti i {@link JRadioButton} del gruppo e restituisce il testo
     * del bottone selezionato.
     *
     * @return il nome del team selezionato, oppure null se nessun team è selezionato
     */
    private String getSelectedTeam() {
        for (Enumeration<AbstractButton> buttons = teamGroup.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) return button.getText();
        }
        return null;
    }
}
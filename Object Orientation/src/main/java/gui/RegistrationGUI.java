package gui;

import controller.*;
import model.Hackathon;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegistrationGUI {
    private static JFrame frame;
    private JPanel registrationPanel;
    private JTable hackathonsTable;
    private JPanel hackathonsPanel;
    private JScrollPane hackathonsScrollPanel;
    private JButton updateButton;
    private JPanel topPanel;
    private JButton registrateButton;
    private JLabel topLabel;
    private int rowSelected;

    public RegistrationGUI(Controller controller, JFrame callerFrame) {
        frame = new JFrame("Registrazione");
        frame.setContentPane(registrationPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        controller.fetchData();
        rowSelected = -1;

        HackathonsTableModel tableModel = new HackathonsTableModel();
        hackathonsTable.setModel(tableModel);
        tableModel.setHackathons(controller.getHackathons());
        tableModel.fireTableDataChanged();
        hackathonsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        updateButton.setFocusPainted(false);
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setHackathons(controller.getHackathons());
                tableModel.fireTableDataChanged();
                rowSelected = -1;
            }
        });
        hackathonsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rowSelected = hackathonsTable.getSelectedRow();
                Hackathon h = controller.getHackathon(rowSelected);
                JOptionPane.showMessageDialog(frame, "Titolo: " + h.getTitle() + "\n" +
                                "Sede: " + h.getLocation() + "\n" +
                                "Data di inizio: " + h.getStartDate() + "\n" +
                                "Data di fine: " + h.getEndDate() + "\n" +
                                "Durata: " + h.getPeriodOfTime() + "\n" +
                                "Data inizio iscrizioni: " + h.getStartSubscriptionDate() + "\n" +
                                "Data fine iscrizioni: " + h.getEndSubscriptionDate() + "\n" +
                                "Data inizio iscrizioni: " + h.getStartSubscriptionDate() + "\n" +
                                "Massimo numero di giocatori: " + h.getMaxPlayers() + "\n" +
                                "Massima dimensione dei team: " + h.getMaxTeamDim() + "\n",
                        "Riepilogo", JOptionPane.PLAIN_MESSAGE);
            }
        });
        registrateButton.setFocusPainted(false);
        registrateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rowSelected != -1) {
                    int res = JOptionPane.showOptionDialog(frame, "Confermi la registrazione a '" +
                                    controller.getHackathon(rowSelected).getTitle() + "'?", "Conferma", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, null, null);
                    if (res == JOptionPane.YES_OPTION) {
                        //Will add the user to the subscription
                        JOptionPane.showMessageDialog(frame, "Registrazione effettuata!");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Registrazione annullata!");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Devi selezionare un hackathon!", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

}
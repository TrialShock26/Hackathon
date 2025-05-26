package gui;

import javax.swing.*;
import controller.*;
import model.Hackathon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Home {
    private static JFrame frame;
    private JPanel homePanel;
    private JTable hackathonsTable;
    private JPanel hackathonsPanel;
    private JScrollPane hackathonsTablePanel;
    private JButton updateButton;
    private JPanel topPanel;
    private JButton registrateButton;
    private Controller controller;
    private int rowSelected;

    public static void main(String[] args) {
        frame = new JFrame("Registrazione");
        frame.setContentPane(new Home().homePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public Home() {
        controller = new Controller();
        controller.fetchData();
        rowSelected = -1;

        HackathonsTableModel table = new HackathonsTableModel();
        hackathonsTable.setModel(table);
        table.setHackathons(controller.getHackathons());
        table.fireTableDataChanged();
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                table.setHackathons(controller.getHackathons());
                table.fireTableDataChanged();
                rowSelected = -1;
            }
        });
        hackathonsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rowSelected = hackathonsTable.getSelectedRow();
                Hackathon h = controller.getHackathon(rowSelected);
                // Cambiare "Message"
                JOptionPane.showMessageDialog(frame, "Titolo: "+h.getTitle()+"\n"+
                                                        "Sede: "+h.getLocation()+"\n"+
                                                        "Data di inizio: "+h.getStartDate()+"\n"+
                                                        "Data di fine: "+h.getEndDate()+"\n"+
                                                        "Durata: "+h.getPeriodOfTime()+"\n"+
                                                        "Data inizio iscrizioni: "+h.getStartSubscriptionDate()+"\n"+
                                                        "Data fine iscrizioni: "+h.getEndSubscriptionDate()+"\n"+
                                                        "Data inizio iscrizioni: "+h.getStartSubscriptionDate()+"\n"+
                                                        "Finestra di tempo: "+h.getRegistrationWindow()+" giorni\n"+
                                                        "Massimo numero di giocatori: "+h.getMaxPlayers()+"\n"+
                                                        "Massima dimensione dei team: "+h.getMaxTeamDim()+"\n",
                                                "Riepilogo", JOptionPane.PLAIN_MESSAGE);
            }
        });
        registrateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rowSelected != -1) {
                    int res = JOptionPane.showOptionDialog(frame, "Confermi la registrazione a '" + controller.getHackathon(rowSelected).getTitle() + "'?", "Conferma", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
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
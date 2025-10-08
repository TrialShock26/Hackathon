package gui;

import java.awt.*;
import javax.swing.*;
import controller.*;

public class ExnVoteGUI {
    private JFrame frame;
    private String teamName;

    public ExnVoteGUI(Controller controller, JFrame callerFrame, String teamName) {
        this.teamName = teamName;

        frame = new JFrame("Esamina Team - " + teamName);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 245));

        // ===== HEADER =====
        JLabel titleLabel = new JLabel("Team: " + teamName, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ===== AREA DOCUMENTO CON SCROLLBAR =====
        JTextArea documentArea = new JTextArea();
        documentArea.setText("""
                Descrizione del progetto del team...
                Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                Nulla nec commodo nisi. Suspendisse potenti.
                In hac habitasse platea dictumst. 
                Curabitur tincidunt purus ac diam euismod, in placerat velit mattis.
                """);
        documentArea.setFont(new Font("Arial", Font.PLAIN, 16));
        documentArea.setLineWrap(true);
        documentArea.setWrapStyleWord(true);
        documentArea.setEditable(false);
        documentArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(documentArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== PANEL INFERIORE =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(240, 240, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));

        // --- Bottone "Indietro" a sinistra ---
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

        // --- Pannello centrale con Esamina e Valuta ---
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        centerPanel.setBackground(new Color(240, 240, 245));

        JButton examineBtn = new JButton("Esamina");
        examineBtn.setPreferredSize(new Dimension(120, 35));
        examineBtn.setBackground(new Color(70, 130, 180));
        examineBtn.setForeground(Color.WHITE);
        examineBtn.setFocusPainted(false);
        examineBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        examineBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame,
                    "Qui potresti aprire il documento completo del team \"" + teamName + "\".",
                    "Esamina Team",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        JButton rateBtn = new JButton("Valuta");
        rateBtn.setPreferredSize(new Dimension(120, 35));
        rateBtn.setBackground(new Color(34, 139, 34));
        rateBtn.setForeground(Color.WHITE);
        rateBtn.setFocusPainted(false);
        rateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        rateBtn.addActionListener(e -> openVoteDialog());

        centerPanel.add(examineBtn);
        centerPanel.add(rateBtn);
        bottomPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    // ===== POPUP DI VALUTAZIONE =====
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
            int selectedVote = (int) voteCombo.getSelectedItem();
            dialog.dispose();

            JOptionPane.showMessageDialog(frame,
                    "Hai assegnato il voto " + selectedVote + " al team \"" + teamName + "\".",
                    "Voto assegnato",
                    JOptionPane.INFORMATION_MESSAGE);

            // Esempio: controller.saveVote(teamName, selectedVote);
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

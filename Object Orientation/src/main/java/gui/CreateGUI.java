    package gui;

    import javax.swing.*;
    import java.awt.*;
    import java.util.ArrayList;
    import java.util.List;
    import controller.Controller;
    import controller.ControllerPlanner;

    public class CreateGUI {
        private JFrame frame;
        private JTextField titoloField, sedeField, durataField, dataInizioField, dataFineField,
                periodoIscrizioniField, dataAperturaIscrizioniField, dataChiusuraIscrizioniField,
                maxIscrittiField, maxDimTeamField;

        private ArrayList<String> utenti = new ArrayList<>();
        private ArrayList<String> giudiciSelezionati = new ArrayList<>();

        private ArrayList<String> names = new ArrayList<>();
        private ArrayList<String> surnames = new ArrayList<>();
        private ArrayList<String> passwords = new ArrayList<>();

        public CreateGUI(Controller controller, JFrame callerFrame) {
            frame = new JFrame("Crea un Hackathon");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(750, 700);
            frame.setLocationRelativeTo(null);

            // ===== PANEL PRINCIPALE GRADIENT =====
            JPanel gradientPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    Color color1 = new Color(245, 245, 250);
                    Color color2 = new Color(230, 240, 255);
                    GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            gradientPanel.setLayout(new BorderLayout(20, 20));

            // ===== TITOLO =====
            JLabel titleLabel = new JLabel("Crea un Hackathon", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Poppins", Font.BOLD, 34));
            titleLabel.setForeground(new Color(45, 60, 90));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
            gradientPanel.add(titleLabel, BorderLayout.NORTH);

            // ===== PANEL CENTRALE =====
            JPanel panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(12, 12, 12, 12);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;
            int y = 0;

            // Creazione campi con label ingrandita
            titoloField = creaCampo(panel, gbc, y++, "Titolo:");
            sedeField = creaCampo(panel, gbc, y++, "Sede:");
            durataField = creaCampo(panel, gbc, y++, "Durata:");
            dataInizioField = creaCampo(panel, gbc, y++, "Data Inizio:");
            dataFineField = creaCampo(panel, gbc, y++, "Data Fine:");
            periodoIscrizioniField = creaCampo(panel, gbc, y++, "Periodo Iscrizioni:");
            dataAperturaIscrizioniField = creaCampo(panel, gbc, y++, "Data Apertura Iscrizioni:");
            dataChiusuraIscrizioniField = creaCampo(panel, gbc, y++, "Data Chiusura Iscrizioni:");
            maxIscrittiField = creaCampo(panel, gbc, y++, "Max Iscritti:");
            maxDimTeamField = creaCampo(panel, gbc, y++, "Max Dimensione Team:");

            // Giudici
            gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
            JLabel giudiciLabel = new JLabel("Giudici:");
            giudiciLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            panel.add(giudiciLabel, gbc);

            JButton selezionaButton = creaPulsante("Seleziona", new Color(70, 130, 180), new Color(40, 90, 160));
            gbc.gridx = 1;
            panel.add(selezionaButton, gbc);
            y++;

            selezionaButton.addActionListener(e -> mostraDialogSelezioneGiudici());

            // ===== PULSANTI INFERIORI =====
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setOpaque(false);

            // Pulsante Indietro a sinistra
            JButton indietroButton = creaPulsante("Indietro", new Color(150, 150, 150), new Color(100, 100, 100));
            indietroButton.setPreferredSize(new Dimension(120, 45));
            indietroButton.addActionListener(e -> {
                frame.dispose();
                callerFrame.setVisible(true);
            });
            bottomPanel.add(indietroButton, BorderLayout.WEST);

            // Pulsante Salva al centro con controllo campi e giudici
            JButton salvaButton = creaPulsante("Salva", new Color(34, 139, 34), new Color(20, 90, 20));
            salvaButton.setPreferredSize(new Dimension(150, 50));
            salvaButton.addActionListener(e -> {
                // Controllo campi vuoti
                if (titoloField.getText().isEmpty() ||
                        sedeField.getText().isEmpty() ||
                        durataField.getText().isEmpty() ||
                        dataInizioField.getText().isEmpty() ||
                        dataFineField.getText().isEmpty() ||
                        periodoIscrizioniField.getText().isEmpty() ||
                        dataAperturaIscrizioniField.getText().isEmpty() ||
                        dataChiusuraIscrizioniField.getText().isEmpty() ||
                        maxIscrittiField.getText().isEmpty() ||
                        maxDimTeamField.getText().isEmpty()) {

                    JOptionPane.showMessageDialog(frame,
                            "Compila tutti i campi!",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Controllo giudici
                if (giudiciSelezionati.size() < 2) {
                    JOptionPane.showMessageDialog(frame,
                            "Seleziona almeno 2 giudici!",
                            "Errore",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String titolo = titoloField.getText().trim();
                String sede = sedeField.getText().trim();
                String durata = durataField.getText().trim();
                String dataInizio = dataInizioField.getText().trim();
                String dataFine = dataFineField.getText().trim();
                String periodoIscrizioni = periodoIscrizioniField.getText().trim();
                String dataApertura = dataAperturaIscrizioniField.getText().trim();
                String dataChiusura = dataChiusuraIscrizioniField.getText().trim();
                int maxIscritti = Integer.parseInt(maxIscrittiField.getText().trim());
                int maxDimTeam = Integer.parseInt(maxDimTeamField.getText().trim());

                ControllerPlanner planner = new ControllerPlanner();

                //planner.controllerOpenHackathon(titolo,sede,dataInizio,dataFine,dataApertura,dataChiusura,maxIscritti,maxDimTeam,"mario.rossi",giudiciSelezionati);

                // Se tutto ok
                JOptionPane.showMessageDialog(frame,
                        "Hackathon creato con successo!",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                callerFrame.setVisible(true);
            });

            bottomPanel.add(salvaButton, BorderLayout.CENTER);

            gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
            panel.add(bottomPanel, gbc);

            gradientPanel.add(panel, BorderLayout.CENTER);
            frame.setContentPane(gradientPanel);
            frame.setVisible(true);
        }

        // ===== METODO HELPER PER CREARE CAMPI =====
        private JTextField creaCampo(JPanel panel, GridBagConstraints gbc, int y, String label) {
            gbc.gridx = 0; gbc.gridy = y;
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
            panel.add(lbl, gbc);

            JTextField field = new JTextField(20);
            gbc.gridx = 1;
            field.setPreferredSize(new Dimension(250, 28));
            panel.add(field, gbc);
            return field;
        }

        // ===== METODO HELPER PER CREARE PULSANTI =====
        private JButton creaPulsante(String text, Color base, Color hover) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 16));
            btn.setBackground(base);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(hover); }
                public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(base); }
            });
            return btn;
        }

        // ===== DIALOG SELEZIONE GIUDICI =====
        private void mostraDialogSelezioneGiudici() {

            ControllerPlanner planner = new ControllerPlanner();
            planner.controllerGetUsers("mario.rossi", utenti, names, surnames, passwords);

            JDialog dialog = new JDialog(frame, "Seleziona Giudici", true);
            dialog.setSize(350, 300); // aumentata leggermente la larghezza
            dialog.setLocationRelativeTo(frame);
            dialog.setLayout(new BorderLayout(10, 10));

            JLabel infoLabel = new JLabel("Seleziona almeno 2 giudici:");
            infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
            dialog.add(infoLabel, BorderLayout.NORTH);

            // Panel con BoxLayout verticale
            JPanel checkPanel = new JPanel();
            checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.Y_AXIS));
            checkPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            JCheckBox[] checkBoxes = new JCheckBox[utenti.size()];
            for (int i = 0; i < utenti.size(); i++) {
                checkBoxes[i] = new JCheckBox(utenti.get(i));
                checkPanel.add(checkBoxes[i]);
            }

            // Aggiunta dello scroll
            JScrollPane scrollPane = new JScrollPane(checkPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(320, 180)); // altezza massima visibile senza tagli
            dialog.add(scrollPane, BorderLayout.CENTER);

            JPanel buttonsPanel = new JPanel();
            JButton confermaBtn = creaPulsante("Conferma", new Color(70, 130, 180), new Color(40, 90, 160));
            JButton annullaBtn = creaPulsante("Annulla", new Color(150, 150, 150), new Color(100, 100, 100));
            buttonsPanel.add(confermaBtn);
            buttonsPanel.add(annullaBtn);
            dialog.add(buttonsPanel, BorderLayout.SOUTH);

            confermaBtn.addActionListener(e -> {
                giudiciSelezionati.clear();
                int count = 0;
                for (JCheckBox cb : checkBoxes) {
                    if (cb.isSelected()) {
                        giudiciSelezionati.add(cb.getText());
                        count++;
                    }
                }
                if (count < 2) {
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

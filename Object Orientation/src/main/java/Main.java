import gui.HomeGUI;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, "C'è stato un errore nella visualizzazione estetica", "Error", JOptionPane.ERROR_MESSAGE);
        }
        new HomeGUI();
    }
}
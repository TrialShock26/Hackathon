package main;

import gui.HomeGUI;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

/**
 * Classe fantoccio per avviare l'applicazione. Permette di chiamare la prima GUI visualizzata e
 * procedere con l'utilizzo del software.
 */
public class Main {
    /**
     * Punto di accesso dell'applicazione. Prima di creare la prima GUI, imposta il tema personalizzato fornito da
     * FlatLaf ({@link FlatLightLaf}) per l'estetica, mostrando un errore se questo non viene caricato correttamente.
     *
     * @param args argomenti di esecuzione
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, "C'è stato un errore nella visualizzazione estetica", "Error", JOptionPane.ERROR_MESSAGE);
        }
        new HomeGUI();
    }
}
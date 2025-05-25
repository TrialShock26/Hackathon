package gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import model.*;

public class HackathonsTableModel extends AbstractTableModel {
    private ArrayList<Hackathon> hackathonsToShow;
    private String[] columnNames = {"Titolo", "Sede","Data di inizio", "Durata", "Inizio registrazioni"};

    @Override
    public int getRowCount() {
        if(hackathonsToShow != null)
            return hackathonsToShow.size();
        return 0;
    }

    @Override
    public int getColumnCount() {return 5;}

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Hackathon current = hackathonsToShow.get(rowIndex);
        switch(columnIndex) {
            case 0: return current.getTitle();
            case 1: return current.getLocation();
            case 2: return current.getStartDate();
            case 3: return current.getPeriodOfTime();
            case 4: return current.getStartSubscriptionDate();
        }
        return null;
    }

    public Hackathon getHackathon(int row) {return hackathonsToShow.get(row);}

    @Override
    public String getColumnName(int column) {return columnNames[column];}

    public void setHackathons(ArrayList<Hackathon> hackathons) {hackathonsToShow = hackathons;}
}

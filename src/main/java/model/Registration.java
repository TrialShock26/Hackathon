package model;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Registration {
    private LocalDate openingDate;
    private LocalDate closingDate;
    private long periodOfTime;
    private Player player;
    private Hackathon hackathon;

    public Registration (LocalDate openingDate, LocalDate closingDate) {
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        periodOfTime = ChronoUnit.DAYS.between(openingDate, closingDate);
    }

    public LocalDate getOpeningDate() {return openingDate;}
    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
        this.periodOfTime = ChronoUnit.DAYS.between(openingDate, closingDate);
    }

    public LocalDate getClosingDate() {return closingDate;}
    public void setClosingDate(LocalDate closingDate) {
        this.closingDate = closingDate;
        this.periodOfTime = ChronoUnit.DAYS.between(openingDate, closingDate);
    }

    public long getPeriodOfTime() {return periodOfTime;}

    public Player getPlayer() {return player;}

    public Hackathon getHackathon() {return hackathon;}
}

package controller;

import model.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Controller {
    private ArrayList<Hackathon> allHackathons;

    public Controller() {
        this.allHackathons = new ArrayList<Hackathon>();
    }

    public ArrayList<Hackathon> getHackathons() {return allHackathons;}

    public void addHackathon(Hackathon hackathon) {allHackathons.add(hackathon);}
    public Hackathon removeHackathon(int index) {return allHackathons.remove(index);}

    public Hackathon getHackathon(int index) {return allHackathons.get(index);}

    public void fetchData() {
        allHackathons.add(new Hackathon("CodeBlast", "San Francisco", LocalDate.of(2023, 5, 1),
                LocalDate.of(2023, 5, 3), LocalDate.of(2023, 4, 25), LocalDate.of(2023, 4, 30), 100, 10,
                new Planner("johnDoe1", "pass123", "John", "Doe")));

        allHackathons.add(new Hackathon("HackTheFuture", "Berlin", LocalDate.of(2024, 7, 10),
                LocalDate.of(2024, 7, 12), LocalDate.of(2024, 6, 30), LocalDate.of(2024, 7, 5), 150, 12,
                new Planner("annaB", "securePass!", "Anna", "Baumann")));

        allHackathons.add(new Hackathon("InnoJam", "Tokyo", LocalDate.of(2022, 9, 15),
                LocalDate.of(2022, 9, 17), LocalDate.of(2022, 8, 20), LocalDate.of(2022, 9, 10), 80, 8,
                new Planner("takashiY", "tokyoHack", "Takashi", "Yamamoto")));

        allHackathons.add(new Hackathon("ByteBattle", "New York", LocalDate.of(2025, 1, 5),
                LocalDate.of(2025, 1, 7), LocalDate.of(2024, 12, 15), LocalDate.of(2024, 12, 31), 200, 15,
                new Planner("mariaG", "ny2025", "Maria", "Gonzalez")));

        allHackathons.add(new Hackathon("DevStorm", "London", LocalDate.of(2023, 11, 20),
                LocalDate.of(2023, 11, 22), LocalDate.of(2023, 10, 25), LocalDate.of(2023, 11, 15), 120, 10,
                new Planner("davidK", "l0ndonHack", "David", "Knight")));

        allHackathons.add(new Hackathon("CodeSprint", "Sydney", LocalDate.of(2023, 3, 18),
                LocalDate.of(2023, 3, 20), LocalDate.of(2023, 2, 25), LocalDate.of(2023, 3, 10), 90, 9,
                new Planner("lucyM", "sydSprint", "Lucy", "Martin")));

        allHackathons.add(new Hackathon("HackHorizons", "Toronto", LocalDate.of(2024, 4, 10),
                LocalDate.of(2024, 4, 12), LocalDate.of(2024, 3, 15), LocalDate.of(2024, 4, 5), 75, 6,
                new Planner("nathanC", "canHack", "Nathan", "Clark")));

        allHackathons.add(new Hackathon("CyberQuest", "Dubai", LocalDate.of(2025, 2, 1),
                LocalDate.of(2025, 2, 3), LocalDate.of(2024, 12, 20), LocalDate.of(2025, 1, 20), 110, 11,
                new Planner("fatimaZ", "dubai123", "Fatima", "Zayed")));

        allHackathons.add(new Hackathon("HackOps", "Paris", LocalDate.of(2022, 6, 14),
                LocalDate.of(2022, 6, 16), LocalDate.of(2022, 5, 20), LocalDate.of(2022, 6, 10), 130, 7,
                new Planner("leoP", "bonjour", "Leo", "Petit")));

        allHackathons.add(new Hackathon("TechRaid", "Singapore", LocalDate.of(2023, 8, 5),
                LocalDate.of(2023, 8, 7), LocalDate.of(2023, 7, 10), LocalDate.of(2023, 7, 30), 95, 5,
                new Planner("chenL", "sing2023", "Chen", "Li")));
    }
}
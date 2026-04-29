package com.betting.app.config;

import com.betting.app.model.Event;
import com.betting.app.model.Market;
import com.betting.app.model.Outcome;
import com.betting.app.repository.EventRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EventRepository eventRepository;

    @PersistenceContext
    private EntityManager em;

    public DataInitializer(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (eventRepository.count() > 0) return;

        // ── LIVE EVENTS ───────────────────────────────────────────────────────

        Event celticsMiami = event("Boston Celtics vs Miami Heat",
            "NBA", "NBA Playoffs 2026 – Conference Quarterfinals",
            "Boston Celtics", "Miami Heat",
            LocalDateTime.of(2026, 4, 29, 19, 30), "LIVE", true);
        market(celticsMiami, "Moneyline",
            o("Boston Celtics", 1.22), o("Miami Heat", 4.20));
        market(celticsMiami, "Point Spread",
            o("Celtics -8.5", 1.91), o("Heat +8.5", 1.91));
        market(celticsMiami, "Total Points",
            o("Over 212.5", 1.91), o("Under 212.5", 1.91));

        Event arsenalCity = event("Arsenal vs Manchester City",
            "SOCCER", "Premier League – Matchweek 35",
            "Arsenal", "Manchester City",
            LocalDateTime.of(2026, 4, 29, 20, 0), "LIVE", true);
        market(arsenalCity, "Match Result",
            o("Arsenal", 2.80), o("Draw", 3.40), o("Manchester City", 2.55));
        market(arsenalCity, "Both Teams to Score",
            o("Yes", 1.72), o("No", 2.10));
        market(arsenalCity, "Total Goals",
            o("Over 2.5", 1.95), o("Under 2.5", 1.87));

        Event leafsPanthers = event("Toronto Maple Leafs vs Florida Panthers",
            "NHL", "NHL Playoffs 2026 – Round 1 Game 1",
            "Toronto Maple Leafs", "Florida Panthers",
            LocalDateTime.of(2026, 4, 29, 19, 0), "LIVE", false);
        market(leafsPanthers, "Moneyline",
            o("Toronto Maple Leafs", 2.20), o("Florida Panthers", 1.72));
        market(leafsPanthers, "Puck Line",
            o("Leafs -1.5", 3.50), o("Panthers +1.5", 1.33));
        market(leafsPanthers, "Total Goals",
            o("Over 5.5", 1.95), o("Under 5.5", 1.87));

        Event jonesMiocic = event("Jon Jones vs Stipe Miocic II",
            "MMA", "UFC 316 – Heavyweight Championship",
            "Jon Jones", "Stipe Miocic",
            LocalDateTime.of(2026, 4, 29, 3, 0), "LIVE", true);
        market(jonesMiocic, "Fight Winner",
            o("Jon Jones", 1.28), o("Stipe Miocic", 3.80));
        market(jonesMiocic, "Method of Victory",
            o("KO / TKO", 2.10), o("Decision", 2.75), o("Submission", 8.00));
        market(jonesMiocic, "Total Rounds",
            o("Over 2.5", 2.30), o("Under 2.5", 1.65));

        // ── UPCOMING – TONIGHT ────────────────────────────────────────────────

        Event thunderGrizzlies = event("Oklahoma City Thunder vs Memphis Grizzlies",
            "NBA", "NBA Playoffs 2026 – Conference Quarterfinals",
            "Oklahoma City Thunder", "Memphis Grizzlies",
            LocalDateTime.of(2026, 4, 29, 22, 0), "UPCOMING", false);
        market(thunderGrizzlies, "Moneyline",
            o("Oklahoma City Thunder", 1.25), o("Memphis Grizzlies", 4.00));
        market(thunderGrizzlies, "Point Spread",
            o("Thunder -7.5", 1.91), o("Grizzlies +7.5", 1.91));
        market(thunderGrizzlies, "Total Points",
            o("Over 218.5", 1.91), o("Under 218.5", 1.91));

        // ── UPCOMING – TOMORROW ───────────────────────────────────────────────

        Event bucksPackers = event("Milwaukee Bucks vs Indiana Pacers",
            "NBA", "NBA Playoffs 2026 – Conference Quarterfinals",
            "Milwaukee Bucks", "Indiana Pacers",
            LocalDateTime.of(2026, 4, 30, 19, 30), "UPCOMING", false);
        market(bucksPackers, "Moneyline",
            o("Milwaukee Bucks", 1.59), o("Indiana Pacers", 2.45));
        market(bucksPackers, "Point Spread",
            o("Bucks -4.5", 1.91), o("Pacers +4.5", 1.91));
        market(bucksPackers, "Total Points",
            o("Over 224.5", 1.91), o("Under 224.5", 1.91));

        Event nuggetsLakers = event("Denver Nuggets vs Los Angeles Lakers",
            "NBA", "NBA Playoffs 2026 – Conference Quarterfinals",
            "Denver Nuggets", "Los Angeles Lakers",
            LocalDateTime.of(2026, 4, 30, 22, 0), "UPCOMING", false);
        market(nuggetsLakers, "Moneyline",
            o("Denver Nuggets", 1.44), o("Los Angeles Lakers", 2.90));
        market(nuggetsLakers, "Point Spread",
            o("Nuggets -5.5", 1.91), o("Lakers +5.5", 1.91));
        market(nuggetsLakers, "Total Points",
            o("Over 215.5", 1.91), o("Under 215.5", 1.91));

        Event yankeesRedSox = event("New York Yankees vs Boston Red Sox",
            "MLB", "MLB Regular Season 2026",
            "New York Yankees", "Boston Red Sox",
            LocalDateTime.of(2026, 4, 30, 19, 5), "UPCOMING", false);
        market(yankeesRedSox, "Moneyline",
            o("New York Yankees", 1.69), o("Boston Red Sox", 2.20));
        market(yankeesRedSox, "Run Line",
            o("Yankees -1.5", 2.25), o("Red Sox +1.5", 1.70));
        market(yankeesRedSox, "Total Runs",
            o("Over 8.5", 1.91), o("Under 8.5", 1.91));

        Event dodgersGiants = event("Los Angeles Dodgers vs San Francisco Giants",
            "MLB", "MLB Regular Season 2026",
            "Los Angeles Dodgers", "San Francisco Giants",
            LocalDateTime.of(2026, 4, 30, 22, 10), "UPCOMING", false);
        market(dodgersGiants, "Moneyline",
            o("Los Angeles Dodgers", 1.56), o("San Francisco Giants", 2.52));
        market(dodgersGiants, "Run Line",
            o("Dodgers -1.5", 2.30), o("Giants +1.5", 1.67));
        market(dodgersGiants, "Total Runs",
            o("Over 7.5", 1.91), o("Under 7.5", 1.91));

        Event avsDallas = event("Colorado Avalanche vs Dallas Stars",
            "NHL", "NHL Playoffs 2026 – Round 1 Game 1",
            "Colorado Avalanche", "Dallas Stars",
            LocalDateTime.of(2026, 4, 30, 21, 0), "UPCOMING", false);
        market(avsDallas, "Moneyline",
            o("Colorado Avalanche", 1.95), o("Dallas Stars", 1.87));
        market(avsDallas, "Puck Line",
            o("Avalanche -1.5", 3.25), o("Stars +1.5", 1.40));
        market(avsDallas, "Total Goals",
            o("Over 5.5", 2.05), o("Under 5.5", 1.77));

        // ── UPCOMING – THIS WEEK ──────────────────────────────────────────────

        Event realArsenal = event("Real Madrid vs Arsenal",
            "SOCCER", "UEFA Champions League – Semi-Final 1st Leg",
            "Real Madrid", "Arsenal",
            LocalDateTime.of(2026, 5, 1, 20, 0), "UPCOMING", true);
        market(realArsenal, "Match Result",
            o("Real Madrid", 2.20), o("Draw", 3.40), o("Arsenal", 3.30));
        market(realArsenal, "Both Teams to Score",
            o("Yes", 1.75), o("No", 2.05));
        market(realArsenal, "Total Goals",
            o("Over 2.5", 2.00), o("Under 2.5", 1.82));
        market(realArsenal, "Asian Handicap",
            o("Real Madrid -0.5", 2.10), o("Arsenal +0.5", 1.80));

        Event cityBayern = event("Manchester City vs Bayern Munich",
            "SOCCER", "UEFA Champions League – Semi-Final 1st Leg",
            "Manchester City", "Bayern Munich",
            LocalDateTime.of(2026, 5, 1, 20, 0), "UPCOMING", true);
        market(cityBayern, "Match Result",
            o("Manchester City", 2.05), o("Draw", 3.50), o("Bayern Munich", 3.50));
        market(cityBayern, "Both Teams to Score",
            o("Yes", 1.80), o("No", 2.00));
        market(cityBayern, "Total Goals",
            o("Over 2.5", 1.91), o("Under 2.5", 1.91));

        Event liverpoolChelsea = event("Liverpool vs Chelsea",
            "SOCCER", "Premier League – Matchweek 36",
            "Liverpool", "Chelsea",
            LocalDateTime.of(2026, 5, 3, 16, 30), "UPCOMING", false);
        market(liverpoolChelsea, "Match Result",
            o("Liverpool", 1.72), o("Draw", 3.75), o("Chelsea", 4.80));
        market(liverpoolChelsea, "Both Teams to Score",
            o("Yes", 1.67), o("No", 2.15));
        market(liverpoolChelsea, "Total Goals",
            o("Over 2.5", 1.87), o("Under 2.5", 1.95));

        Event caneloFight = event("Canelo Alvarez vs David Benavidez",
            "BOXING", "WBC Super Middleweight World Title",
            "Canelo Alvarez", "David Benavidez",
            LocalDateTime.of(2026, 5, 3, 22, 0), "UPCOMING", true);
        market(caneloFight, "Fight Winner",
            o("Canelo Alvarez", 1.44), o("David Benavidez", 2.85));
        market(caneloFight, "Method of Victory",
            o("KO / TKO", 1.87), o("Points Decision", 2.10), o("Draw", 18.00));
        market(caneloFight, "Total Rounds",
            o("Over 9.5", 1.95), o("Under 9.5", 1.87));

        Event italianOpen = event("Italian Open – Men's Singles Winner",
            "TENNIS", "ATP Masters 1000 – Rome",
            null, null,
            LocalDateTime.of(2026, 5, 18, 14, 0), "UPCOMING", false);
        market(italianOpen, "Outright Winner",
            o("Jannik Sinner", 2.10),
            o("Carlos Alcaraz", 2.75),
            o("Alexander Zverev", 8.00),
            o("Novak Djokovic", 9.00),
            o("Holger Rune", 18.00),
            o("Andrey Medvedev", 12.00));

        Event senate2026 = event("US Senate – Party Control 2026",
            "POLITICS", "US Midterm Elections 2026",
            null, null,
            LocalDateTime.of(2026, 11, 3, 20, 0), "UPCOMING", false);
        market(senate2026, "Senate Majority",
            o("Republican Party", 1.50), o("Democratic Party", 2.55));
        market(senate2026, "House Majority",
            o("Republican Party", 1.65), o("Democratic Party", 2.30));

        Event knicks76ers = event("New York Knicks vs Philadelphia 76ers",
            "NBA", "NBA Playoffs 2026 – Conference Quarterfinals",
            "New York Knicks", "Philadelphia 76ers",
            LocalDateTime.of(2026, 4, 30, 17, 30), "UPCOMING", false);
        market(knicks76ers, "Moneyline",
            o("New York Knicks", 1.38), o("Philadelphia 76ers", 3.10));
        market(knicks76ers, "Point Spread",
            o("Knicks -3.5", 1.91), o("76ers +3.5", 1.91));
        market(knicks76ers, "Total Points",
            o("Over 210.5", 1.91), o("Under 210.5", 1.91));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Event event(String name, String category, String competition,
                        String home, String away,
                        LocalDateTime date, String status, boolean featured) {
        return eventRepository.save(
            new Event(name, category, competition, home, away, date, status, featured));
    }

    private void market(Event event, String name, Outcome... outcomes) {
        Market m = new Market(name, event);
        em.persist(m);
        for (Outcome o : outcomes) {
            o.getClass(); // non-null check
            em.persist(new Outcome(o.getName(), o.getDecimalOdds(), m));
        }
    }

    /** Temporary outcome shell — used only in varargs before persistence. */
    private Outcome o(String name, double odds) {
        return new Outcome(name, odds, null);
    }
}

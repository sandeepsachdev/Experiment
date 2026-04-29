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

        // ── AFL ───────────────────────────────────────────────────────────────

        Event collCarlton = event("Collingwood vs Carlton",
            "AFL", "AFL 2026 – Round 7",
            "Collingwood", "Carlton",
            LocalDateTime.of(2026, 5, 1, 19, 30), "UPCOMING", true);
        market(collCarlton, "Head to Head",
            o("Collingwood", 1.72), o("Carlton", 2.15));
        market(collCarlton, "Line",
            o("Collingwood -12.5", 1.91), o("Carlton +12.5", 1.91));
        market(collCarlton, "Total Points",
            o("Over 165.5", 1.91), o("Under 165.5", 1.91));

        Event swansGWS = event("Sydney Swans vs GWS Giants",
            "AFL", "AFL 2026 – Round 7",
            "Sydney Swans", "GWS Giants",
            LocalDateTime.of(2026, 5, 2, 16, 10), "UPCOMING", false);
        market(swansGWS, "Head to Head",
            o("Sydney Swans", 1.55), o("GWS Giants", 2.50));
        market(swansGWS, "Line",
            o("Swans -8.5", 1.91), o("GWS +8.5", 1.91));
        market(swansGWS, "Total Points",
            o("Over 158.5", 1.91), o("Under 158.5", 1.91));

        Event richmondGeelong = event("Richmond vs Geelong",
            "AFL", "AFL 2026 – Round 7",
            "Richmond", "Geelong",
            LocalDateTime.of(2026, 5, 3, 14, 30), "UPCOMING", false);
        market(richmondGeelong, "Head to Head",
            o("Richmond", 3.10), o("Geelong", 1.38));
        market(richmondGeelong, "Line",
            o("Richmond +20.5", 1.91), o("Geelong -20.5", 1.91));
        market(richmondGeelong, "Total Points",
            o("Over 155.5", 1.91), o("Under 155.5", 1.91));

        Event brisbaneFreo = event("Brisbane Lions vs Fremantle",
            "AFL", "AFL 2026 – Round 8",
            "Brisbane Lions", "Fremantle",
            LocalDateTime.of(2026, 5, 8, 19, 20), "UPCOMING", false);
        market(brisbaneFreo, "Head to Head",
            o("Brisbane Lions", 1.45), o("Fremantle", 2.80));
        market(brisbaneFreo, "Line",
            o("Lions -14.5", 1.91), o("Fremantle +14.5", 1.91));
        market(brisbaneFreo, "Total Points",
            o("Over 162.5", 1.91), o("Under 162.5", 1.91));

        Event aflPremier = event("AFL 2026 Premiership – Outright Winner",
            "AFL", "AFL 2026 Season",
            null, null,
            LocalDateTime.of(2026, 9, 26, 14, 30), "UPCOMING", true);
        market(aflPremier, "Premiership Winner",
            o("Collingwood", 5.50),
            o("Brisbane Lions", 6.00),
            o("Sydney Swans", 7.00),
            o("Geelong", 7.50),
            o("Carlton", 9.00),
            o("Melbourne", 10.00),
            o("GWS Giants", 11.00),
            o("Fremantle", 12.00),
            o("Richmond", 15.00),
            o("Port Adelaide", 13.00),
            o("Western Bulldogs", 17.00),
            o("Hawthorn", 19.00));

        // ── NRL ───────────────────────────────────────────────────────────────

        Event roostersRabbits = event("Sydney Roosters vs South Sydney Rabbitohs",
            "NRL", "NRL 2026 – Round 9",
            "Sydney Roosters", "South Sydney Rabbitohs",
            LocalDateTime.of(2026, 4, 30, 20, 0), "UPCOMING", true);
        market(roostersRabbits, "Head to Head",
            o("Sydney Roosters", 1.65), o("South Sydney Rabbitohs", 2.25));
        market(roostersRabbits, "Line",
            o("Roosters -6.5", 1.91), o("Rabbitohs +6.5", 1.91));
        market(roostersRabbits, "Total Points",
            o("Over 43.5", 1.91), o("Under 43.5", 1.91));

        Event penrithParramatta = event("Penrith Panthers vs Parramatta Eels",
            "NRL", "NRL 2026 – Round 9",
            "Penrith Panthers", "Parramatta Eels",
            LocalDateTime.of(2026, 5, 1, 19, 50), "UPCOMING", false);
        market(penrithParramatta, "Head to Head",
            o("Penrith Panthers", 1.42), o("Parramatta Eels", 2.88));
        market(penrithParramatta, "Line",
            o("Panthers -10.5", 1.91), o("Eels +10.5", 1.91));
        market(penrithParramatta, "Total Points",
            o("Over 46.5", 1.91), o("Under 46.5", 1.91));

        Event brisbaneStorm = event("Brisbane Broncos vs Melbourne Storm",
            "NRL", "NRL 2026 – Round 9",
            "Brisbane Broncos", "Melbourne Storm",
            LocalDateTime.of(2026, 5, 2, 18, 0), "UPCOMING", false);
        market(brisbaneStorm, "Head to Head",
            o("Brisbane Broncos", 2.40), o("Melbourne Storm", 1.62));
        market(brisbaneStorm, "Line",
            o("Broncos +8.5", 1.91), o("Storm -8.5", 1.91));
        market(brisbaneStorm, "Total Points",
            o("Over 44.5", 1.91), o("Under 44.5", 1.91));

        Event nrlPremier = event("NRL 2026 Premiership – Outright Winner",
            "NRL", "NRL 2026 Season",
            null, null,
            LocalDateTime.of(2026, 10, 4, 18, 0), "UPCOMING", true);
        market(nrlPremier, "Premiership Winner",
            o("Penrith Panthers", 4.50),
            o("Melbourne Storm", 5.00),
            o("Sydney Roosters", 7.00),
            o("Brisbane Broncos", 8.00),
            o("South Sydney Rabbitohs", 9.00),
            o("Parramatta Eels", 11.00),
            o("Newcastle Knights", 13.00),
            o("North Queensland Cowboys", 14.00),
            o("Cronulla Sharks", 13.00),
            o("Canberra Raiders", 17.00));

        // ── HORSE RACING ──────────────────────────────────────────────────────

        Event doombenCup = event("Doomben Cup 2026",
            "RACING", "Group 1 – Eagle Farm, Brisbane",
            null, null,
            LocalDateTime.of(2026, 5, 9, 15, 35), "UPCOMING", true);
        market(doombenCup, "Win",
            o("Anamoe", 3.50),
            o("Profiteer", 4.50),
            o("Zaaki", 5.00),
            o("Alligator Blood", 6.00),
            o("Fangirl", 7.00),
            o("Home Affairs", 8.00),
            o("Sir Dragonet", 10.00),
            o("Wisdom of Water", 15.00));

        Event stradbrokeHandicap = event("Stradbroke Handicap 2026",
            "RACING", "Group 1 – Eagle Farm, Brisbane",
            null, null,
            LocalDateTime.of(2026, 6, 6, 16, 15), "UPCOMING", false);
        market(stradbrokeHandicap, "Win",
            o("Rothfire", 5.00),
            o("Nature Strip", 5.50),
            o("Eduardo", 6.00),
            o("Masked Crusader", 7.00),
            o("Incentivise", 8.00),
            o("Tofane", 9.00),
            o("Crosshaven", 10.00),
            o("Gytrash", 12.00));

        Event melbCup = event("Melbourne Cup 2026",
            "RACING", "Group 1 – Flemington Racecourse",
            null, null,
            LocalDateTime.of(2026, 11, 3, 15, 0), "UPCOMING", true);
        market(melbCup, "Win",
            o("Without A Fight", 6.00),
            o("Gold Trip", 7.00),
            o("Verry Elleegant", 8.00),
            o("Delectation", 9.00),
            o("Emissary", 10.00),
            o("Twilight Payment", 11.00),
            o("Montefilia", 12.00),
            o("Explosive Jack", 14.00),
            o("Smokin' Romans", 15.00),
            o("Knights Order", 17.00));
        market(melbCup, "Winning Country",
            o("Australia", 2.20),
            o("Ireland", 2.80),
            o("UK / Europe", 4.00),
            o("New Zealand", 6.00));

        // ── IRAN / MIDDLE EAST POLITICS ───────────────────────────────────────

        Event iranCeasefire = event("Iran Conflict – Ceasefire by 31 Dec 2026",
            "POLITICS", "Iran / Middle East 2026",
            null, null,
            LocalDateTime.of(2026, 12, 31, 23, 59), "UPCOMING", true);
        market(iranCeasefire, "Ceasefire Agreement Reached",
            o("Yes", 2.75), o("No", 1.50));
        market(iranCeasefire, "Ceasefire Brokered By",
            o("United Nations", 3.50),
            o("United States", 4.00),
            o("Qatar / Gulf States", 5.00),
            o("No Ceasefire", 1.50));

        Event iranNuclear = event("Iran Nuclear Deal – Signed Before 2027",
            "POLITICS", "Iran / Middle East 2026",
            null, null,
            LocalDateTime.of(2026, 12, 31, 23, 59), "UPCOMING", false);
        market(iranNuclear, "Deal Signed",
            o("Yes", 3.75), o("No", 1.30));
        market(iranNuclear, "Deal Type",
            o("Full Agreement", 5.00),
            o("Partial / Interim Deal", 4.50),
            o("No Deal", 1.30));

        Event usIranMilitary = event("US Military Action Against Iran in 2026",
            "POLITICS", "Iran / Middle East 2026",
            null, null,
            LocalDateTime.of(2026, 12, 31, 23, 59), "UPCOMING", true);
        market(usIranMilitary, "US Strikes Iran",
            o("Yes", 3.20), o("No", 1.36));
        market(usIranMilitary, "Type of Action",
            o("Airstrikes only", 4.00),
            o("Naval blockade", 6.00),
            o("Ground forces deployed", 12.00),
            o("No military action", 1.36));

        Event midEastRegionalWar = event("Middle East – Regional War by July 2027",
            "POLITICS", "Iran / Middle East 2026",
            null, null,
            LocalDateTime.of(2027, 7, 1, 0, 0), "UPCOMING", false);
        market(midEastRegionalWar, "Regional War Breaks Out",
            o("Yes", 2.40), o("No", 1.57));
        market(midEastRegionalWar, "Next Country Drawn In",
            o("Saudi Arabia", 3.50),
            o("Jordan", 5.00),
            o("Turkey", 6.00),
            o("Egypt", 8.00),
            o("No escalation", 1.57));

        Event iranLeader = event("Iran – Supreme Leader Change by End of 2027",
            "POLITICS", "Iran / Middle East 2026",
            null, null,
            LocalDateTime.of(2027, 12, 31, 23, 59), "UPCOMING", false);
        market(iranLeader, "Supreme Leader Changes",
            o("Yes", 4.50), o("No", 1.22));
        market(iranLeader, "Transition Outcome",
            o("Orderly succession", 5.50),
            o("Power struggle / instability", 7.00),
            o("Military takes control", 12.00),
            o("No change", 1.22));

        // ── ELECTIONS ─────────────────────────────────────────────────────────

        Event vicElection = event("Victorian State Election 2026",
            "POLITICS", "Victorian State Election – 28 November 2026",
            null, null,
            LocalDateTime.of(2026, 11, 28, 18, 0), "UPCOMING", true);
        market(vicElection, "Election Winner",
            o("Labor (incumbent)", 1.40),
            o("Liberal-National Coalition", 3.00),
            o("Hung Parliament", 12.00));
        market(vicElection, "Labor Majority Size",
            o("Majority government", 1.50),
            o("Minority government", 3.80),
            o("Coalition wins", 3.00));

        Event brazilElection = event("Brazilian Presidential Election 2026",
            "POLITICS", "Brazil General Election – 4 October 2026",
            null, null,
            LocalDateTime.of(2026, 10, 4, 20, 0), "UPCOMING", true);
        market(brazilElection, "Presidential Winner",
            o("Luiz Inácio Lula da Silva", 2.10),
            o("Jair Bolsonaro", 3.50),
            o("Other / Third Candidate", 5.00));
        market(brazilElection, "Goes to Second Round",
            o("Yes", 1.45), o("No", 2.70));

        Event frenchElection = event("French Presidential Election 2027",
            "POLITICS", "France Presidential Election – April 2027",
            null, null,
            LocalDateTime.of(2027, 4, 23, 20, 0), "UPCOMING", false);
        market(frenchElection, "First Round Leader",
            o("Marine Le Pen (RN)", 2.75),
            o("Centre-Left candidate", 3.20),
            o("Centre-Right candidate", 4.00),
            o("Jean-Luc Mélenchon (LFI)", 8.00));
        market(frenchElection, "Presidential Winner",
            o("Marine Le Pen", 3.00),
            o("Centre-Left candidate", 3.50),
            o("Centre-Right candidate", 4.50),
            o("Far-Left candidate", 10.00));
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

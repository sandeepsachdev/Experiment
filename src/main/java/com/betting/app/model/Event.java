package com.betting.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private String competition;
    private String homeTeam;
    private String awayTeam;
    private LocalDateTime eventDate;
    private String status;   // LIVE | UPCOMING
    private boolean featured;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private List<Market> markets = new ArrayList<>();

    public Event() {}

    public Event(String name, String category, String competition,
                 String homeTeam, String awayTeam,
                 LocalDateTime eventDate, String status, boolean featured) {
        this.name        = name;
        this.category    = category;
        this.competition = competition;
        this.homeTeam    = homeTeam;
        this.awayTeam    = awayTeam;
        this.eventDate   = eventDate;
        this.status      = status;
        this.featured    = featured;
    }

    public Long getId()                  { return id; }
    public String getName()              { return name; }
    public String getCategory()          { return category; }
    public String getCompetition()       { return competition; }
    public String getHomeTeam()          { return homeTeam; }
    public String getAwayTeam()          { return awayTeam; }
    public LocalDateTime getEventDate()  { return eventDate; }
    public String getStatus()            { return status; }
    public boolean isFeatured()          { return featured; }
    public List<Market> getMarkets()     { return markets; }
}

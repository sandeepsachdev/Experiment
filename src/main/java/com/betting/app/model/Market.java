package com.betting.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "markets")
public class Market {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;

    @OneToMany(mappedBy = "market", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private List<Outcome> outcomes = new ArrayList<>();

    public Market() {}

    public Market(String name, Event event) {
        this.name  = name;
        this.event = event;
    }

    public Long getId()              { return id; }
    public String getName()          { return name; }
    public Event getEvent()          { return event; }
    public List<Outcome> getOutcomes() { return outcomes; }
}

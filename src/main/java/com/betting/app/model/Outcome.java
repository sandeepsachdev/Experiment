package com.betting.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "outcomes")
public class Outcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double decimalOdds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_id")
    @JsonIgnore
    private Market market;

    public Outcome() {}

    public Outcome(String name, double decimalOdds, Market market) {
        this.name        = name;
        this.decimalOdds = decimalOdds;
        this.market      = market;
    }

    public Long getId()           { return id; }
    public String getName()       { return name; }
    public double getDecimalOdds(){ return decimalOdds; }
    public Market getMarket()     { return market; }
}

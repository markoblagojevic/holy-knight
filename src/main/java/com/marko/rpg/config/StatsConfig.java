package com.marko.rpg.config;

import lombok.Data;

@Data
public class StatsConfig {

    private int maxHealth;
    private int currentHealth;
    private int attack;
    private int defense;
    private int magic;
}

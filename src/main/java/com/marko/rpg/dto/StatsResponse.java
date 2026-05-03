package com.marko.rpg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {

    private int maxHealth;
    private int currentHealth;
    private int attack;
    private int defense;
    private int magic;
}

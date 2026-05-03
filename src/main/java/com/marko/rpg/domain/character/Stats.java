package com.marko.rpg.domain.character;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stats {

    private int maxHealth;
    private int currentHealth;
    private int attack;
    private int defense;
    private int magic;
}

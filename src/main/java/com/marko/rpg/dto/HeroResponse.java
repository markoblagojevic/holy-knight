package com.marko.rpg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HeroResponse {

    private String id;
    private String name;
    private StatsResponse stats;
    private List<ActiveEffectResponse> activeEffects;

    private int level;
    private int experience;
    private int experienceToNextLevel;

    private List<MoveResponse> learnedMoves;
    private List<MoveResponse> equippedMoves;
}

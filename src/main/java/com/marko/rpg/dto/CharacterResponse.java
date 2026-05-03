package com.marko.rpg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CharacterResponse {

    private String id;
    private String name;
    private StatsResponse stats;
    private List<MoveResponse> moves;
    private List<ActiveEffectResponse> activeEffects;
}

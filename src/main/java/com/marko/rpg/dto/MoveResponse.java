package com.marko.rpg.dto;

import com.marko.rpg.domain.move.EffectType;
import com.marko.rpg.domain.move.MoveType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MoveResponse {

    private String id;
    private String name;
    private String description;
    private MoveType type;
    private EffectType effectType;
    private int baseValue;
    private int duration;
}

package com.marko.rpg.config;

import com.marko.rpg.domain.move.EffectType;
import com.marko.rpg.domain.move.MoveType;
import lombok.Data;

@Data
public class MoveConfig {

    private String id;
    private String name;
    private String description;

    private MoveType type;
    private EffectType effectType;

    private int baseValue;
    private int duration;
}

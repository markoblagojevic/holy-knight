package com.marko.rpg.domain.move;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Move {

    private String id;
    private String name;
    private String description;

    private MoveType Type;
    private EffectType effectType;

    private int baseValue;
    private int duration;
}

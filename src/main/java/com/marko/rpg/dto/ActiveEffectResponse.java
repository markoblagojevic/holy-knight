package com.marko.rpg.dto;

import com.marko.rpg.domain.move.EffectType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActiveEffectResponse {

    private EffectType effectType;
    private int value;
    private int remainingTurns;
}

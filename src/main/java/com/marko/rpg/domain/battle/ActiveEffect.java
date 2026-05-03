package com.marko.rpg.domain.battle;

import com.marko.rpg.domain.move.EffectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActiveEffect {

    private EffectType effectType;

    private int value;

    private int remainingTurns;
}

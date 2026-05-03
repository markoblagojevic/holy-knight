package com.marko.rpg.dto;

import com.marko.rpg.domain.battle.BattleEventType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BattleEventResponse {

    private BattleEventType type;
    private String message;
}

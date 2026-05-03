package com.marko.rpg.domain.battle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class BattleEvent {
    private BattleEventType type;
    private String message;


}

package com.marko.rpg.dto;

import com.marko.rpg.domain.battle.BattleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BattleResponse {

    private HeroResponse hero;
    private CharacterResponse monster;
    private BattleStatus status;
    private List<BattleEventResponse> events;
    private MoveResponse learnedMove;
}

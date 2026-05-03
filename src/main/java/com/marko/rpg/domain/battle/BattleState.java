package com.marko.rpg.domain.battle;

import com.marko.rpg.domain.character.Hero;
import com.marko.rpg.domain.character.Monster;
import com.marko.rpg.domain.move.Move;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BattleState {

    private Hero hero;
    private Monster monster;

    private BattleStatus status = BattleStatus.IN_PROGRESS;

    private List<BattleEvent> event = new ArrayList<>();

    private Move learnedMove;



}

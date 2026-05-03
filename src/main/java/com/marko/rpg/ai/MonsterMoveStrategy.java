package com.marko.rpg.ai;

import com.marko.rpg.domain.battle.BattleState;
import com.marko.rpg.domain.character.Monster;
import com.marko.rpg.domain.move.Move;

public interface MonsterMoveStrategy {

    Move chooseMove(BattleState state, Monster monster);
}

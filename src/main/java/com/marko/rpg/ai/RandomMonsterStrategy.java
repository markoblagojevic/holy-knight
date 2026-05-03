package com.marko.rpg.ai;

import com.marko.rpg.domain.battle.BattleState;
import com.marko.rpg.domain.character.Monster;
import com.marko.rpg.domain.move.Move;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class RandomMonsterStrategy implements MonsterMoveStrategy {


    private final Random random = new Random();


    @Override
    public Move chooseMove(BattleState state, Monster monster) {
        List<Move> moves = monster.getMoves();

        int randomIndex = random.nextInt(moves.size());
        return moves.get(randomIndex);
    }
}

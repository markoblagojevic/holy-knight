package com.marko.rpg.ai;

import com.marko.rpg.domain.battle.BattleState;
import com.marko.rpg.domain.character.Hero;
import com.marko.rpg.domain.character.Monster;
import com.marko.rpg.domain.move.EffectType;
import com.marko.rpg.domain.move.Move;
import com.marko.rpg.domain.move.MoveType;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
public class SmartMonsterStrategy implements MonsterMoveStrategy {

    private static final double LOW_HP_THRESHOLD = 0.3;

    private final Random random = new Random();

    @Override
    public Move chooseMove(BattleState state, Monster monster) {

        Hero hero = state.getHero();

        if(isLowHealth(monster)) {

            Optional<Move> healMove = findHealMove(monster);

            if(healMove.isPresent()) {
                return healMove.get();
            }
        }

        Optional<Move> killingMove = findKillingMove(monster,hero);

            if(killingMove.isPresent()) {
                return killingMove.get();
            }

        return chooseRandomMove(monster);

    }

    private boolean isLowHealth(Monster monster){

        double currentHp = monster.getStats().getCurrentHealth();
        double maxHp = monster.getStats().getMaxHealth();

        double healthPercentage = currentHp / maxHp;

        return healthPercentage <= LOW_HP_THRESHOLD;

    }

    private Optional<Move> findHealMove(Monster monster) {

        return monster.getMoves().stream().filter(move -> move.getEffectType() == EffectType.HEAL || move.getEffectType() == EffectType.DRAIN_LIFE).findFirst();
    }
    private Optional<Move> findKillingMove(Monster monster, Hero hero) {

        return monster.getMoves().stream().filter(move -> isDamageMove(move)).filter(move -> canKill(move,monster,hero)).max(Comparator.comparingInt(move -> estimateDamage(move,monster)));
    }


    private boolean isDamageMove(Move move){
        return move.getEffectType() == EffectType.DAMAGE || move.getEffectType() == EffectType.DRAIN_LIFE;
    }

    private boolean canKill(Move move, Monster monster, Hero hero) {

        int estimateDamage = estimateDamage(move,monster);

        return estimateDamage >= hero.getStats().getCurrentHealth();
    }

    private int estimateDamage(Move move, Monster monster) {

        if(move.getType()== MoveType.PHYSICAL){
            return move.getBaseValue()+monster.getStats().getAttack();
        }
        if(move.getType()== MoveType.MAGIC){
            return move.getBaseValue()+monster.getStats().getMagic();
        }
        return 0;
    }

    private Move chooseRandomMove(Monster monster) {

        List<Move> moves = monster.getMoves();

        int randomIndex = random.nextInt(moves.size());
        return moves.get(randomIndex);
    }
}

package com.marko.rpg.domain.battle;

import com.marko.rpg.domain.character.GameCharacter;
import com.marko.rpg.domain.character.Stats;
import com.marko.rpg.domain.move.EffectType;
import com.marko.rpg.domain.move.Move;
import com.marko.rpg.domain.move.MoveType;
import org.springframework.stereotype.Component;

import java.util.Iterator;

@Component
public class BattleEngine {


    private void addEvent(BattleState state, BattleEventType type, String message) {

        state.getEvent().add(new BattleEvent(type, message));
    }

    private void applyTemporaryEffect(BattleState state, GameCharacter attacker, GameCharacter defender,Move move) {

        GameCharacter target = isBuff(move.getEffectType())?attacker:defender;

        ActiveEffect effect = new ActiveEffect(
                move.getEffectType(),
                move.getBaseValue(),
                move.getDuration());

        target.getActiveEffects().add(effect);

        addEvent(
                state,
                BattleEventType.EFFECT_APPLIED,
                target.getName() + " received " + move.getEffectType() + " for " + move.getDuration() + " turns.");
    }



    private int calculateDamage(GameCharacter attacker, GameCharacter defender, Move move) {
        if (move.getType() == MoveType.PHYSICAL) {
            return Math.max(
                    1,
                    move.getBaseValue() + getEffectiveAttack(attacker) - getEffectiveDefense(defender)
            );
        }

        if (move.getType() == MoveType.MAGIC) {
            return Math.max(
                    1,
                    move.getBaseValue() + getEffectiveMagic(attacker)
            );
        }

        return 0;
    }

    private int getEffectModifier(GameCharacter character, EffectType effectType) {
        return character.getActiveEffects().stream().filter(effect ->effect.getEffectType()==effectType).mapToInt(ActiveEffect::getValue).sum();
    }

    private boolean isBuff(EffectType effectType) {
        return effectType == EffectType.BUFF_ATTACK || effectType == EffectType.BUFF_DEFENSE || effectType == EffectType.BUFF_MAGIC;
    }

    private boolean isBuffOrDebuff(EffectType effectType){
        return isBuff(effectType) || effectType == EffectType.DEBUFF_ATTACK  || effectType == EffectType.DEBUFF_DEFENSE ||  effectType == EffectType.DEBUFF_MAGIC;
    }


    private int getEffectiveAttack(GameCharacter character){
        return character.getStats().getAttack() + getEffectModifier(character,EffectType.BUFF_ATTACK) - getEffectModifier(character, EffectType.DEBUFF_ATTACK);
    }


    private int getEffectiveDefense(GameCharacter character) {
        return character.getStats().getDefense() + getEffectModifier(character, EffectType.BUFF_DEFENSE) - getEffectModifier(character, EffectType.DEBUFF_DEFENSE);
    }

    private int getEffectiveMagic(GameCharacter character) {
        return character.getStats().getMagic() + getEffectModifier(character, EffectType.BUFF_MAGIC) - getEffectModifier(character, EffectType.DEBUFF_MAGIC);
    }





    private void applyDamage(BattleState state, GameCharacter attacker, GameCharacter defender, Move move) {

        int damage = calculateDamage(attacker, defender, move);

        int maxHealth = Math.max(0, defender.getStats().getCurrentHealth() - damage);

        defender.getStats().setCurrentHealth(maxHealth);

        addEvent(state, BattleEventType.DAMAGE_DEALT, attacker.getName()+ " dealt " + damage +" damage to "+ defender.getName());
    }

    private void applyHeal (BattleState state, GameCharacter character, Move move){

        int healing = move.getBaseValue() + getEffectiveMagic(character);

        int newHealth = Math.min(character.getStats().getMaxHealth(),character.getStats().getCurrentHealth()+healing);

        character.getStats().setCurrentHealth(newHealth);

        addEvent(state,BattleEventType.HEAL_APPLIED,character.getName()+ "healed for" + healing+" HP. ");

    }

    private void applyDrainLife(BattleState state, GameCharacter attacker, GameCharacter defender, Move move) {

        int damage = move.getBaseValue() + getEffectiveMagic(attacker);

        int defenderNewHealth = Math.max(
                0,
                defender.getStats().getCurrentHealth() - damage);

        defender.getStats().setCurrentHealth(defenderNewHealth);

        int attackerNewHealth = Math.min(
                attacker.getStats().getMaxHealth(),
                attacker.getStats().getCurrentHealth() + damage
        );

        attacker.getStats().setCurrentHealth(attackerNewHealth);

        addEvent(state,BattleEventType.DAMAGE_DEALT,attacker.getName()+ " drained "+ damage +" HP from" + defender.getName()+ ".");

        addEvent(state,BattleEventType.HEAL_APPLIED,attacker.getName()+ " healed for" + damage+ "HP.");

    }

    private void tickEffects(BattleState state, GameCharacter character){

        Iterator<ActiveEffect> iterator = character.getActiveEffects().iterator();

        while (iterator.hasNext()){
            ActiveEffect effect = iterator.next();

            effect.setRemainingTurns(effect.getRemainingTurns() - 1);

            if(effect.getRemainingTurns() <= 0){
                iterator.remove();

                addEvent(state,BattleEventType.EFFECT_EXPIRED, character.getName() + "'s " + effect.getEffectType() + " expired.");
            }
        }
    }

    private void updateBattleStatus(BattleState state){
        if(state.getHero().getStats().getCurrentHealth() <= 0){
            state.setStatus(BattleStatus.HERO_LOST);

            addEvent(state,BattleEventType.CHARACTER_DEFEATED,state.getHero().getName()+ " was defeated.");

            return;
        }
        if(state.getMonster().getStats().getCurrentHealth() <= 0){
            state.setStatus(BattleStatus.HERO_WON);

            addEvent(state,BattleEventType.CHARACTER_DEFEATED,state.getMonster().getName()+ " was defeated.");
        }
    }


    public BattleState applyMove(BattleState state, GameCharacter attacker, GameCharacter defender, Move move) {

        addEvent(state,BattleEventType.MOVE_USED,attacker.getName()+" used "+move.getName()+".");

        if(move.getEffectType()== EffectType.DAMAGE){
            applyDamage(state, attacker, defender, move);
        } else if (move.getEffectType()==EffectType.HEAL) {
            applyHeal(state, attacker, move);
        } else if (move.getEffectType()==EffectType.DRAIN_LIFE) {
            applyDrainLife(state,attacker,defender,move);
        } else if (isBuffOrDebuff(move.getEffectType())) {
            applyTemporaryEffect(state, attacker, defender, move);
        }

        updateBattleStatus(state);

        if(state.getStatus()==BattleStatus.IN_PROGRESS){
            tickEffects(state,attacker);
        }

        return state;
    }



}

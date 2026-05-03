package com.marko.rpg.service;

import com.marko.rpg.ai.SmartMonsterStrategy;
import com.marko.rpg.domain.battle.BattleEngine;
import com.marko.rpg.domain.battle.BattleState;
import com.marko.rpg.domain.battle.BattleStatus;
import com.marko.rpg.domain.character.Hero;
import com.marko.rpg.domain.character.Monster;
import com.marko.rpg.domain.move.Move;
import com.marko.rpg.domain.run.Encounter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BattleService {

    private final RunService runService;
    private final BattleEngine battleEngine;
    private final SmartMonsterStrategy smartMonsterStrategy;

    private final Map<String, BattleState> activeBattles = new HashMap<>();

    public BattleService(RunService runService, BattleEngine battleEngine, SmartMonsterStrategy smartMonsterStrategy) {
        this.runService = runService;
        this.battleEngine = battleEngine;
        this.smartMonsterStrategy = smartMonsterStrategy;
    }

    public BattleState startBattle(String runId){

        Encounter encounter = runService.getCurrentEncounter(runId);

        BattleState battleState = new BattleState();

        battleState.setHero(runService.getRun(runId).getHero());
        battleState.setMonster(encounter.getMonster());
        battleState.setStatus(BattleStatus.IN_PROGRESS);

        resetTemporaryEffects(battleState.getHero(), battleState.getMonster());

        activeBattles.put(runId, battleState);

        return battleState;
    }

    public BattleState playTurn(String runId, String moveId){
        BattleState battleState = getActiveBattle(runId);

        if (battleState.getStatus() != BattleStatus.IN_PROGRESS) {
            return battleState;
        }

        Hero hero = battleState.getHero();
        Monster monster = battleState.getMonster();

        Move heroMove = findHeroMove(hero, moveId);

        battleEngine.applyMove(battleState, hero, monster, heroMove);

        if (battleState.getStatus() == BattleStatus.HERO_WON) {
            Move learnedMove = runService.completeCurrentEncounter(runId);
            battleState.setLearnedMove(learnedMove);

            activeBattles.remove(runId);
            return battleState;
        }

        Move monsterMove = smartMonsterStrategy.chooseMove(battleState, monster);

        battleEngine.applyMove(battleState, monster, hero, monsterMove);

        if (battleState.getStatus() == BattleStatus.HERO_LOST) {
            runService.failRun(runId);
            activeBattles.remove(runId);
        }

        return battleState;
    }

    public BattleState getActiveBattle(String runId){

        BattleState battleState = activeBattles.get(runId);

        if(battleState == null){
            throw new IllegalArgumentException("Argument battle not found for run: " + runId);
        }

        return battleState;
    }

    private Move findHeroMove(Hero hero, String moveId){

        return hero.getEquippedMoves().stream()
                .filter(move -> move.getId().equals(moveId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Move not equipped: " + moveId));
    }

    private void resetTemporaryEffects(Hero hero, Monster monster) {
        hero.getActiveEffects().clear();
        monster.getActiveEffects().clear();
    }
}

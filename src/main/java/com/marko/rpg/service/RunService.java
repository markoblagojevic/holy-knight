package com.marko.rpg.service;

import com.marko.rpg.domain.character.Hero;
import com.marko.rpg.domain.move.Move;
import com.marko.rpg.domain.run.Encounter;
import com.marko.rpg.domain.run.EncounterStatus;
import com.marko.rpg.domain.run.RunState;
import com.marko.rpg.domain.run.RunStatus;
import com.marko.rpg.factory.RunFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class RunService {

    private final RunFactory runFactory;
    private final Map<String, RunState> activeRuns = new HashMap<>();
    private final Random random = new Random();

    public RunService(RunFactory runFactory) {
        this.runFactory = runFactory;
    }

    public RunState startNewRun(){

        RunState runState = runFactory.createNewRun();

        activeRuns.put(runState.getRunId(), runState);

        return runState;

    }

    public RunState getRun(String runId){
        RunState runState = activeRuns.get(runId);

        if(runState == null){
            throw new IllegalStateException("Run not found: " + runId);
        }
        return runState;
    }

    public Encounter getCurrentEncounter(String runId){
        RunState runState = getRun(runId);

        return runState.getEncounters().get(runState.getCurrentEncounterIndex());
    }

    public Move completeCurrentEncounter(String runId){
        RunState runState = getRun(runId);

        Encounter currentEncounter = getCurrentEncounter(runId);

        currentEncounter.setStatus(EncounterStatus.COMPLETED);

        runState.getHero().getActiveEffects().clear();
        currentEncounter.getMonster().getActiveEffects().clear();

        awardExperience(runState.getHero(),currentEncounter.getMonster().getExperienceReward());

        restoreHeroAfterVictory(runState.getHero());

        Move learnedMove = learnRandomMonsterMove(runState.getHero(),currentEncounter.getMonster().getMoves());

        unlockNextEncounterOrCompleteRun(runState);

        return learnedMove;
    }

    private void restoreHeroAfterVictory(Hero hero) {
        int healingAmount = (int) (hero.getStats().getMaxHealth() * 0.3);

        int newHealth = Math.min(
                hero.getStats().getMaxHealth(),
                hero.getStats().getCurrentHealth() + healingAmount
        );

        hero.getStats().setCurrentHealth(newHealth);
    }

    public void failRun(String runId) {
        RunState runState = getRun(runId);
        runState.setStatus(RunStatus.FAILED);
    }

    public void awardExperience(Hero hero, int experienceReward){
        hero.setExperience(hero.getExperience() + experienceReward);

        while (hero.getExperience() >= hero.getExperienceToNextLevel()){
            hero.setExperience(hero.getExperience() - hero.getExperienceToNextLevel());
            levelUp(hero);
        }
    }
    public void levelUp(Hero hero){
        hero.setLevel(hero.getLevel() + 1);

        hero.getStats().setMaxHealth(hero.getStats().getMaxHealth() + 15);
        hero.getStats().setCurrentHealth(hero.getStats().getMaxHealth());

        hero.getStats().setAttack(hero.getStats().getAttack() + 3);
        hero.getStats().setDefense(hero.getStats().getDefense() + 2);
        hero.getStats().setMagic(hero.getStats().getMagic() + 2);

        hero.setExperienceToNextLevel(hero.getExperienceToNextLevel() + 50);
    }

    private Move learnRandomMonsterMove(Hero hero, List<Move>monsterMoves){

        Move learnedMove = monsterMoves.get(random.nextInt(monsterMoves.size()));

        boolean alreadyLearned = hero.getLearnedMoves()
                .stream()
                .anyMatch(move -> move.getId().equals(learnedMove.getId()));

        if(!alreadyLearned){
            hero.getLearnedMoves().add(learnedMove);
        }

        return learnedMove;
    }

    private void unlockNextEncounterOrCompleteRun(RunState runState){

        int nextIndex = runState.getCurrentEncounterIndex() + 1;

        if (nextIndex >= runState.getEncounters().size()) {
            runState.setStatus(RunStatus.COMPLETED);
            return;
        }

        runState.setCurrentEncounterIndex(nextIndex);

        Encounter nextEncounter = runState.getEncounters().get(nextIndex);
        nextEncounter.setStatus(EncounterStatus.AVAILABLE);

    }

    public Hero equipMoves(String runId, List<String> moveIds){
        RunState runState = getRun(runId);
        Hero hero = runState.getHero();

        if (moveIds.size() > 4){
            throw new IllegalArgumentException("Hero can equip at most 4 moves.");
        }


        List<Move> selectedMoves = moveIds.stream()
                .map(moveId -> findLearnedMove(hero, moveId))
                .toList();


        hero.setEquippedMoves(selectedMoves);
        return hero;
    }

    private Move findLearnedMove(Hero hero, String moveId){
        return hero.getLearnedMoves()
                .stream()
                .filter(move->move.getId().equals(moveId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Move not learned:"+ moveId));
    }





}

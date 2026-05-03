package com.marko.rpg.mapper;

import com.marko.rpg.domain.battle.ActiveEffect;
import com.marko.rpg.domain.battle.BattleEvent;
import com.marko.rpg.domain.battle.BattleState;
import com.marko.rpg.domain.character.GameCharacter;
import com.marko.rpg.domain.character.Hero;
import com.marko.rpg.domain.move.Move;
import com.marko.rpg.domain.run.Encounter;
import com.marko.rpg.domain.run.RunState;
import com.marko.rpg.dto.*;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    public RunResponse toRunResponse(RunState runState) {
        return new RunResponse(
                runState.getRunId(),
                toHeroResponse(runState.getHero()),
                runState.getEncounters()
                        .stream()
                        .map(this::toEncounterResponse)
                        .toList(),
                runState.getCurrentEncounterIndex(),
                runState.getStatus()
        );
    }

    public BattleResponse toBattleResponse(BattleState battleState) {
        return new BattleResponse(
                toHeroResponse(battleState.getHero()),
                toCharacterResponse(battleState.getMonster()),
                battleState.getStatus(),
                battleState.getEvent()
                        .stream()
                        .map(this::toBattleEventResponse)
                        .toList(),
                battleState.getLearnedMove() == null
                        ? null
                        : toMoveResponse(battleState.getLearnedMove())
        );
    }

    public HeroResponse toHeroResponse(Hero hero) {
        return new HeroResponse(
                hero.getId(),
                hero.getName(),
                toStatsResponse(hero.getStats()),
                hero.getActiveEffects()
                        .stream()
                        .map(this::toActiveEffectResponse)
                        .toList(),
                hero.getLevel(),
                hero.getExperience(),
                hero.getExperienceToNextLevel(),
                hero.getLearnedMoves()
                        .stream()
                        .map(this::toMoveResponse)
                        .toList(),
                hero.getEquippedMoves()
                        .stream()
                        .map(this::toMoveResponse)
                        .toList()
        );
    }

    private EncounterResponse toEncounterResponse(Encounter encounter) {
        return new EncounterResponse(
                encounter.getOrder(),
                toCharacterResponse(encounter.getMonster()),
                encounter.getStatus()
        );
    }

    private CharacterResponse toCharacterResponse(GameCharacter character) {
        return new CharacterResponse(
                character.getId(),
                character.getName(),
                toStatsResponse(character.getStats()),
                character.getMoves()
                        .stream()
                        .map(this::toMoveResponse)
                        .toList(),
                character.getActiveEffects()
                        .stream()
                        .map(this::toActiveEffectResponse)
                        .toList()
        );
    }

    private StatsResponse toStatsResponse(com.marko.rpg.domain.character.Stats stats) {
        return new StatsResponse(
                stats.getMaxHealth(),
                stats.getCurrentHealth(),
                stats.getAttack(),
                stats.getDefense(),
                stats.getMagic()
        );
    }

    private MoveResponse toMoveResponse(Move move) {
        return new MoveResponse(
                move.getId(),
                move.getName(),
                move.getDescription(),
                move.getType(),
                move.getEffectType(),
                move.getBaseValue(),
                move.getDuration()
        );
    }

    private ActiveEffectResponse toActiveEffectResponse(ActiveEffect effect) {
        return new ActiveEffectResponse(
                effect.getEffectType(),
                effect.getValue(),
                effect.getRemainingTurns()
        );
    }

    private BattleEventResponse toBattleEventResponse(BattleEvent event) {
        return new BattleEventResponse(
                event.getType(),
                event.getMessage()
        );
    }
}

package com.marko.rpg.factory;

import com.marko.rpg.config.*;
import com.marko.rpg.domain.character.Hero;
import com.marko.rpg.domain.character.Monster;
import com.marko.rpg.domain.character.Stats;
import com.marko.rpg.domain.move.EffectType;
import com.marko.rpg.domain.move.Move;
import com.marko.rpg.domain.move.MoveType;
import com.marko.rpg.domain.run.Encounter;
import com.marko.rpg.domain.run.EncounterStatus;
import com.marko.rpg.domain.run.RunState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RunFactory {

    private final GameConfigLoader gameConfigLoader;

    public RunFactory(GameConfigLoader gameConfigLoader) {
        this.gameConfigLoader = gameConfigLoader;
    }

    public RunState createNewRun() {
        GameConfig gameConfig = gameConfigLoader.getGameConfig();

        Hero hero = createHeroFromConfig(gameConfig.getHero());

        List<Encounter> encounters = createEncountersFromConfig(gameConfig.getMonsters());

        RunState runState = new RunState();
        runState.setHero(hero);
        runState.setEncounters(encounters);
        runState.setCurrentEncounterIndex(0);

        return runState;
    }

    private Hero createHeroFromConfig(HeroConfig heroConfig) {
        Hero hero = new Hero();

        hero.setId(heroConfig.getId());
        hero.setName(heroConfig.getName());
        hero.setStats(createStatsFromConfig(heroConfig.getStats()));

        List<Move> defaultMoves = heroConfig.getMoves()
                .stream()
                .map(this::createMoveFromConfig)
                .toList();

        hero.setMoves(new ArrayList<>(defaultMoves));
        hero.setLearnedMoves(new ArrayList<>(defaultMoves));
        hero.setEquippedMoves(new ArrayList<>(defaultMoves));

        return hero;
    }

    private List<Encounter> createEncountersFromConfig(List<MonsterConfig> monsterConfigs) {
        List<Encounter> encounters = new ArrayList<>();

        for (int i = 0; i < monsterConfigs.size(); i++) {
            Monster monster = createMonsterFromConfig(monsterConfigs.get(i));

            EncounterStatus status = i == 0
                    ? EncounterStatus.AVAILABLE
                    : EncounterStatus.LOCKED;

            encounters.add(new Encounter(i + 1, monster, status));
        }

        return encounters;
    }

    private Monster createMonsterFromConfig(MonsterConfig monsterConfig) {
        Monster monster = new Monster();

        monster.setId(monsterConfig.getId());
        monster.setName(monsterConfig.getName());
        monster.setStats(createStatsFromConfig(monsterConfig.getStats()));
        monster.setExperienceReward(monsterConfig.getExperienceReward());
        monster.setAiStrategy(monsterConfig.getAiStrategy());

        List<Move> moves = monsterConfig.getMoves()
                .stream()
                .map(this::createMoveFromConfig)
                .toList();

        monster.setMoves(new ArrayList<>(moves));

        return monster;
    }

    private Stats createStatsFromConfig(StatsConfig statsConfig) {
        return new Stats(
                statsConfig.getMaxHealth(),
                statsConfig.getCurrentHealth(),
                statsConfig.getAttack(),
                statsConfig.getDefense(),
                statsConfig.getMagic()
        );
    }

    private Move createMoveFromConfig(MoveConfig moveConfig) {
        return new Move(
                moveConfig.getId(),
                moveConfig.getName(),
                moveConfig.getDescription(),
                moveConfig.getType(),
                moveConfig.getEffectType(),
                moveConfig.getBaseValue(),
                moveConfig.getDuration()
        );
    }

}

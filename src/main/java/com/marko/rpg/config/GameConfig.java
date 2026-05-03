package com.marko.rpg.config;

import lombok.Data;

import java.util.List;

@Data
public class GameConfig {

    private HeroConfig hero;
    private List<MonsterConfig> monsters;
}

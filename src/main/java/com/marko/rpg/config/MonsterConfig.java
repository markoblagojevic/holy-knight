package com.marko.rpg.config;

import lombok.Data;

import java.util.List;

@Data
public class MonsterConfig {

    private String id;
    private String name;

    private StatsConfig stats;

    private int experienceReward;
    private String aiStrategy;

    private List<MoveConfig> moves;
}

package com.marko.rpg.config;

import lombok.Data;

import java.util.List;

@Data
public class HeroConfig {

    private String id;
    private String name;

    private StatsConfig stats;

    private List<MoveConfig> moves;
}

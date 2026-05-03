package com.marko.rpg.domain.character;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Monster extends GameCharacter {

    private int experienceReward;
    private String aiStrategy;


}

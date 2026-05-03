package com.marko.rpg.domain.character;

import com.marko.rpg.domain.move.Move;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class Hero extends GameCharacter {

    private int level=1;
    private int experience = 0;
    private int experienceToNextLevel = 100;

    private List<Move> learnedMoves = new ArrayList<>();
    private List<Move> equippedMoves = new ArrayList<>();
}

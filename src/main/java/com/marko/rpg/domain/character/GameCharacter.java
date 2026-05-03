package com.marko.rpg.domain.character;

import com.marko.rpg.domain.battle.ActiveEffect;
import com.marko.rpg.domain.move.Move;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameCharacter {

    private String id;
    private String name;
    private Stats stats;

    private List<Move> moves= new ArrayList<>();

    private List<ActiveEffect> activeEffects= new ArrayList<>();


}

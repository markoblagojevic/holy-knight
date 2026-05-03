package com.marko.rpg.domain.run;


import com.marko.rpg.domain.character.Monster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Encounter {

    private int order;
    private Monster monster;
    private EncounterStatus status;

}

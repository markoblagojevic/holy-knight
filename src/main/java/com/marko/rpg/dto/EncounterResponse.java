package com.marko.rpg.dto;

import com.marko.rpg.domain.run.EncounterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EncounterResponse {

    private int order;
    private CharacterResponse monster;
    private EncounterStatus status;
}

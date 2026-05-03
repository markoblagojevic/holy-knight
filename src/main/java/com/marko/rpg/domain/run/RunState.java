package com.marko.rpg.domain.run;


import com.marko.rpg.domain.character.Hero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RunState {

    private String runId= UUID.randomUUID().toString();

    private Hero hero;

    private List<Encounter> encounters = new ArrayList<>();

    private int currentEncounterIndex = 0;

    private RunStatus status = RunStatus.IN_PROGRESS;

}

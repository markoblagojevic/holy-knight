package com.marko.rpg.dto;

import com.marko.rpg.domain.run.RunStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RunResponse {

    private String runId;
    private HeroResponse hero;
    private List<EncounterResponse> encounters;
    private int currentEncounterIndex;
    private RunStatus status;
}

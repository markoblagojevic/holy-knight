package com.marko.rpg.controller;

import com.marko.rpg.domain.character.Hero;
import com.marko.rpg.domain.run.RunState;
import com.marko.rpg.dto.EquipMovesRequest;
import com.marko.rpg.dto.HeroResponse;
import com.marko.rpg.dto.RunResponse;
import com.marko.rpg.mapper.GameMapper;
import com.marko.rpg.service.RunService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/runs")
public class RunController {

    private final RunService runService;
    private final GameMapper gameMapper;


    public RunController(RunService runService, GameMapper gameMapper) {
        this.runService = runService;
        this.gameMapper = gameMapper;
    }

    @PostMapping
    public RunResponse startRun() {
        return gameMapper.toRunResponse(runService.startNewRun());
    }

    @GetMapping("/{runId}")
    public RunResponse getRun(@PathVariable String runId) {
        return gameMapper.toRunResponse(runService.getRun(runId));
    }

    @PutMapping("/{runId}/equip-moves")
    public HeroResponse equipMoves(
            @PathVariable String runId,
            @RequestBody EquipMovesRequest request
    ) {
        return gameMapper.toHeroResponse(
                runService.equipMoves(runId, request.getMoveIds())
        );
    }

}

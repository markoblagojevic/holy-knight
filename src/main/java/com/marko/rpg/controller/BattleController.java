package com.marko.rpg.controller;

import com.marko.rpg.domain.battle.BattleState;
import com.marko.rpg.dto.BattleResponse;
import com.marko.rpg.mapper.GameMapper;
import com.marko.rpg.service.BattleService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/battles")
public class BattleController {

    private final BattleService battleService;
    private final GameMapper gameMapper;

    public BattleController(BattleService battleService, GameMapper gameMapper) {
        this.battleService = battleService;
        this.gameMapper = gameMapper;
    }

    @PostMapping("/{runId}/start")
    public BattleResponse startBattle(@PathVariable String runId) {
        return gameMapper.toBattleResponse(battleService.startBattle(runId));
    }

    @PostMapping("/{runId}/turn")
    public BattleResponse playTurn(
            @PathVariable String runId,
            @RequestParam String moveId
    ) {
        return gameMapper.toBattleResponse(battleService.playTurn(runId, moveId));
    }

    @GetMapping("/{runId}")
    public BattleResponse getBattle(@PathVariable String runId) {
        return gameMapper.toBattleResponse(battleService.getActiveBattle(runId));
    }

}

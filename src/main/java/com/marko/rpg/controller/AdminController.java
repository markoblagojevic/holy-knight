package com.marko.rpg.controller;

import com.marko.rpg.config.GameConfigLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final GameConfigLoader gameConfigLoader;

    public AdminController(GameConfigLoader gameConfigLoader) {
        this.gameConfigLoader = gameConfigLoader;
    }

    @PostMapping("/reload-config")
    public String reloadConfig() {
        gameConfigLoader.reloadConfig();
        return "Game config reloaded successfully.";
    }
}

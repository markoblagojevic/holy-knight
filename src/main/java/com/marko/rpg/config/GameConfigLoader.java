package com.marko.rpg.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class GameConfigLoader {

    private final ObjectMapper objectMapper;
    private GameConfig gameConfig;

    public GameConfigLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.gameConfig = loadConfig();
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public void reloadConfig() {
        this.gameConfig = loadConfig();
    }

    private GameConfig loadConfig() {
        try {
            InputStream inputStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("game-config.json");

            if (inputStream == null) {
                throw new IllegalStateException("game-config.json not found");
            }

            return objectMapper.readValue(inputStream, GameConfig.class);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to load game config", e);
        }
    }
}

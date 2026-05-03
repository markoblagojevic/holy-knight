package com.marko.rpg.dto;

import lombok.Data;

import java.util.List;

@Data
public class EquipMovesRequest {

    private List<String> moveIds;
}

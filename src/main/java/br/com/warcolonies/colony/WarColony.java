package br.com.warcolonies.colony;

import java.util.Map;
import java.util.UUID;
import net.minecraft.world.item.Item;

public class WarColony {
    private UUID owner;
    private Object position; // chunk/blockpos
    private int level;
    private Map<Item, Integer> stock;
    private Map<Item, Integer> productionRates;
    private int militaryStrength;
    // TODO: Getters/Setters
}

package br.com.warcolonies.colony;

import java.util.HashMap;
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

    public WarColony(UUID owner, Object position, int level) {
        this.owner = owner;
        this.position = position;
        this.level = level;
        this.stock = new HashMap<>();
        this.productionRates = new HashMap<>();
        this.militaryStrength = 0;
    }

    // Getters
    public UUID getOwner() {
        return owner;
    }

    public Object getPosition() {
        return position;
    }

    public int getLevel() {
        return level;
    }

    public Map<Item, Integer> getStock() {
        return stock;
    }

    public Map<Item, Integer> getProductionRates() {
        return productionRates;
    }

    public int getMilitaryStrength() {
        return militaryStrength;
    }

    // Setters
    public void setLevel(int level) {
        this.level = level;
    }

    public void setMilitaryStrength(int militaryStrength) {
        this.militaryStrength = militaryStrength;
    }

    public void addToStock(Item item, int amount) {
        stock.put(item, stock.getOrDefault(item, 0) + amount);
    }
}

package br.com.warcolonies.colony;

import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Representa uma colônia abstrata usada pelo War Colonies.
 * Ela NÃO substitui a colônia do MineColonies, é um "espelho estratégico".
 */
public class WarColony {

    // ---- Dados básicos ----
    private UUID owner;         // dono (pode ser null por enquanto)
    private Object position;    // posição aproximada (chunk/blockpos futuramente)
    private int level;

    // Estoques por Item (pensando no futuro)
    private Map<Item, Integer> stock;
    private Map<Item, Integer> productionRates;
    private int militaryStrength;

    // ---- Economia abstrata simples (para debug) ----
    private String name;
    private int productionRate; // quanto produz por tick estratégico
    private Map<String, Integer> resourceStock; // ex: "food" -> quantidade

    public WarColony(UUID owner, Object position, int level) {
        this.owner = owner;
        this.position = position;
        this.level = level;

        this.stock = new HashMap<>();
        this.productionRates = new HashMap<>();
        this.militaryStrength = 0;

        this.name = "Colônia";
        this.productionRate = 1;
        this.resourceStock = new HashMap<>();
    }

    // ---- Getters básicos ----
    public UUID getOwner() {
        return owner;
    }

    public Object getPosition() {
        return position;
    }

    public int getLevel() {
        return level;
    }

    public int getMilitaryStrength() {
        return militaryStrength;
    }

    public Map<Item, Integer> getStock() {
        return stock;
    }

    public Map<Item, Integer> getProductionRates() {
        return productionRates;
    }

    // ---- Setters básicos ----
    public void setLevel(int level) {
        this.level = level;
    }

    public void setMilitaryStrength(int militaryStrength) {
        this.militaryStrength = militaryStrength;
    }

    public void addToStock(Item item, int amount) {
        stock.put(item, stock.getOrDefault(item, 0) + amount);
    }

    // ---- Parte nova: economia simples para o MVP ----

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Produção fixa por tick estratégico (ex: comida).
     */
    public int getProductionRate() {
        return productionRate;
    }

    public void setProductionRate(int productionRate) {
        this.productionRate = productionRate;
    }

    /**
     * Adiciona recurso abstrato, ex: "food".
     */
    public void addResource(String key, int amount) {
        resourceStock.put(key, resourceStock.getOrDefault(key, 0) + amount);
    }

    /**
     * Lê recurso abstrato (se não existir, retorna 0).
     */
    public int getResource(String key) {
        return resourceStock.getOrDefault(key, 0);
    }

    /**
     * Retorna o mapa de recursos abstratos (cópia defensiva).
     */
    public Map<String, Integer> getResourceStock() {
        return new HashMap<>(resourceStock);
    }
}

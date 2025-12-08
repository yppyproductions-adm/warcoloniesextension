package br.com.warcolonies.core;

public class WarColoniesMod {
    // Managers centrais do mod (singletons simples)
    private static final br.com.warcolonies.colony.WarColonyManager COLONY_MANAGER = new br.com.warcolonies.colony.WarColonyManager();
    private static final br.com.warcolonies.logistics.LogisticsManager LOGISTICS_MANAGER = new br.com.warcolonies.logistics.LogisticsManager();
    private static final br.com.warcolonies.territory.ConquestManager CONQUEST_MANAGER = new br.com.warcolonies.territory.ConquestManager();
    private static final br.com.warcolonies.building.BuildingSimulationManager BUILDING_MANAGER = new br.com.warcolonies.building.BuildingSimulationManager();
    private static final br.com.warcolonies.data.WarEconomyManager ECONOMY_MANAGER = new br.com.warcolonies.data.WarEconomyManager();
    private static final br.com.warcolonies.warfare.WarfareManager WARFARE_MANAGER = new br.com.warcolonies.warfare.WarfareManager();
    private static final br.com.warcolonies.colony.DiplomacyManager DIPLOMACY_MANAGER = new br.com.warcolonies.colony.DiplomacyManager();
    private static final br.com.warcolonies.territory.TerritoryManager TERRITORY_MANAGER = new br.com.warcolonies.territory.TerritoryManager();

    private WarColoniesMod() {
        // utilitário estático
    }

    public static void init() {
        // ponto único para inicializar dependências futuras se necessário
    }

    // Getters globais (mantém coesão e facilita injeção)
    public static br.com.warcolonies.colony.WarColonyManager getColonyManager() {
        return COLONY_MANAGER;
    }

    public static br.com.warcolonies.logistics.LogisticsManager getLogisticsManager() {
        return LOGISTICS_MANAGER;
    }

    public static br.com.warcolonies.territory.ConquestManager getConquestManager() {
        return CONQUEST_MANAGER;
    }

    public static br.com.warcolonies.building.BuildingSimulationManager getBuildingManager() {
        return BUILDING_MANAGER;
    }

    public static br.com.warcolonies.data.WarEconomyManager getEconomyManager() {
        return ECONOMY_MANAGER;
    }

    public static br.com.warcolonies.warfare.WarfareManager getWarfareManager() {
        return WARFARE_MANAGER;
    }

    public static br.com.warcolonies.colony.DiplomacyManager getDiplomacyManager() {
        return DIPLOMACY_MANAGER;
    }

    public static br.com.warcolonies.territory.TerritoryManager getTerritoryManager() {
        return TERRITORY_MANAGER;
    }
}

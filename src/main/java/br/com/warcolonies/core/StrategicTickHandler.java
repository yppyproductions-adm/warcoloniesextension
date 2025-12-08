package br.com.warcolonies.core;

import br.com.warcolonies.building.BuildingSimulationManager;
import br.com.warcolonies.data.WarEconomyManager;
import br.com.warcolonies.logistics.LogisticsManager;
import br.com.warcolonies.territory.ConquestManager;
import net.neoforged.neoforge.event.tick.TickEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class StrategicTickHandler {
    private final WarEconomyManager economyManager;
    private final LogisticsManager logisticsManager;
    private final BuildingSimulationManager buildingManager;
    private final ConquestManager conquestManager;

    public StrategicTickHandler(WarEconomyManager economyManager,
                                LogisticsManager logisticsManager,
                                BuildingSimulationManager buildingManager,
                                ConquestManager conquestManager) {
        this.economyManager = economyManager;
        this.logisticsManager = logisticsManager;
        this.buildingManager = buildingManager;
        this.conquestManager = conquestManager;
    }

    // Tick estratégico básico para manter o mundo “vivo”
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        economyManager.processTick();
        logisticsManager.updateShipments();
        buildingManager.updateTasks();
        conquestManager.updateClaims();
    }
}

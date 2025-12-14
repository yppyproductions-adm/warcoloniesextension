package br.com.warcolonies.core;

import br.com.warcolonies.data.WarEconomyManager;
import br.com.warcolonies.logistics.LogisticsManager;
import br.com.warcolonies.territory.ConquestManager;
import br.com.warcolonies.building.BuildingSimulationManager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * Tick estratégico do War Colonies.
 * Roda no servidor a cada X ticks (aqui: 20 = 1 segundo).
 */
public class StrategicTickHandler {

    private final WarEconomyManager economy;
    private final LogisticsManager logistics;
    private final BuildingSimulationManager building;
    private final ConquestManager conquest;

    // Conta ticks do servidor
    private int tickCounter = 0;

    public StrategicTickHandler(
            WarEconomyManager economy,
            LogisticsManager logistics,
            BuildingSimulationManager building,
            ConquestManager conquest
    ) {
        this.economy = economy;
        this.logistics = logistics;
        this.building = building;
        this.conquest = conquest;
    }

    /**
     * Em NeoForge 1.21.x, registramos o listener em UMA subclasse concreta
     * do ServerTickEvent — aqui usamos o POST.
     */
    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        // Chamado uma vez por tick de servidor (fase Post)
        tickCounter++;

        // A cada 20 ticks (1 segundo), rodamos o tick estratégico
        if (tickCounter >= 20) {
            tickCounter = 0;

            economy.processTick();
            logistics.updateShipments();
            building.updateTasks();
            conquest.updateClaims();
        }
    }
}

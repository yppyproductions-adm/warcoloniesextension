package br.com.warcolonies.data;

import br.com.warcolonies.colony.WarColony;
import br.com.warcolonies.core.WarColoniesMod;

import java.util.List;

/**
 * Responsável pela parte econômica abstrata das colônias.
 * Agora:
 *  - conta quantos ticks estratégicos rodaram
 *  - gera "food" para cada colônia registrada no WarColonyManager
 */
public class WarEconomyManager {

    private long ticksProcessed = 0L;

    /**
     * Chamado a cada tick estratégico pelo StrategicTickHandler.
     */
    public void processTick() {
        ticksProcessed++;

        // Pega TODAS as colônias registradas no gerenciador central
        List<WarColony> colonies = WarColoniesMod.getColonyManager().getColonies();
        for (WarColony colony : colonies) {
            updateProduction(colony);
        }
    }

    /**
     * Produção simples: cada colônia ganha "food" = productionRate.
     */
    public void updateProduction(WarColony colony) {
        int produced = colony.getProductionRate();
        if (produced <= 0) {
            return;
        }
        colony.addResource("food", produced);
    }

    public long getTicksProcessed() {
        return ticksProcessed;
    }
}

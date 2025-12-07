package br.com.warcolonies.data;

import java.util.List;
import br.com.warcolonies.colony.WarColony;
import br.com.warcolonies.warfare.EnemyVillage;
import br.com.warcolonies.logistics.Shipment;
import br.com.warcolonies.building.VirtualBuildingTask;
import br.com.warcolonies.territory.ConquestClaim;

public class WarWorldData {
    private List<WarColony> colonies;
    private List<EnemyVillage> enemyVillages;
    private List<Shipment> shipments;
    private List<VirtualBuildingTask> buildingTasks;
    private List<ConquestClaim> claims;

    // TODO: Métodos para salvar/carregar usando SavedData
}

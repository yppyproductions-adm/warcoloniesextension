package br.com.warcolonies.data;

import java.util.ArrayList;
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

    public WarWorldData() {
        this.colonies = new ArrayList<>();
        this.enemyVillages = new ArrayList<>();
        this.shipments = new ArrayList<>();
        this.buildingTasks = new ArrayList<>();
        this.claims = new ArrayList<>();
    }

    // TODO: Métodos para salvar/carregar usando SavedData
    
    // Getters
    public List<WarColony> getColonies() {
        return new ArrayList<>(colonies);
    }

    public List<EnemyVillage> getEnemyVillages() {
        return new ArrayList<>(enemyVillages);
    }

    public List<Shipment> getShipments() {
        return new ArrayList<>(shipments);
    }

    public List<VirtualBuildingTask> getBuildingTasks() {
        return new ArrayList<>(buildingTasks);
    }

    public List<ConquestClaim> getClaims() {
        return new ArrayList<>(claims);
    }

    // Add methods
    public void addColony(WarColony colony) {
        colonies.add(colony);
    }

    public void addEnemyVillage(EnemyVillage village) {
        enemyVillages.add(village);
    }

    public void addShipment(Shipment shipment) {
        shipments.add(shipment);
    }

    public void addBuildingTask(VirtualBuildingTask task) {
        buildingTasks.add(task);
    }

    public void addClaim(ConquestClaim claim) {
        claims.add(claim);
    }
}

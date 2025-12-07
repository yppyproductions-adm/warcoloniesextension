package br.com.warcolonies.building;

import java.util.Map;
import br.com.warcolonies.colony.WarColony;

public class VirtualBuildingTask {
    private WarColony colony;
    private String building;
    private int totalBlocks;
    private int remainingBlocks;
    private float virtualSpeed;
    private Map<String, Integer> requiredResources;
    private Map<String, Integer> deliveredResources;

    public VirtualBuildingTask(WarColony colony, String building, int totalBlocks, float virtualSpeed, Map<String, Integer> requiredResources) {
        this.colony = colony;
        this.building = building;
        this.totalBlocks = totalBlocks;
        this.remainingBlocks = totalBlocks;
        this.virtualSpeed = virtualSpeed;
        this.requiredResources = requiredResources;
        this.deliveredResources = new java.util.HashMap<>();
    }

    // Getters
    public WarColony getColony() {
        return colony;
    }

    public String getBuilding() {
        return building;
    }

    public int getTotalBlocks() {
        return totalBlocks;
    }

    public int getRemainingBlocks() {
        return remainingBlocks;
    }

    public float getVirtualSpeed() {
        return virtualSpeed;
    }

    public Map<String, Integer> getRequiredResources() {
        return requiredResources;
    }

    public Map<String, Integer> getDeliveredResources() {
        return deliveredResources;
    }

    // Setters
    public void setRemainingBlocks(int remainingBlocks) {
        this.remainingBlocks = remainingBlocks;
    }

    public void addDeliveredResource(String resource, int amount) {
        deliveredResources.put(resource, deliveredResources.getOrDefault(resource, 0) + amount);
    }

    public boolean isComplete() {
        return remainingBlocks <= 0;
    }
}

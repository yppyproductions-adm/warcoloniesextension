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
}

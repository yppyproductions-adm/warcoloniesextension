package br.com.warcolonies.colony;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarColonyManager {
    private List<WarColony> colonies = new ArrayList<>();
    
    // TODO: gerenciar criação/registro/busca das colônias
    
    public void registerColony(WarColony colony) {
        colonies.add(colony);
    }
    
    public List<WarColony> getColoniesByOwner(UUID owner) {
        List<WarColony> result = new ArrayList<>();
        for (WarColony colony : colonies) {
            if (colony.getOwner().equals(owner)) {
                result.add(colony);
            }
        }
        return result;
    }
    
    public List<WarColony> getAllColonies() {
        return new ArrayList<>(colonies);
    }
}

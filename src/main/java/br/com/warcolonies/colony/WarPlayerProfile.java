package br.com.warcolonies.colony;

import java.util.ArrayList;
import java.util.List;

public class WarPlayerProfile {
    private List<Object> controlledColonies;
    private int reputation;
    private Object diplomacy;
    
    // TODO: adicionar mais campos e métodos para relações diplomáticas
    
    public WarPlayerProfile() {
        this.controlledColonies = new ArrayList<>();
        this.reputation = 0;
    }

    // Getters
    public List<Object> getControlledColonies() {
        return new ArrayList<>(controlledColonies);
    }

    public int getReputation() {
        return reputation;
    }

    public Object getDiplomacy() {
        return diplomacy;
    }

    // Setters
    public void addColony(Object colony) {
        controlledColonies.add(colony);
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public void setDiplomacy(Object diplomacy) {
        this.diplomacy = diplomacy;
    }
}

package br.com.warcolonies.warfare;

import java.util.ArrayList;
import java.util.List;

public class WarfareManager {
    private List<EnemyVillage> knownVillages = new ArrayList<>();
    
    // TODO: agendar invasões, resolver combates abstratos, atualizar forças
    
    public void addEnemyVillage(EnemyVillage village) {
        knownVillages.add(village);
    }
    
    public List<EnemyVillage> getKnownVillages() {
        return new ArrayList<>(knownVillages);
    }
}

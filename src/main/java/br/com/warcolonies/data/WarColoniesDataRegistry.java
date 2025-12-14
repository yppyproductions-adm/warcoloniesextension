package br.com.warcolonies.data;

import br.com.warcolonies.colony.WarColony;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registro simples em memória de colônias War Colonies.
 * Por enquanto, só mantém uma lista estática e uma colônia de teste.
 */
public final class WarColoniesDataRegistry {

    private static final List<WarColony> COLONIES = new ArrayList<>();

    private WarColoniesDataRegistry() {
        // utilitário estático
    }

    public static List<WarColony> getColonies() {
        return Collections.unmodifiableList(COLONIES);
    }

    public static void addColony(WarColony colony) {
        COLONIES.add(colony);
    }

    /**
     * Garante que existe pelo menos uma colônia de teste.
     * Retorna sempre a primeira.
     */
    public static WarColony ensureDemoColony() {
        if (COLONIES.isEmpty()) {
            WarColony colony = new WarColony(null, null, 1);
            colony.setName("Colônia de Teste");
            colony.setProductionRate(5); // produz 5 "food" por tick estratégico
            addColony(colony);
        }
        return COLONIES.get(0);
    }
}

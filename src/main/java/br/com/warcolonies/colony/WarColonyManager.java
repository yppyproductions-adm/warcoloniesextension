package br.com.warcolonies.colony;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Gerencia todas as WarColony do War Colonies.
 * Por enquanto:
 *  - mantém uma lista simples em memória
 *  - permite registrar colônias
 *  - permite listar colônias para debug
 */
public class WarColonyManager {

    private final List<WarColony> colonies = new ArrayList<>();

    public WarColonyManager() {
        // vazio mesmo
    }

    /**
     * Adiciona uma colônia no gerenciador.
     */
    public void addColony(WarColony colony) {
        if (colony != null && !colonies.contains(colony)) {
            colonies.add(colony);
        }
    }

    /**
     * Retorna uma cópia da lista de colônias para leitura.
     * Usado pelo comando /war debug colonies.
     */
    public List<WarColony> getColonies() {
        return new ArrayList<>(colonies);
    }

    /**
     * Helper opcional para criar colônia de teste em comandos futuros.
     */
    public WarColony createDebugColony(UUID owner) {
        WarColony colony = new WarColony(owner, null, 1);
        colonies.add(colony);
        return colony;
    }
}

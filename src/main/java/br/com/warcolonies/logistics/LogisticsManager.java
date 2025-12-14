package br.com.warcolonies.logistics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gerencia caravanas (Shipments) em nível abstrato.
 * Agora:
 *  - conta quantos ticks foram processados
 *  - avança progresso das caravanas IN_TRANSIT
 *  - remove as que chegaram ou foram perdidas
 */
public class LogisticsManager {

    private final List<Shipment> activeShipments = new ArrayList<>();
    private long ticksProcessed = 0L;

    // Criação simples de caravana
    public void createShipment(Object origin, Object destination, List<Object> cargo, EscortInfo escort) {
        Shipment shipment = new Shipment(origin, destination, cargo, escort);
        activeShipments.add(shipment);
    }

    /**
     * Chamado a cada tick estratégico.
     * Por enquanto:
     *  - incrementa contador
     *  - avança progresso dos Shipments em 5%
     *  - marca como ARRIVED quando chegar em 100%
     *  - remove ARRIVED e LOST da lista
     */
    public void updateShipments() {
        ticksProcessed++;

        for (Shipment s : activeShipments) {
            if (s.getState() == ShipmentState.IN_TRANSIT) {
                s.advanceProgress(5); // 5% por tick (1 segundo)
            }
        }

        activeShipments.removeIf(shipment ->
                shipment.getState() == ShipmentState.ARRIVED ||
                shipment.getState() == ShipmentState.LOST
        );
    }

    public List<Shipment> getActiveShipments() {
        return Collections.unmodifiableList(activeShipments);
    }

    public long getTicksProcessed() {
        return ticksProcessed;
    }
}

package br.com.warcolonies.logistics;

import java.util.List;

/**
 * Representa uma caravana abstrata (sem NPC) para o MVP.
 */
public class Shipment {

    private final Object origin;
    private final Object destination;
    private final List<Object> cargo;
    private final EscortInfo escort;

    private ShipmentState state;
    private int progress; // 0–100

    public Shipment(Object origin, Object destination, List<Object> cargo, EscortInfo escort) {
        this.origin = origin;
        this.destination = destination;
        this.cargo = cargo;
        this.escort = escort;
        this.state = ShipmentState.IN_TRANSIT;
        this.progress = 0;
    }

    public Object getOrigin() {
        return origin;
    }

    public Object getDestination() {
        return destination;
    }

    public List<Object> getCargo() {
        return cargo;
    }

    public EscortInfo getEscort() {
        return escort;
    }

    public ShipmentState getState() {
        return state;
    }

    public int getProgress() {
        return progress;
    }

    /**
     * Avança o progresso em "amount" (ex.: 5% por tick).
     * Quando chegar em 100, marca como ARRIVED.
     */
    public void advanceProgress(int amount) {
        if (state != ShipmentState.IN_TRANSIT) {
            return;
        }

        progress += amount;
        if (progress >= 100) {
            progress = 100;
            arrive();
        }
    }

    public void arrive() {
        this.state = ShipmentState.ARRIVED;
    }

    public void markLost() {
        this.state = ShipmentState.LOST;
    }
}

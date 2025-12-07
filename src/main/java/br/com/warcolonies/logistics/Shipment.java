package br.com.warcolonies.logistics;

import java.util.List;

public class Shipment {
    private Object origin;
    private Object destination;
    private List<Object> cargo;
    private EscortInfo escort;
    private ShipmentState state;
    private long departure;
    private long arrival;
    private float routePosition;

    public Shipment(Object origin, Object destination, List<Object> cargo, EscortInfo escort) {
        this.origin = origin;
        this.destination = destination;
        this.cargo = cargo;
        this.escort = escort;
        this.state = ShipmentState.PENDING;
        this.routePosition = 0.0f;
    }

    // Getters
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

    public long getDeparture() {
        return departure;
    }

    public long getArrival() {
        return arrival;
    }

    public float getRoutePosition() {
        return routePosition;
    }

    // Setters
    public void setState(ShipmentState state) {
        this.state = state;
    }

    public void setDeparture(long departure) {
        this.departure = departure;
    }

    public void setArrival(long arrival) {
        this.arrival = arrival;
    }

    public void setRoutePosition(float routePosition) {
        this.routePosition = routePosition;
    }
}

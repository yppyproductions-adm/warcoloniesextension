package br.com.warcolonies.logistics;

import java.util.ArrayList;
import java.util.List;

public class LogisticsManager {
    private List<Shipment> activeShipments = new ArrayList<>();
    
    // TODO: criação de Shipments, viagem, checks de emboscada, chegada virtual
    
    public void createShipment(Object origin, Object destination, List<Object> cargo, EscortInfo escort) {
        Shipment shipment = new Shipment(origin, destination, cargo, escort);
        activeShipments.add(shipment);
    }
    
    public void updateShipments() {
        activeShipments.removeIf(shipment -> 
            shipment.getState() == ShipmentState.ARRIVED || 
            shipment.getState() == ShipmentState.LOST
        );
    }
    
    public List<Shipment> getActiveShipments() {
        return new ArrayList<>(activeShipments);
    }
}

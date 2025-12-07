package br.com.warcolonies.logistics;

import java.util.List;
import br.com.warcolonies.logistics.EscortInfo;

public class Shipment {
    private Object origin;
    private Object destination;
    private List<Object> cargo;
    private EscortInfo escort;
    private ShipmentState state;
    private long departure;
    private long arrival;
    private float routePosition;
    // enum ShipmentState { PENDING, IN_TRANSIT, ARRIVED, LOST, AMBUSHED }
}

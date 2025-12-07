package br.com.warcolonies.territory;

import br.com.warcolonies.colony.WarColony;

public class ConquestClaim {
    private WarColony targetColony;
    private Object attacker;
    private Object originalOwner;
    private long startTime;
    private long duration;
    private ClaimStatus status;
    // enum ClaimStatus { ACTIVE, COMPLETED, CANCELLED }
}

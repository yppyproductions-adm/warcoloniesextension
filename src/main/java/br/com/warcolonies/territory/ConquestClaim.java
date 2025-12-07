package br.com.warcolonies.territory;

import br.com.warcolonies.colony.WarColony;

public class ConquestClaim {
    private WarColony targetColony;
    private Object attacker;
    private Object originalOwner;
    private long startTime;
    private long duration;
    private ClaimStatus status;

    public ConquestClaim(WarColony targetColony, Object attacker, Object originalOwner, long duration) {
        this.targetColony = targetColony;
        this.attacker = attacker;
        this.originalOwner = originalOwner;
        this.startTime = System.currentTimeMillis();
        this.duration = duration;
        this.status = ClaimStatus.ACTIVE;
    }

    // Getters
    public WarColony getTargetColony() {
        return targetColony;
    }

    public Object getAttacker() {
        return attacker;
    }

    public Object getOriginalOwner() {
        return originalOwner;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getDuration() {
        return duration;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    // Setters
    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - startTime > duration;
    }
}

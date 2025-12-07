package br.com.warcolonies.logistics;

public class EscortInfo {
    private int combatPower;
    private int units;
    private String troopProfile; // arqueiro, infantaria, lanceiro, cavaleiro

    public EscortInfo(int combatPower, int units, String troopProfile) {
        this.combatPower = combatPower;
        this.units = units;
        this.troopProfile = troopProfile;
    }

    // Getters
    public int getCombatPower() {
        return combatPower;
    }

    public int getUnits() {
        return units;
    }

    public String getTroopProfile() {
        return troopProfile;
    }

    // Setters
    public void setCombatPower(int combatPower) {
        this.combatPower = combatPower;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public void setTroopProfile(String troopProfile) {
        this.troopProfile = troopProfile;
    }
}

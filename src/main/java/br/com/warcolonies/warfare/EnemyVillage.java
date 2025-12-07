package br.com.warcolonies.warfare;

public class EnemyVillage {
    private Object position;
    private int hostility;
    private int militaryStrength;
    private Object resources; // recursos internos

    public EnemyVillage(Object position, int hostility, int militaryStrength, Object resources) {
        this.position = position;
        this.hostility = hostility;
        this.militaryStrength = militaryStrength;
        this.resources = resources;
    }

    // Getters
    public Object getPosition() {
        return position;
    }

    public int getHostility() {
        return hostility;
    }

    public int getMilitaryStrength() {
        return militaryStrength;
    }

    public Object getResources() {
        return resources;
    }

    // Setters
    public void setPosition(Object position) {
        this.position = position;
    }

    public void setHostility(int hostility) {
        this.hostility = hostility;
    }

    public void setMilitaryStrength(int militaryStrength) {
        this.militaryStrength = militaryStrength;
    }

    public void setResources(Object resources) {
        this.resources = resources;
    }
}

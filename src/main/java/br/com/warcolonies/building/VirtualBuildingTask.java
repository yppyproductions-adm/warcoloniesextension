package br.com.warcolonies.building;

/**
 * Representa uma obra simulada em "modo virtual".
 * Ela não mexe em blocos de verdade ainda, é só para debug do sistema.
 */
public class VirtualBuildingTask {

    private final String description;
    private final int totalTicksNeeded;
    private int ticksRemaining;

    public VirtualBuildingTask(String description, int totalTicksNeeded) {
        this.description = description;
        this.totalTicksNeeded = totalTicksNeeded;
        this.ticksRemaining = totalTicksNeeded;
    }

    public String getDescription() {
        return description;
    }

    public int getTotalTicksNeeded() {
        return totalTicksNeeded;
    }

    public int getTicksRemaining() {
        return ticksRemaining;
    }

    /**
     * Avança 1 "tick estratégico" na tarefa.
     */
    public void tick() {
        if (ticksRemaining > 0) {
            ticksRemaining--;
        }
    }

    /**
     * A tarefa é considerada completa quando ticksRemaining chega a 0.
     */
    public boolean isComplete() {
        return ticksRemaining <= 0;
    }
}

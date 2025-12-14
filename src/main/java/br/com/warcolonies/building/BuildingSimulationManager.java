package br.com.warcolonies.building;

import java.util.ArrayList;
import java.util.List;

/**
 * Simula obras em "modo virtual" (quando o player não está perto).
 * Agora:
 *  - conta quantos ticks já rodaram
 *  - avança o progresso das VirtualBuildingTask
 *  - remove tarefas completas
 */
public class BuildingSimulationManager {

    private final List<VirtualBuildingTask> activeTasks = new ArrayList<>();
    private long ticksProcessed = 0L;

    /**
     * Adiciona uma nova obra virtual à lista.
     */
    public void addTask(VirtualBuildingTask task) {
        if (task != null) {
            activeTasks.add(task);
        }
    }

    /**
     * Chamado a cada tick estratégico.
     * Por enquanto:
     *  - incrementa contador
     *  - chama tick() em cada tarefa
     *  - remove tarefas marcadas como completas
     */
    public void updateTasks() {
        ticksProcessed++;

        for (VirtualBuildingTask task : activeTasks) {
            task.tick();
        }

        activeTasks.removeIf(VirtualBuildingTask::isComplete);
        // Depois: aqui vamos ligar isso a prédios reais do MineColonies.
    }

    public List<VirtualBuildingTask> getActiveTasks() {
        return new ArrayList<>(activeTasks);
    }

    public long getTicksProcessed() {
        return ticksProcessed;
    }
}

package br.com.warcolonies.building;

import java.util.ArrayList;
import java.util.List;

public class BuildingSimulationManager {
    private List<VirtualBuildingTask> activeTasks = new ArrayList<>();
    
    // TODO: avançar obras virtualmente quando chunk não carregado
    
    public void addTask(VirtualBuildingTask task) {
        activeTasks.add(task);
    }
    
    public void updateTasks() {
        activeTasks.removeIf(VirtualBuildingTask::isComplete);
        // TODO: avançar progresso das tarefas virtualmente
    }
    
    public List<VirtualBuildingTask> getActiveTasks() {
        return new ArrayList<>(activeTasks);
    }
}

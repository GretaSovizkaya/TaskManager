import java.util.*;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private static int newId = 1;
    public List<Task> getAllTypesTasks() {
        return new ArrayList<>(tasks.values());
    }


    public void deleteAllTasks(Task task) {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(Integer id) {
        return tasks.get(id);
    }

    public void createNewTask(Task task) {
        int newId = generateNewId();
        task.setId(newId);
        tasks.put(newId, task);
        if (task instanceof Epic) {
            epics.put(newId, (Epic) task);
        }

    }

    private Integer generateNewId() {
        // Implement generating new unique id

        return newId++;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
        }

    }

    public void removeTaskById(Integer id) {
        tasks.remove(id);
        if (epics.containsKey(id)){
            epics.remove(id);
        }
    }

    public List<Subtask> getSubtasksOfEpic(Integer epicId) {
        Task epic = epics.get(epicId);
        if (epic != null) {
            return new ArrayList<>(((Epic) epic).getSubtasks());
        }
        return new ArrayList<>();
    }

    public void addSubtaskToEpic(Subtask subtask, Epic epic) {
        subtasks.put(subtask.getId(), subtask);
        epic.getSubtasks().add(subtask);
        updateEpicStatus(epic);
    }

    public void updateEpicStatus(Epic epic) {
        Status newStatus = Status.NEW;
        for (Subtask subtask : epic.getSubtasks())
            if (subtask.getStatus().compareTo(newStatus) > 0) {
                newStatus = subtask.getStatus();
            }
        epic.setStatus(newStatus);
    }

    public void createNewEpic(Epic epic) {
        int newId = generateNewId();
        epic.setId(newId);
        tasks.put(newId, epic);
        epics.put(newId, epic);
    }

}
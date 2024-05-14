package managers;

import basis.*;
import org.w3c.dom.Node;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();//пустую строку удалила
    private static int newId = 1;

    //использую Managers
    public InMemoryTaskManager() { // удалила конструктор и исправила
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public List<Task> getAllTypesTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks(Task task) {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public void createNewTask(Task task) {
        int newId = generateNewId();
        task.setId(newId);
        tasks.put(newId, task);
        if (task instanceof Epic) {
            epics.put(newId, (Epic) task);
        }
    }

    private Integer generateNewId() {
        return newId++;
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
        }
    }

    @Override
    public void removeTaskById(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
            epics.remove(id);
        }
    }

    @Override
    public void removeSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = subtask.getEpic();
            if (epic != null) {
                epic.getSubtasks().remove(subtask);
            }
            subtasks.remove(id);
        }
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(Integer epicId) {
        Task epic = epics.get(epicId);
        if (epic != null) {
            return new ArrayList<>(((Epic) epic).getSubtasks());
        }
        return new ArrayList<>();
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        Status newStatus = Status.NEW;
        for (Subtask subtask : epic.getSubtasks())
            if (subtask.getStatus().compareTo(newStatus) > 0) {
                newStatus = subtask.getStatus();
            }
        epic.setStatus(newStatus);
    }

    @Override
    public void createNewEpic(Epic epic) {
        int newId = generateNewId();
        epic.setId(newId);
        tasks.put(newId, epic);
        epics.put(newId, epic);
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        int newId = generateNewId();
        subtask.setId(newId);
        subtasks.put(newId, subtask);
        Epic epic = subtask.getEpic();
        epic.getSubtasks().add(subtask);
        updateEpicStatus(epic);
        tasks.put(newId, subtask);
    }

    @Override
    public void addSubtaskToEpic(Subtask subtask, Epic epic) {
        subtasks.put(subtask.getId(), subtask);
        epic.getSubtasks().add(subtask);
        updateEpicStatus(epic);
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Task> getHistory() { // оставила как getHistory
        return historyManager.getHistory();
    }
}
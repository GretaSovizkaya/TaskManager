package managers;

import basis.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private static int newId = 1;

    //убрала поле history
    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    //использую Managers
    public InMemoryTaskManager() {
        this(Managers.getDefaultHistory());
    }

    // updateTaskHistory() убрала
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
        System.out.println("Добавили задачку!");
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
        historyManager.add(task);
    }

    @Override
    public void removeTaskById(Integer id) {
        Task removedTask = tasks.remove(id);
        if (removedTask != null) {
            historyManager.add(removedTask); // Добавление удаленной задачи в историю
        }
        epics.remove(id); // Удаление эпика по id
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
        historyManager.add(epic);
    }

    @Override
    public void createNewEpic(Epic epic) {
        int newId = generateNewId();
        epic.setId(newId);
        tasks.put(newId, epic);
        epics.put(newId, epic);
        historyManager.add(epic); // добавляю в history
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
        historyManager.add(subtask);
    }
    @Override
    public void addSubtaskToEpic(Subtask subtask, Epic epic) {
        subtasks.put(subtask.getId(), subtask);
        epic.getSubtasks().add(subtask);
        updateEpicStatus(epic); // Обновление статуса эпика
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
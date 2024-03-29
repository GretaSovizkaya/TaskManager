import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager;
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private List<Task> history = new ArrayList<>(10);
    private static int newId = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public InMemoryTaskManager() {
        this(new InMemoryHistoryManager());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateTaskHistory(Task task) {
        if (task != null) {
            if (history.size() >= 10) {
                history.remove(0);
            }
            history.add(task);
        }
    }

    public List<Task> getAllTypesTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks(Task task) {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(Integer id) {
        Task task = tasks.get(id);
        if (task != null) {
            updateTaskHistory(task);
        }
        return task;
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
        if (epics.containsKey(id)) {
            epics.remove(id);
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

    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            updateTaskHistory(subtask);
        }
        return subtask;
    }

    public Epic getEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            updateTaskHistory(epic);
        }
        return epic;
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

}
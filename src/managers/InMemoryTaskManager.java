package managers;

import basis.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HistoryManager historyManager;
    protected Map<Integer, Task> tasks = new HashMap<>();
    protected Map<Integer, Epic> epics = new HashMap<>();
    protected Map<Integer, Subtask> subtasks = new HashMap<>();
    protected static int newId = 1;
    public final TreeSet <Task> priorityTasks;
    static Comparator<Task> comparator = Comparator.comparing(Task::getStartTime);

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
        priorityTasks = new TreeSet<>(comparator);
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
        if (task.getStartTime() != null) {
            priorityTasks.add(task);
        }
    }

    private Integer generateNewId() {
        return newId++;
    }
    public LocalDateTime getEndTime(Task task) {
        if (task instanceof Epic) {
            LocalDateTime time = subtasks.get(((Epic) task).getSubtasks().getFirst()).getStartTime();
            ((Epic) task).getSubtasks().stream()
                    .map(i -> subtasks.get(i).getDuration())
                    .filter(Objects::nonNull)
                    .peek(time::plus);

            return time;
        }
        if (checkStartTime(task)) {
            return task.getStartTime().plus(task.getDuration());

        }
        throw new RuntimeException("Чекните временной интервал, ошибка");

    }
    public TreeSet<Task> getPrioritizedTasks() { // вовзращаем список по прриоритету
        return priorityTasks;
    }

    protected boolean checkStartTime(Task task) {
        return task.getStartTime() != null;
    }
    private boolean Overlap(Task task1, Task task2) {
        return !getEndTime(task1).isBefore(task2.getStartTime())
                && !getEndTime(task2).isBefore(task1.getStartTime());
    }

    public boolean checkTime(Task task) {
        Optional<Task> first = priorityTasks.stream()
                .filter(existingTask -> Overlap(existingTask, task))
                .findFirst();
        return first.isPresent();
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        if (task instanceof Epic) {
            epics.put(task.getId(), (Epic) task);
        }
    }

    @Override
    public void updateEpic(Epic epic){
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }
    @Override
    public void updateSubtask(Subtask subtask){
        subtasks.put(subtask.getId(), subtask);
        Epic epic = subtask.getEpic();
        if (epic != null) {
            updateEpicStatus(epic);
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
        Epic epic = epics.get(epicId);
        if (epic != null) {
            return new ArrayList<>(epic.getSubtasks());
        }
        return new ArrayList<>();
    }
    private void updateEpicStatus(Epic epic) {
        if (!epic.getSubtasks().isEmpty()) {
            Status newStatus = Status.NEW;
            for (Subtask subtask : epic.getSubtasks())
                if (subtask.getStatus().compareTo(newStatus) > 0) {
                    newStatus = subtask.getStatus();
                }
            epic.setStatus(newStatus);
        }
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
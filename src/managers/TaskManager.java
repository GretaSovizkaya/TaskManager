package managers;

import basis.*;

import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    List<Task> getAllTypesTasks();

    void deleteAllTasks(Task task);

    Task getTaskById(Integer id);

    void createNewTask(Task task);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void removeTaskById(Integer id);

    List<Subtask> getSubtasksOfEpic(Integer epicId);

    void addSubtaskToEpic(Subtask subtask, Epic epic);

    void createNewEpic(Epic epic);

    List<Task> getHistory();

    Subtask getSubtaskById(Integer id); 

    Epic getEpicById(Integer id);

    List<Epic> getAllEpics();

    void createNewSubtask(Subtask subtask);

    void removeEpicById(Integer id);

    void removeSubtaskById(Integer id);

    TreeSet<Task> getPrioritizedTasks();
}
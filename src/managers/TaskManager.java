package managers;

import basis.*;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTypesTasks();

    void deleteAllTasks(Task task);

    Task getTaskById(Integer id);

    void createNewTask(Task task);

    void updateTask(Task task);

    void removeTaskById(Integer id);

    List<Subtask> getSubtasksOfEpic(Integer epicId);

    void addSubtaskToEpic(Subtask subtask, Epic epic);

    void updateEpicStatus(Epic epic);

    void createNewEpic(Epic epic);

    List<Task> getTasks();

    Subtask getSubtaskById(Integer id); //добавлено

    Epic getEpicById(Integer id); //добавлено

    List<Epic> getAllEpics(); //добавлено

    void createNewSubtask(Subtask subtask);

    void removeEpicById(Integer id);

    void removeSubtaskById(Integer id);
}
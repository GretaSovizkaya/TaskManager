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
    List<Task> getHistory();
}
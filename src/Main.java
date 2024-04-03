import basis.Epic;
import basis.Status;
import basis.Subtask;
import basis.Task;
import managers.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        HistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager manager = new InMemoryTaskManager(historyManager);


        Task task1 = new Task("Задача1", "описание задачи1");
        Task task2 = new Task("Задача2", "описание задачи2");

        Epic epic1 = new Epic(3, "Эпик 1", "Описание эпика 1");
        Epic epic2 = new Epic(4, "Эпик 2", "Описание эпика 2");

        Subtask subtask1 = new Subtask(5, "Подзадача 1", "Описание подзадачи 1",epic1);
        Subtask subtask2 = new Subtask(6, "Подзадача эпика 2", "Описание подзадачи", epic2);

        manager.createNewTask(task1);
        manager.createNewTask(task2);

        manager.createNewEpic(epic1);
        manager.createNewEpic(epic2);

        manager.addSubtaskToEpic(subtask1, epic1);
        manager.addSubtaskToEpic(subtask2, epic2);

        task2.setStatus(Status.IN_PROGRESS);
        epic2.setStatus(Status.DONE);

        manager.removeTaskById(1);
        manager.removeTaskById(3);

        subtask2.setStatus(Status.IN_PROGRESS); // Изменяем статус подзадачи
        manager.updateEpicStatus(epic1);

        List<Task> history = manager.getHistory();

        System.out.println("Задачи:");
        for (Task task : manager.getAllTypesTasks()) {
            System.out.println(task);
        }

        // Выводим историю изменений на экран
        System.out.println("История изменений:");
        for (Task task : history) {
            System.out.println(task);
        }

    }

}
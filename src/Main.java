

import basis.Epic;
import basis.Status;
import basis.Subtask;
import basis.Task;
import managers.FileBackedTaskManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String[] args) {
        try {
            // Создаем временный файл
            File tempFile = File.createTempFile("tasks", ".csv");

            // Создаем FileBackedTaskManager с временным файлом
            FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

            // Создаем задачи
            Task task1 = new Task("Task1", "Description task1");
            task1.setStatus(Status.NEW);
            manager.createNewTask(task1);

            Epic epic2 = new Epic("Epic2", "Description epic2");
            epic2.setStatus(Status.DONE);
            manager.createNewEpic(epic2);

            Subtask subtask3 = new Subtask("Sub Task3", "Description sub task3", epic2);
            subtask3.setStatus(Status.DONE);
            manager.createNewSubtask(subtask3);

            // Сохраняем задачи в файл
            manager.save();

            // Выводим содержимое файла для проверки
            System.out.println("Saved tasks to file:");
            Files.lines(tempFile.toPath()).forEach(System.out::println);

            // Загружаем задачи из файла
            FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);

            // Выводим загруженные задачи для проверки
            System.out.println("\nLoaded tasks from file:");
            for (Task task : loadedManager.getAllTypesTasks()) {
                System.out.println(task);
            }

            // Удаляем временный файл
            tempFile.deleteOnExit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

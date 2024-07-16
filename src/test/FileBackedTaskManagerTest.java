package test;

import basis.*;
import managers.FileBackedTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private File tempFile;
    private FileBackedTaskManager manager;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("tasks", ".csv");
        manager = new FileBackedTaskManager(tempFile);
    }

    @AfterEach
    public void tearDown() {
        tempFile.delete();
    }

    @Test
    public void testSaveAndLoadEmptyFile() {
        //manager.save();
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(loadedManager.getAllTypesTasks().isEmpty(), "Task list should be empty");
    }

    @Test
    public void testSaveMultipleTasks() {
        Task task1 = new Task("Task", "smth", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Epic epic = new Epic("Epic", "smth");
        Subtask subtask = new Subtask("Subtask", "smth", epic);

        manager.createNewTask(task1);
        manager.createNewEpic(epic);
        manager.createNewSubtask(subtask);

        final int idTask = task1.getId();
        final int idSubtask = subtask.getId();
        final int idEpic = epic.getId();

        assertEquals(task1,manager.getTaskById(idTask));
        assertEquals(subtask,manager.getSubtaskById(idSubtask));
        assertEquals(epic,manager.getEpicById(idEpic));
    }

    @Test
    public void createAndSaveTaskFromFile() throws IOException {
        Task task = new Task("task", "smth", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        LocalDate data = LocalDate.of(2024, 7, 06);
        LocalTime time = LocalTime.now();
        LocalDateTime dataTime = LocalDateTime.of(data, time);
        Duration duration = Duration.ofMinutes(12_545_655);
        task.setStartTime(dataTime);
        task.setDuration(duration);

        manager.createNewTask(task);

        assertNotNull(manager.getAllTypesTasks());
    }

}

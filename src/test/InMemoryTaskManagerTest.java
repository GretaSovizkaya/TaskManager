package test;

import basis.*;
import managers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    public static InMemoryTaskManager manager;

    @BeforeEach
    public void setup() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void nonMatchingTasksId() {
        Task task1 = new Task("Task", "Description", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        task1.setId(1);
        Task task2 = new Task("Task", "Description", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        task2.setId(1);

        Assertions.assertEquals(task1, task2, "ошибка");
    }

    @Test
    public void nonMatchingSubtasksId() {
        Epic epic = new Epic("Epic", "Description");
        Subtask subtask1 = new Subtask("Subtask", "Description", epic);
        subtask1.setId(2); // Задаем одинаковые id для подзадач
        Subtask subtask2 = new Subtask("Subtask", "Description", epic);
        subtask2.setId(2);

        Assertions.assertEquals(subtask1, subtask2, "ошибка");
    }

    @Test
    public void nonMatchingEpicId() {
        Epic epic = new Epic("Epic1", "Hello Hello");
        epic.setId(1);

        Epic epic1 = new Epic("Epic1", "Hello Hello");
        epic1.setId(1);

        assertEquals(epic, epic1, "Ошибка");
    }

    @Test
    public void shouldNotAllowSubtaskToBeItsOwnEpic() {
        Subtask subtask = new Subtask("Subtask", "Description", null);

        subtask.setEpic(subtask);

        assertNotSame(subtask, subtask.getEpic());
    }


    @Test
    public void shouldAddDifferentTypesOfTasksToManager() {
        // строку удалила
        Task task = new Task("Task", "Description", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Epic epic = new Epic("Epic", "Description");
        Subtask subtask = new Subtask("Subtask", "Description", epic);

        manager.createNewTask(task);
        manager.createNewTask(epic);
        manager.createNewSubtask(subtask);


        List<Task> tasks = manager.getAllTypesTasks();

        Assertions.assertTrue(tasks.contains(task));
        Assertions.assertTrue(tasks.contains(epic));
        Assertions.assertTrue(tasks.contains(subtask));
    }

    @Test
    public void shouldNotModifyTaskWhenAddingToManager() {
        Task task = new Task("Task", "Description", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        manager.createNewTask(task);
        Task retrievedTask = manager.getTaskById(task.getId());
        Assertions.assertEquals(task, retrievedTask);
    }


    @Test
    public void shouldNotConflictGeneratedAndSpecifiedTaskIds() {
        Task task1 = new Task("Task 1 ", "Description", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        task1.setId(1);
        manager.createNewTask(task1);

        Task retrievedTask = manager.getTaskById(task1.getId());
        Assertions.assertEquals(task1, retrievedTask);
    }

    @Test
    public void shouldReturnEndTimeOfTask() {
        Task task = new Task("Task", "Description", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        LocalDateTime startTime = LocalDateTime.now();
        Duration duration = Duration.ofHours(1);
        task.setStartTime(startTime);
        task.setDuration(duration);

        manager.createNewTask(task);

        LocalDateTime expectedEndTime = startTime.plus(duration);
        Assertions.assertEquals(expectedEndTime, manager.getEndTime(task));
    }
    @Test
    public void CheckTimeTest() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now();
        task1.setStartTime(now.plusHours(1));
        task1.setDuration(Duration.ofHours(2));
        task2.setStartTime(now.plusHours(4));
        task2.setDuration(Duration.ofHours(2));

        manager.createNewTask(task1);

        assertFalse(manager.checkTime(task2));
    }

    @Test
    public void OverlapTest() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now();
        task1.setStartTime(now.plusHours(1));
        task1.setDuration(Duration.ofHours(2));
        task2.setStartTime(now.plusHours(2));
        task2.setDuration(Duration.ofHours(2));

        manager.createNewTask(task1);
        manager.createNewTask(task2);

        assertTrue(manager.checkTime(task2));
    }
    @Test
    public void testGetPrioritizedTasks() {
        Task task1 = new Task("Task 1", "Description 1", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        Task task3 = new Task("Task 3", "Description 3", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now();
        task1.setStartTime(now.plusHours(3));
        task1.setDuration(Duration.ofHours(1));
        task2.setStartTime(now.plusHours(1));
        task2.setDuration(Duration.ofHours(1));
        task3.setStartTime(now.plusHours(2));
        task3.setDuration(Duration.ofHours(1));

        manager.createNewTask(task1);
        manager.createNewTask(task2);
        manager.createNewTask(task3);

        TreeSet<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertEquals(3, prioritizedTasks.size());
        assertEquals(task2, prioritizedTasks.first());
        assertEquals(task1, prioritizedTasks.last());
    }
}


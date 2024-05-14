package test;

import basis.*;
import managers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    public static InMemoryTaskManager manager;

    @BeforeEach
    public void setup() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void nonMatchingTasksId() {
        Task task1 = new Task("Task", "Description");
        task1.setId(1);
        Task task2 = new Task("Task", "Description");
        task2.setId(1);

        Assertions.assertEquals(task1, task2, "ошибка");
    }

    @Test
    public void nonMatchingSubtasksId() {
        Epic epic = new Epic(1, "Epic", "Description");
        Subtask subtask1 = new Subtask(2, "Subtask", "Description", epic);
        subtask1.setId(2); // Задаем одинаковые id для подзадач
        Subtask subtask2 = new Subtask(2, "Subtask", "Description", epic);
        subtask2.setId(2);

        Assertions.assertEquals(subtask1, subtask2, "ошибка");
    }

    @Test
    public void nonMatchingEpicId() {
        Epic epic = new Epic(1, "Epic1", "Hello Hello");
        epic.setId(1);

        Epic epic1 = new Epic(1, "Epic1", "Hello Hello");
        epic1.setId(1);

        assertEquals(epic, epic1, "Ошибка");
    }

    @Test
    public void shouldNotAllowSubtaskToBeItsOwnEpic() {
        Subtask subtask = new Subtask(1, "Subtask", "Description", null);

        subtask.setEpic(subtask);

        assertNotSame(subtask, subtask.getEpic());
    }


    @Test
    public void shouldAddDifferentTypesOfTasksToManager() {
        // строку удалила
        Task task = new Task("Task", "Description");
        Epic epic = new Epic(2, "Epic", "Description");
        Subtask subtask = new Subtask(1, "Subtask", "Description", epic);

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
        Task task = new Task("Task", "Description");
        manager.createNewTask(task);
        Task retrievedTask = manager.getTaskById(task.getId());
        Assertions.assertEquals(task, retrievedTask);
    }


    @Test
    public void shouldNotConflictGeneratedAndSpecifiedTaskIds() {
        Task task1 = new Task("Task 1 ", "Description");
        task1.setId(1);
        manager.createNewTask(task1);

        Task retrievedTask = manager.getTaskById(task1.getId());
        Assertions.assertEquals(task1, retrievedTask);
    }
}


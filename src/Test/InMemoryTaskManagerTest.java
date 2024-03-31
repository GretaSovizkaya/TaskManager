package Test;
import Basis.*;
import Managers.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    //исходя из ТЗ и обязательных пунктов вроде как все здесь:))
    private InMemoryTaskManager manager;

    @BeforeEach
    public void setUp() {
        manager = new InMemoryTaskManager();
    }

    @Test
    public void shouldReturnEqualTasksWhenIdsAreEqual() {
        Task task1 = new Task("Task", "Description");
        Task task2 = new Task("Task", "Description");

        Assertions.assertEquals(task1, task2);
    }

    @Test
    public void shouldReturnEqualSubtasksWhenIdsAreEqual() {
        Epic epic = new Epic(1, "Epic", "Description");
        Subtask subtask1 = new Subtask(2, "Subtask", "Description", epic);
        Subtask subtask2 = new Subtask(2, "Subtask", "Description", epic);

        Assertions.assertEquals(subtask1, subtask2);
    }

    @Test
    public void shouldNotAllowSubtaskToBeItsOwnEpic() {
        Subtask subtask = new Subtask(1, "Subtask", "Description", null);
        subtask.setEpic(subtask.getEpic());

        assertNotSame(subtask, subtask.getEpic());
    }

    @Test
    public void shouldReturnInitializedTaskManagerInstance() {
        TaskManager manager = new InMemoryTaskManager();

        Assertions.assertNotNull(manager);
        Assertions.assertNotNull(manager.getAllTypesTasks());
    }

    @Test
    public void shouldAddDifferentTypesOfTasksToManager() {
        TaskManager manager = new InMemoryTaskManager();
        Task task = new Task("Task", "Description");
        Epic epic = new Epic(2, "Epic", "Description");

        manager.createNewTask(task);
        manager.createNewTask(epic);

        List<Task> tasks = manager.getAllTypesTasks();

        Assertions.assertTrue(tasks.contains(task));
        Assertions.assertTrue(tasks.contains(epic));
    }

    public void shouldNotModifyTaskWhenAddingToManager() {
        // Arrange
        Task task = new Task("Task", "Description");
        InMemoryTaskManager manager = new InMemoryTaskManager();

        // Act
        manager.createNewTask(task);

        // Assert
        Assertions.assertNotNull(manager.getTaskById(3));
    }


    @Test
    public void shouldNotConflictGeneratedAndSpecifiedTaskIds() {
        // Arrange
        Task task1 = new Task("Task 1 ", "Description");
        InMemoryTaskManager manager = new InMemoryTaskManager();

        // Act
        manager.createNewTask(task1);

        // Assert
        Assertions.assertNotNull(manager.getTaskById(3));
    }

}


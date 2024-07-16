package testHttp;

import basis.Status;
import basis.Task;
import org.junit.jupiter.api.Test;
import testHttp.HttpTaskServerTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskHandlerTest extends HttpTaskServerTest {

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Testing task", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTypesTasks();
        assertNotNull(tasksFromManager, "Tasks not returned");
        assertEquals(1, tasksFromManager.size(), "Incorrect number of tasks");
        assertEquals("Test Task", tasksFromManager.get(0).getName(), "Incorrect task name");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Testing task", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        manager.createNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task[] tasks = gson.fromJson(response.body(), Task[].class);
        assertNotNull(tasks, "Tasks not returned");
        assertEquals(1, tasks.length, "Incorrect number of tasks");
        assertEquals("Test Task", tasks[0].getName(), "Incorrect task name");
    }


    @Test
    public void testDeleteTasks() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Testing task", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        manager.createNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTypesTasks();
        assertEquals(0, tasksFromManager.size(), "Tasks not deleted");
    }
}

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

public class HistoryHandlerTest extends HttpTaskServerTest {

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        Task task = new Task("Test Task", "Testing task", Status.NEW, Duration.ofMinutes(30), LocalDateTime.now());
        manager.createNewTask(task);
        manager.getTaskById(task.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task[] history = gson.fromJson(response.body(), Task[].class);
        assertNotNull(history, "History not returned");
        assertEquals(1, history.length, "Incorrect number of history items");
        assertEquals("Test Task", history[0].getName(), "Incorrect history task name");
    }
}

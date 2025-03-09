package handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import basis.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String method = httpExchange.getRequestMethod();
            String response = "";

            switch (method) {
                case "GET":
                    response = handleGet(path);
                    break;
                case "POST":
                    response = handlePost(httpExchange.getRequestBody());
                    break;
                case "DELETE":
                    handleDelete(path);
                    break;
                default:
                    sendNotFound(httpExchange, "Method not allowed");
            }

            sendText(httpExchange, response);
        } catch (IOException e) {
            sendNotFound(httpExchange, "IOException occurred: " + e.getMessage());
        } catch (Exception e) {
            sendNotFound(httpExchange, "Exception occurred: " + e.getMessage());
        }
    }

    private String handleGet(String path) throws IOException {
        List<Task> tasks = taskManager.getAllTypesTasks();
        return gson.toJson(tasks);
    }

    private String handlePost(InputStream requestBody) throws IOException {
        InputStreamReader reader = new InputStreamReader(requestBody, StandardCharsets.UTF_8);
        Task task = gson.fromJson(reader, Task.class);
        taskManager.createNewTask(task);
        return gson.toJson(task);
    }

    private void handleDelete(String path) throws IOException {
        taskManager.deleteAllTasks(null);
    }
}

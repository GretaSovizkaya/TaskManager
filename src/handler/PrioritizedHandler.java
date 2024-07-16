package handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import basis.Task;

import java.io.IOException;
import java.util.TreeSet;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if ("GET".equals(method)) {
                String response = handleGet();
                sendText(exchange, response);
            } else {
                sendNotFound(exchange, "Method Not Allowed");
            }
        } catch (IOException e) {
            sendNotFound(exchange, "IOException occurred: " + e.getMessage());
        } catch (Exception e) {
            sendNotFound(exchange, "Exception occurred: " + e.getMessage());
        }
    }

    private String handleGet() throws IOException {
        TreeSet<Task> prioritizedTasks = taskManager.getPrioritizedTasks(); // Исправлено на taskManager
        return gson.toJson(prioritizedTasks);
    }
}

package handler;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import basis.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager manager) {
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
        } catch (Exception e) {
            sendNotFound(exchange, "Internal Server Error: " + e.getMessage());
        }
    }

    private String handleGet() {
        List<Task> history = taskManager.getHistory();
        return gson.toJson(history);
    }
}

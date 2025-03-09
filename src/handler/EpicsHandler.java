package handler;

import basis.Epic;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class EpicsHandler extends BaseHttpHandler {

    public EpicsHandler(TaskManager manager) {
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
                    sendText(httpExchange, response);
                    break;
                case "POST":
                    response = handlePost(httpExchange.getRequestBody());
                    sendText(httpExchange, response);
                    break;
                case "DELETE":
                    handleDelete(path);
                    sendText(httpExchange, "");
                    break;
                default:
                    sendNotFound(httpExchange, "Method not allowed");
            }
        } catch (IOException e) {
            sendNotFound(httpExchange, "IOException occurred: " + e.getMessage());
        } catch (NumberFormatException e) {
            sendNotFound(httpExchange, "NumberFormatException occurred: " + e.getMessage());
        } catch (Exception e) {
            sendNotFound(httpExchange, "Exception occurred: " + e.getMessage());
        }
    }

    private String handleGet(String path) throws IOException {
        String idStr = path.substring(path.lastIndexOf("/") + 1);
        int id = parseInt(idStr);
        Epic epic = taskManager.getEpicById(id);
        return gson.toJson(epic);
    }

    private String handlePost(InputStream requestBody) throws IOException {
        InputStreamReader reader = new InputStreamReader(requestBody, StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(reader, Epic.class);
        taskManager.createNewEpic(epic);
        return gson.toJson(epic);
    }

    private void handleDelete(String path) throws IOException {
        String idStr = path.substring(path.lastIndexOf("/") + 1);
        int id = parseInt(idStr);
        taskManager.removeSubtaskById(id);
    }
}

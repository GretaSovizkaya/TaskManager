package handler;

import basis.Epic;
import basis.Subtask;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtasksHandler extends BaseHttpHandler{
    public SubtasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String response = "";
        try{
            switch (method){
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
                    sendNotFound(httpExchange,"Method not allowed");
            }

        } catch (Exception e){
            sendNotFound(httpExchange, e.getMessage());
        }
    }

    private String handleGet(String path) {
        String idStr = path.substring(path.lastIndexOf("/") + 1);
        int id = Integer.parseInt(idStr);
        Subtask subtask = taskManager.getSubtaskById(id);
        return gson.toJson(subtask);
    }

    private String handlePost(InputStream requestBody) throws IOException {
        InputStreamReader reader = new InputStreamReader(requestBody, StandardCharsets.UTF_8);
        Subtask subtask = gson.fromJson(reader, Subtask.class);
        taskManager.createNewSubtask(subtask);
        return gson.toJson(subtask);
    }

    private void handleDelete(String path) {

        String idStr = path.substring(path.lastIndexOf("/") + 1);
        int id = Integer.parseInt(idStr);
        taskManager.removeSubtaskById(id);
    }
}

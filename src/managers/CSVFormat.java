package managers;

import basis.Epic;
import basis.Status;
import basis.Subtask;
import basis.Task;

public class CSVFormat {
    protected static String taskToString(Task task) {
        String type = task instanceof Epic ? "EPIC" : task instanceof Subtask ? "SUBTASK" : "TASK";
        String epicId = task instanceof Subtask ? String.valueOf(((Subtask) task).getEpic().getId()) : "";
        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                type,
                task.getName(),
                task.getStatus(),
                task.getDescription(),
                epicId);
    }
    protected static Task fromString(String value, FileBackedTaskManager manager) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String type = fields[1];
        String name = fields[2];
        Status status = Status.valueOf(fields[3]);
        String description = fields[4];
        Task task;

        switch (type) {
            case "EPIC":
                task = new Epic(name, description);
                task.setId(id);
                task.setStatus(status);
                break;
            case "SUBTASK":
                Epic epic = manager.epics.get(Integer.parseInt(fields[5]));
                task = new Subtask(name, description, epic);
                task.setId(id);
                task.setStatus(status);
                break;
            default:
                task = new Task(name, description);
                task.setId(id);
                task.setStatus(status);
        }

        return task;
    }
}

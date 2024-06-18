package managers;

import basis.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
    }

    @Override
    public void createNewSubtask(Subtask subtask) {
        super.createNewSubtask(subtask);
        save();
    }

    @Override
    public void addSubtaskToEpic(Subtask subtask, Epic epic) {
        super.addSubtaskToEpic(subtask, epic);
        save();
    }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(Integer id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        save();
    }

    public void save() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("id,type,name,status,description,epic\n");

            for (Task task : tasks.values()) {
                sb.append(taskToString(task)).append("\n");
            }
            for (Epic epic : epics.values()) {
                sb.append(taskToString(epic)).append("\n");
            }
            for (Subtask subtask : subtasks.values()) {
                sb.append(taskToString(subtask)).append("\n");
            }

            Files.writeString(file.toPath(), sb.toString());
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    private String taskToString(Task task) {
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

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                if (line.equals("id,type,name,status,description,epic")) continue;
                Task task = fromString(line, manager);
                if (task instanceof Epic) {
                    manager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    manager.subtasks.put(task.getId(), (Subtask) task);
                    manager.epics.get(((Subtask) task).getEpic().getId()).getSubtasks().add((Subtask) task);
                } else {
                    manager.tasks.put(task.getId(), task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
        return manager;
    }

    private static Task fromString(String value, FileBackedTaskManager manager) {
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

    static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(final Throwable cause) {
            super(cause);
        }
    }
}

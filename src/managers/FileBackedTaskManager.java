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

    private void save() {//исправлен модификатор доступа
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("id,type,name,status,description,epic\n");

            for (Task task : tasks.values()) {
                sb.append(CSVFormat.taskToString(task)).append("\n");
            }
            for (Epic epic : epics.values()) {
                sb.append(CSVFormat.taskToString(epic)).append("\n");
            }
            for (Subtask subtask : subtasks.values()) {
                sb.append(CSVFormat.taskToString(subtask)).append("\n");
            }

            Files.writeString(file.toPath(), sb.toString());
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines) {
                if (line.equals("id,type,name,status,description,epic")) continue;
                Task task = CSVFormat.fromString(line, manager);
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


    static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(final Throwable cause) {
            super(cause);
        }
    }
}

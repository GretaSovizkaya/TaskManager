package basis;

import java.util.*;

public class Epic extends Task {
    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(int id, String name, String description) {
        super(name, description);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

}
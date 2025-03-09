package basis;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime; // field endTime
    private List<Subtask> subtasks = new ArrayList<>();
    public Epic(String name, String description) {
        super(name, description);
        this.duration = Duration.ZERO;
        this.startTime = LocalDateTime.now();
        this.endTime = LocalDateTime.now();
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

}